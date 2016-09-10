package com.tegess.domain.application

import org.bson.types.ObjectId

case class Application(id: ObjectId,
                       name: String,
                       admin: String,
                       fbLogin: Option[FbLoginData],
                       credentialsLogin: Boolean,
                       redirectURL: Option[String])

case class FbLoginData(id:String, secret:String)

object Application {
  def apply(name: String,
            admin: String,
            fbLogin: Option[FbLoginData],
            credentialsLogin: Boolean,
            redirectURL: Option[String]): Application =
    new Application(new ObjectId(), name, admin, fbLogin, credentialsLogin, redirectURL)

  def apply(name: String,
            admin: String): Application =
    new Application(new ObjectId(), name, admin, None, false, None)
}
