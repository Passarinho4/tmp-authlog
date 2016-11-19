package com.tegess.controller.application

import com.tegess.controller.application.ApplicationReadController.ApplicationData
import com.tegess.domain.admin.Admin
import com.tegess.domain.application.{Application, FbLoginData}
import com.tegess.persistance.service.application.ApplicationService
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation._

import scala.Option

@RestController
@Component
class ApplicationWriteController {

  @Autowired
  var applicationService: ApplicationService = _

  @RequestMapping(value=Array("/api/applications"), method=Array(RequestMethod.POST))
  def createApplication(@RequestBody applicationData: ApplicationData, authentication: Authentication) = {
    val admin = authentication.getPrincipal.asInstanceOf[Admin]

    val fbLoginData = for {
      fbSecret <- applicationData.facebookSecret
      fbAppId <- applicationData.facebookAppId
    } yield FbLoginData(fbAppId, fbSecret)

    if(applicationData.name == null) {
      throw new IllegalArgumentException("Application name can't be null")
    }

    val app = Application(applicationData.name,
      admin.getRealUsername,
      fbLoginData,
      credentialsLogin = false,
      applicationData.facebookRedirectURI)

    applicationService.save(app)
  }

  @RequestMapping(value=Array("/api/applications/{id}"), method=Array(RequestMethod.DELETE))
  def deleteApplication(@PathVariable id: String) = {
    val appId = new ObjectId(id)
    applicationService.remove(appId)
  }
}
