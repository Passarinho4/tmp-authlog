package com.tegess.controller.user

import com.tegess.controller.user.UserReadController.UserData
import com.tegess.persistance.service.user.UserService
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.{PathVariable, RequestMapping, RequestMethod, RestController}

@RestController
@Component
class UserReadController {

  @Autowired
  var userService:UserService = _

  @RequestMapping(value=Array("/api/applications/{appId}/users"), method=Array(RequestMethod.GET))
  def getUsers(@PathVariable appId: String) = {
    val id = new ObjectId(appId)

    val users = userService.findAll(id)

    for {
      user <- users
    } yield UserData(user.username, user.username, user.mail, user.pictureUrl,
      user.gender, user.locale.map(_.toLanguageTag), user.privileges)

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