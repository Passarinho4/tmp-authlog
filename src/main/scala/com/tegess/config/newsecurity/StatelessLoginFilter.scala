package com.tegess.config.newsecurity

import javax.servlet.FilterChain
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import com.avsystem.commons.jiop.JavaInterop._
import com.tegess.domain.admin.Admin
import com.tegess.persistance.service.admin.AdminService
import com.tegess.persistance.service.application.ApplicationService
import com.tegess.persistance.service.event.LoginEventService
import com.tegess.persistance.service.user.UserService
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.{AuthenticationCredentialsNotFoundException, AuthenticationManager, UsernamePasswordAuthenticationToken}
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.stereotype.Component

import scala.util.{Failure, Success, Try}

@Component class StatelessLoginFilter(val urlMapping: String,
                                      val authenticationManager: AuthenticationManager,
                                      val adminService: AdminService,
                                      val tokenService: TokenService) extends AbstractAuthenticationProcessingFilter(new AntPathRequestMatcher(urlMapping)) {
  setAuthenticationManager(authenticationManager)

  @Autowired var loginEventService: LoginEventService = _
  @Autowired var applicationService: ApplicationService = _
  @Autowired var userService: UserService = _


  def attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication = {
    val authenticationTry = for {
      applicationId <- Try(new ObjectId(request.getServletPath.split("/")(3))) // SPLIT
      header <- Try(Option(request.getHeader(TokenService.AUTHORIZATION_HEADER)).get)
      login <- Try(applicationId + header.split(TokenService.AUTHORIZATION_HEADER_DELIMITER)(0))
      password <- Try(header.split(TokenService.AUTHORIZATION_HEADER_DELIMITER)(1))
      loginToken = new UsernamePasswordAuthenticationToken(login, password)
    } yield {
      getAuthenticationManager.authenticate(loginToken)
    }

    authenticationTry match {
      case Success(a) => a
      case Failure(e:StringIndexOutOfBoundsException) =>
        throw new AuthenticationCredentialsNotFoundException("Wrong header format")
      case Failure(e:NoSuchElementException) =>
        throw new AuthenticationCredentialsNotFoundException("Header Authorization not found")
      case Failure(e) => throw e
    }

  }

  override protected def successfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain, authentication: Authentication) {
    for {
      userDetails <- Try(adminService.loadUserByUsername(authentication.getPrincipal.asInstanceOf[Admin].getUsername))
      //probably unnecessary
      authenticatedUser = Admin(userDetails.getUsername, userDetails.getPassword, userDetails.getAuthorities.asScala.toList)
      userAuthentication = new AdminAuthentication(authenticatedUser)
    } {
      loginEventService.publishLoginEvent(authenticatedUser.getApplicationId, authenticatedUser.getRealUsername)
      tokenService.addAuthentication(response, authenticatedUser)
      SecurityContextHolder.getContext.setAuthentication(authentication)
    }
  }
}
