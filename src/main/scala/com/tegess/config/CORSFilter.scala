package com.tegess.config

import javax.servlet.FilterChain
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.web.filter.OncePerRequestFilter

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE) class CORSFilter extends OncePerRequestFilter {

  override def doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
    response.addHeader("Access-Control-Allow-Origin", "*")
    if (request.getHeader("Access-Control-Request-Method") != null && "OPTIONS" == request.getMethod) {
      response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE")
      response.addHeader("Access-Control-Allow-Headers", "Authorization, accept")
      response.addHeader("Access-Control-Allow-Headers", "Content-Type, X-Requested-With, X-AUTH-TOKEN")
      response.addHeader("Access-Control-Max-Age", "1500")
    }
    if (!("OPTIONS" == request.getMethod)) {
      filterChain.doFilter(request, response)
    }
  }
}
