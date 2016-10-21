package com.tegess.controller.test

import com.tegess.controller.test.InstallController.AuthlogBasicInfo
import com.tegess.domain.application.Application
import com.tegess.domain.user.{CredentialsLogin, User}
import com.tegess.persistance.service.application.ApplicationService
import com.tegess.persistance.service.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod, RestController}

@RestController
@Component
class InstallController {

  @Autowired
  var applicationService: ApplicationService = _
  @Autowired
  var userService: UserService = _


  @RequestMapping(value = Array("api/install"), method = Array(RequestMethod.GET))
  def install = {

    if(applicationService.findAll().nonEmpty) {
      "Your DB contains some applications. Please drop DB and try again"
    } else {
      val authlog = Application("authlog", "admin", None, credentialsLogin = true, None)
      val user = User("admin", authlog.id, List(CredentialsLogin),
        Some("authlog"), None, None, None, None, List("superAdmin"))

      applicationService.save(authlog)
      userService.save(user)

      AuthlogBasicInfo(authlog.id.toHexString, user.username, user.password.get)
    }


  }
}

object InstallController {
  case class AuthlogBasicInfo(id: String, adminLogin: String, adminPassword: String)
}
