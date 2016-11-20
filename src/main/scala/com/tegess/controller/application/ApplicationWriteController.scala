package com.tegess.controller.application

import com.tegess.controller._
import com.tegess.controller.application.ApplicationReadController.ApplicationData
import com.tegess.domain.admin.Admin
import com.tegess.domain.application.{Application, FbLoginData}
import com.tegess.persistance.service.application.ApplicationService
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation._

import scala.util.{Failure, Success, Try}

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

  //TODO Please secure this endpoint
  @RequestMapping(value = Array("/api/applications/{id}"), method=Array(RequestMethod.PUT))
  def updateApplication(@RequestBody applicationData: ApplicationData,
                        @PathVariable id: String,
                        authentication: Authentication) = {
    val fbLoginData = for {
      fbSecret <- applicationData.facebookSecret
      fbAppId <- applicationData.facebookAppId
    } yield FbLoginData(fbAppId, fbSecret)

    val admin = authentication.getPrincipal.asInstanceOf[Admin]

    val result = for{
      appId <- Try(new ObjectId(id))
      application <- applicationService.findOne(appId).toTry
      updatedApp = Application(appId, applicationData.name, application.admin,
        application.secret, fbLoginData, false, applicationData.facebookRedirectURI)
    } yield applicationService.save(updatedApp)

    result match {
      case Success(e) =>
      case Failure(e) => throw e
    }
  }

  @RequestMapping(value=Array("/api/applications/{id}"), method=Array(RequestMethod.DELETE))
  def deleteApplication(@PathVariable id: String) = {
    val appId = new ObjectId(id)
    applicationService.remove(appId)
  }
}
