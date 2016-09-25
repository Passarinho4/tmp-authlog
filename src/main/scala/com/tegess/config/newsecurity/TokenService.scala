package com.tegess.config.newsecurity

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import com.avsystem.commons.jiop.JavaInterop._
import com.tegess.domain.admin.Admin
import com.tegess.persistance.service.admin.AdminService
import com.tegess.persistance.service.application.ApplicationService
import io.jsonwebtoken._
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.codec.Base64

import scala.util.{Failure, Success, Try}

class TokenService(val adminService: AdminService, val applicationService: ApplicationService) {

  import TokenService._

  def addAuthentication(response: HttpServletResponse, authenticatedUser: Admin) {
    response.addHeader(AUTH_HEADER_NAME, createTokenForAdmin(authenticatedUser))
    response.addHeader(ACCESS_CONTROL_EXPOSE_HEADERS, X_AUTH_TOKEN)

  }

  def getAuthentication(request: HttpServletRequest): Authentication = {
    val token: String = request.getHeader(AUTH_HEADER_NAME)
    if (token != null) {
      val user: Admin = parseUserFromToken(token)
      if (user != null) {
        return new AdminAuthentication(user)
      }
    }
    null
  }

  private def parseUserFromToken(token: String): Admin = {
    val result = for {
      applicationId <- JwtTokenApplicationIdParser.parse(token)
      secret <- Try(applicationService.findOne(applicationId).get.fbLogin.get.secret)
    } yield {
      val claims: Claims = Jwts.parser.setSigningKey(Base64.encode(secret.getBytes)).parseClaimsJws(token).getBody
      val userDetails: UserDetails = adminService.loadUserByUsername(applicationId.toHexString + claims.get(USERNAME_KEY, classOf[String]))
      val admin: Admin = Admin(userDetails.getUsername, userDetails.getPassword, userDetails.getAuthorities.asScala.toList)
      admin
    }
    result match {
      case Success(admin) => admin
      case Failure(e) => throw e
    }
  }

  private def createTokenForAdmin(admin: Admin): String = {
    val result = for {
      application <- Try(applicationService.findOne(admin.getApplicationId).get)
      secret <- Try(application.fbLogin.get.secret) // TODO create special secret field in application (split app secret from fb secret)
    } yield {
      val jwtBuilder: JwtBuilder = Jwts.builder
        .setSubject(admin.getRealUsername)
        .signWith(SignatureAlgorithm.HS256, Base64.encode(secret.getBytes))
        .setHeaderParam(TYP_KEY, JWT)
        .setHeaderParam(APPLICATION_KEY, application.id.toHexString)

      val authoritiesAsString: List[String] = admin.getAuthorities.asScala.map(_.getAuthority).toList
      val hashMap: JHashMap[String, AnyRef] = new JHashMap[String, AnyRef]
      hashMap.put(USERNAME_KEY, admin.getRealUsername)
      hashMap.put(AUTHORITIES_KEY, authoritiesAsString)
      jwtBuilder.setClaims(hashMap)
      jwtBuilder.compact
    }

    result match {
      case Success(s) => s
      case Failure(e) => throw new CantCreateTokenForAdmin(e)
    }
  }
}
object TokenService {

  val TYP_KEY = "typ"
  val JWT = "JWT"
  val AUTHORIZATION_HEADER_DELIMITER = ":"
  val APPLICATION_KEY = "application"
  val AUTHORIZATION_HEADER = "Authorization"
  val USERNAME_KEY = "username"
  val AUTHORITIES_KEY = "authorities"
  val AUTH_HEADER_NAME: String = "X-AUTH-TOKEN"
  val ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers"
  val X_AUTH_TOKEN = "X-AUTH-TOKEN"

  case class CantCreateTokenForAdmin(e: Throwable) extends Exception(e)
}