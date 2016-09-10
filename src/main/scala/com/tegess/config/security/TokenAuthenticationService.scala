package com.tegess.config.security

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import org.springframework.security.core.{Authentication, GrantedAuthority}
import com.avsystem.commons.jiop.JavaInterop._
import com.tegess.domain.admin.Admin
import com.tegess.persistance.service.admin.AdminService

class TokenAuthenticationService(val secret: String, val adminService: AdminService) {
  private val AUTH_HEADER_NAME: String = "X-AUTH-TOKEN"

  final private val tokenHandler: TokenHandler = new TokenHandler(secret, adminService)

  def addAuthentication(response: HttpServletResponse, adminAuthentication: AdminAuthentication) {
    val username: String = adminAuthentication.getName
    val authorities: List[_ <: GrantedAuthority] = adminAuthentication.getAuthorities.asScala.toList
    response.addHeader(AUTH_HEADER_NAME, tokenHandler.createTokenForAdmin(username, authorities))
    response.addHeader("Access-Control-Expose-Headers", "X-AUTH-TOKEN")

  }

  def getAuthentication(request: HttpServletRequest): Authentication = {
    val token: String = request.getHeader(AUTH_HEADER_NAME)
    if (token != null) {
      val user: Admin = tokenHandler.parseUserFromToken(token)
      if (user != null) {
        return new AdminAuthentication(user)
      }
    }
    null
  }
}
