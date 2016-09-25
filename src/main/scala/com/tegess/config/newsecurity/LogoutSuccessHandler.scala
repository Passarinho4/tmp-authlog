package com.tegess.config.newsecurity

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import org.springframework.security.core.Authentication
import org.springframework.security.web.RedirectStrategy
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler

class LogoutSuccessHandler(val defaultTargetURL: String) extends SimpleUrlLogoutSuccessHandler {

  this.setRedirectStrategy(new RedirectStrategy {
    override def sendRedirect(request: HttpServletRequest, response: HttpServletResponse, url: String): Unit = {}
  })

  override def onLogoutSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
    super.onLogoutSuccess(request, response, authentication)
  }
}
