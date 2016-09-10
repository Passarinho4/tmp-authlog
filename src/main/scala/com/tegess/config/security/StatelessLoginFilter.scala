package com.tegess.config.security

import javax.servlet.FilterChain
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import com.avsystem.commons.jiop.JavaInterop._
import com.tegess.domain.admin.Admin
import com.tegess.persistance.service.admin.AdminService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.{AuthenticationCredentialsNotFoundException, AuthenticationManager, UsernamePasswordAuthenticationToken}
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.stereotype.Component

@Component class StatelessLoginFilter(val urlMapping: String, val authenticationManager: AuthenticationManager) extends AbstractAuthenticationProcessingFilter(new AntPathRequestMatcher(urlMapping)) {
  setAuthenticationManager(authenticationManager)
  @Autowired private val adminService: AdminService = null
  @Autowired private val tokenAuthenticationService: TokenAuthenticationService = null

  def attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication = {
    val header: Option[String] = Option(request.getHeader("Authorization"))

    if(header.isEmpty) {
      throw new AuthenticationCredentialsNotFoundException("Header Authorization not found")
    }

    try {
      val login: String = header.get.split(":")(0)
      val password: String = header.get.split(":")(1).split(",")(0)
      val loginToken: UsernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(login, password)
      getAuthenticationManager.authenticate(loginToken)
    }
    catch {
      case e: IndexOutOfBoundsException => {
        throw new AuthenticationCredentialsNotFoundException("Wrong header format..")
      }
    }
  }

  override protected def successfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain, authentication: Authentication) {
    val userDetails: UserDetails = adminService.loadUserByUsername(authentication.getPrincipal.asInstanceOf[Admin].getUsername)
    val authenticatedUser: Admin = Admin(userDetails.getUsername, userDetails.getPassword, userDetails.getAuthorities.asScala.toList)
    val userAuthentication: AdminAuthentication = new AdminAuthentication(authenticatedUser)
    tokenAuthenticationService.addAuthentication(response, userAuthentication)
    SecurityContextHolder.getContext.setAuthentication(authentication)
  }
}
