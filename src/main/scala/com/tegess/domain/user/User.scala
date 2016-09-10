package com.tegess.domain.user

import java.util.Locale

import org.bson.types.ObjectId

case class User(username: String,
                application: ObjectId,
                loginTypes: List[LoginType],
                password: Option[String],
                mail: Option[String],
                pictureUrl: Option[String],
                gender: Option[String],
                locale: Option[Locale],
                privileges: List[String])

object User {
  def createFromCredentials(application: ObjectId, username: String, password: String): User = {
    new User(username, application, List(CredentialsLogin), Some(password), None, None, None, None, List())
  }
  def createFromFacebook(application: ObjectId,
                         username: String,
                         mail: String,
                         gender: String,
                         locale: Locale,
                         pictureUrl: String): User = {
    new User(username, application, List(FacebookLogin), None, Some(mail), Some(pictureUrl), Some(gender), Some(locale), List())
  }
}
