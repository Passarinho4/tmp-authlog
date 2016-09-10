package com.tegess.config.security

import org.springframework.security.core.{Authentication, GrantedAuthority}
import com.avsystem.commons.jiop.JavaInterop._
import com.tegess.domain.admin.Admin

class AdminAuthentication(val admin: Admin) extends Authentication {
  private var authenticated: Boolean = true

  override def getAuthorities: JCollection[_ <: GrantedAuthority] = {
    admin.getAuthorities
  }

  def getCredentials: Object = {
    admin.getPassword
  }

  def getDetails: Object = {
    null
  }

  def getPrincipal: Object = {
    admin
  }

  def isAuthenticated: Boolean = {
    authenticated
  }

  def setAuthenticated(isAuthenticated: Boolean) {
    this.authenticated = isAuthenticated
  }

  def getName: String = {
    admin.getUsername
  }
}
