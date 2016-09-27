package com.tegess.controller.user

import com.tegess.controller._
import com.tegess.controller.user.UserReadController.UserData
import com.tegess.persistance.service.user.UserService
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.{PathVariable, RequestMapping, RequestMethod, RestController}

import scala.util.{Failure, Success, Try}


@RestController
@Component
class UserReadController {

  @Autowired var userService:UserService = _

  @RequestMapping(value=Array("/api/applications/{appId}/users"), method=Array(RequestMethod.GET))
  def getUsers(@PathVariable appId: String) = {
    val id = new ObjectId(appId)

    val users = userService.findAll(id)

    for {
      user <- users
    } yield UserData(user.username, user.username, user.mail, user.pictureUrl,
      user.gender, user.locale.map(_.toLanguageTag), user.privileges)

  }

  @PreAuthorize(value = "authentication.getName() == #appId.concat(#userId)")
  @RequestMapping(value=Array("/api/applications/{appId}/users/{userId}"), method=Array(RequestMethod.GET))
  def getUser(@PathVariable appId: String, @PathVariable userId: String) = {
    val userTry = for {
      applicationId <- Try(new ObjectId(appId))
      user <- userService.findOne(applicationId, userId).toTry
    } yield UserData(user.username, user.username, user.mail, user.pictureUrl,
      user.gender, user.locale.map(_.toLanguageTag), user.privileges)

    userTry match {
      case Success(u) => u
      case Failure(e) => throw new IllegalArgumentException("Something went wrong", e)
    }

  }

}
object UserReadController {
  case class UserData(id: String,
                      username: String,
                      mail: Option[String],
                      pictureURL: Option[String],
                      gender: Option[String],
                      locale: Option[String],
                      privileges: List[String])
}