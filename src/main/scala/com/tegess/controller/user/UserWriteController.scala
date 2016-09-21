package com.tegess.controller.user

import com.tegess.controller.user.UserWriteController.NewUserRequest
import com.tegess.domain.user.{CredentialsLogin, FacebookLogin, User}
import com.tegess.persistance.service.user.UserService
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation._

@RestController
@Component
class UserWriteController {

  @Autowired
  var userService: UserService = _

  @RequestMapping(value=Array("/api/applications/{id}/users"), method=Array(RequestMethod.POST))
  def createUser(@RequestBody newUser: NewUserRequest, @PathVariable id: String) = {
    val appId = new ObjectId(id)
    val user = User(newUser.username,
      appId,
      List(FacebookLogin, CredentialsLogin),
      Option(newUser.password),
      Option(newUser.mail),
      newUser.picture,
      None,
      None,
      List())
    userService.save(user)
  }

  @RequestMapping(value=Array("/api/applications/{id}/users/{userId}"), method=Array(RequestMethod.DELETE))
  def deleteUser(@PathVariable id: String, @PathVariable userId: String) = {
    val appId = new ObjectId(id)
    userService.remove(appId, userId)
  }

  @RequestMapping(value=Array("/api/applications/{id}/users/{userId}/privileges"), method=Array(RequestMethod.POST))
  def addPrivilege(@PathVariable id: String, @PathVariable userId: String, @RequestBody privileges: List[String]) = {
    val appId = new ObjectId(id)
    val user = userService.findOne(appId, userId).get
    val updatedUser = user.copy(privileges = user.privileges ::: privileges)
    userService.save(updatedUser)
  }

  @RequestMapping(value=Array("/api/applications/{id}/users/{userId}/privileges/{privilege}"), method=Array(RequestMethod.DELETE))
  def deletePrivilege(@PathVariable id: String, @PathVariable userId: String, @PathVariable privilege: String) = {
    val appId = new ObjectId(id)
    val user = userService.findOne(appId, userId).get
    val updatedUser = user.copy(privileges = user.privileges.filter(_ != privilege))
    userService.save(updatedUser)
  }

}
object UserWriteController {
  case class NewUserRequest(username: String, password: String, mail: String, picture: Option[String])
}