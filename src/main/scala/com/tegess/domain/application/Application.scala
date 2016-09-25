package com.tegess.domain.application

import java.util.UUID

import org.bson.types.ObjectId

case class Application(id: ObjectId,
                       name: String,
                       admin: String,
                       secret: String,
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
    new Application(new ObjectId(),
      name,
      admin,
      UUID.randomUUID().toString,
      fbLogin,
      credentialsLogin,
      redirectURL)

}
