package com.tegess.controller.user

import java.util.UUID
import javax.servlet.http.HttpServletRequest

import com.tegess.controller._
import com.tegess.controller.user.UserWriteController.NewUserRequest
import com.tegess.domain.user.{CredentialsLogin, FacebookLogin, User, UserPhoto}
import com.tegess.persistance.service.user.UserService
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{MediaType, ResponseEntity}
import org.springframework.stereotype.Component
import org.springframework.util.MimeType
import org.springframework.web.bind.annotation._
import org.springframework.web.multipart.MultipartFile

import scala.util.{Failure, Success, Try}


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

  @RequestMapping(value=Array("/api/applications/{id}/users/{userId}/photo"), method=Array(RequestMethod.POST))
  def uploadAndSetUserPhoto(@RequestBody file: MultipartFile,
                            @PathVariable id: String,
                            @PathVariable userId: String,
                            request: HttpServletRequest) = {
    for {
      applicationId <- Try(new ObjectId(id))
      user <- userService.findOne(applicationId, userId).toTry
      filename = UUID.randomUUID().toString
      updatedUser = user.copy(pictureUrl = Some(request.getScheme + "://" + request.getServerName + ":" + request.getServerPort + request.getServletPath + "/" + filename))
      userPhoto = UserPhoto(filename, file.getBytes, MimeType.valueOf(file.getContentType))
    } {
      userService.save(updatedUser)
      userService.save(userPhoto)
    }

  }

  @RequestMapping(value=Array("/api/applications/{id}/users/{userId}/photo/{photoName}"), method=Array(RequestMethod.GET))
  def getUserPhoto(@PathVariable id: String,
                  @PathVariable userId: String,
                  @PathVariable photoName: String) = {
    val photo = for {
      applicationId <- Try(new ObjectId(id))
      user <- userService.findOne(applicationId, userId).toTry
    //TODO add application and user validation
      photo <- userService.findOne(photoName).toTry
    } yield photo

    photo match {
      case Success(p) => ResponseEntity.ok()
          .contentLength(p.photo.length)
          .contentType(MediaType.parseMediaType(p.mimeType.toString))
          .body(p.photo)
      case Failure(e) => throw e
    }

  }
}
object UserWriteController {
  case class NewUserRequest(username: String, password: String, mail: String, picture: Option[String])
}