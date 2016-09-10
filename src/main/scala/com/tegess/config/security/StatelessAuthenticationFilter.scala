package com.tegess.config.security

import javax.servlet.http.HttpServletRequest
import javax.servlet.{FilterChain, ServletRequest, ServletResponse}

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean

@Component class StatelessAuthenticationFilter extends GenericFilterBean {
  @Autowired private val authenticationService: TokenAuthenticationService = null

  def doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
    val httpRequest: HttpServletRequest = request.asInstanceOf[HttpServletRequest]
    val authentication: Authentication = authenticationService.getAuthentication(httpRequest)
    SecurityContextHolder.getContext.setAuthentication(authentication)
    chain.doFilter(request, response)
    SecurityContextHolder.getContext.setAuthentication(null)
  }
}
