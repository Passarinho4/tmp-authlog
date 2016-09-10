package com.tegess.domain.user

import scala.util.Try

sealed trait LoginType {
  val name:String
}

object CredentialsLogin extends LoginType {
  override val name = "CredentialsLogin"
}
object FacebookLogin extends LoginType {
  override val name = "FacebookLogin"
}

object LoginType {
  def forName(name: String): Try[LoginType] = name match {
    case CredentialsLogin.name => Try(CredentialsLogin)
    case FacebookLogin.name => Try(FacebookLogin)
    case _ => Try(throw new IllegalArgumentException(s"Unsupported login type $name"))
  }
}