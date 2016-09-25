package com.tegess.domain.admin

import java.util

import com.avsystem.commons.jiop.JavaInterop._
import com.tegess.domain.user.User
import org.bson.types.ObjectId
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class Admin(username:String,
            password: String,
            grantedAuthority: List[GrantedAuthority],
            enabled:Boolean,
            accountNonExpired: Boolean,
            credentialsNonExpired: Boolean,
            accountNonLocked: Boolean) extends UserDetails {
  require(ObjectId.isValid(username.take(24)) && username.length>25)

  import Admin._
  override def isEnabled: Boolean = enabled

  override def getPassword: String = password

  override def isAccountNonExpired: Boolean = accountNonExpired

  override def isCredentialsNonExpired: Boolean = credentialsNonExpired

  override def getAuthorities: util.Collection[_ <: GrantedAuthority] = grantedAuthority.asJava

  override def isAccountNonLocked: Boolean = accountNonLocked

  override def getUsername: String = username

  def getApplicationId = getApplicationIdFromUsername(username)

  def getRealUsername = getRealUsernameFromUsername(username)

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

  def apply(user: User) = {
    new Admin(
      user.application.toHexString + user.username,
      user.password.get, //TODO for with exception
      List(),
      enabled = true,
      accountNonExpired = true,
      credentialsNonExpired = true,
      accountNonLocked = true
    )
  }

  def getApplicationIdFromUsername(username: String):ObjectId = new ObjectId(username.take(24))

  def getRealUsernameFromUsername(username: String) = username.substring(24)

}