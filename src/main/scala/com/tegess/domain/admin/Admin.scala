package com.tegess.domain.admin

import java.util

import com.avsystem.commons.jiop.JavaInterop._
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class Admin(username:String,
            password: String,
            grantedAuthority: List[GrantedAuthority],
            enabled:Boolean,
            accountNonExpired: Boolean,
            credentialsNonExpired: Boolean,
            accountNonLocked: Boolean) extends UserDetails {

  override def isEnabled: Boolean = enabled

  override def getPassword: String = password

  override def isAccountNonExpired: Boolean = accountNonExpired

  override def isCredentialsNonExpired: Boolean = credentialsNonExpired

  override def getAuthorities: util.Collection[_ <: GrantedAuthority] = grantedAuthority.asJava

  override def isAccountNonLocked: Boolean = accountNonLocked

  override def getUsername: String = username

}
object Admin {

  def apply(username: String,
            password: String,
            grantedAuthority: List[GrantedAuthority],
            enabled: Boolean,
            accountNonExpired: Boolean,
            credentialsNonExpired: Boolean,
            accountNonLocked: Boolean): Admin =
    new Admin(username, password, grantedAuthority, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked)

  def apply(username: String, password: String, grantedAuthority: List[GrantedAuthority]): Admin =
    new Admin(username, password, grantedAuthority, enabled = true,
      accountNonExpired = true, credentialsNonExpired = true, accountNonLocked = true)

}