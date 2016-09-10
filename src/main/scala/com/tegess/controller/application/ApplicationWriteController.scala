package com.tegess.controller.application

import com.tegess.controller.application.ApplicationReadController.ApplicationData
import com.tegess.domain.application.{Application, FbLoginData}
import com.tegess.persistance.service.application.ApplicationService
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation._

@RestController
@Component
class ApplicationWriteController {

  @Autowired
  var applicationService: ApplicationService = _

  @RequestMapping(value=Array("/api/applications"), method=Array(RequestMethod.POST))
  def createApplication(@RequestBody applicationData: ApplicationData) = {
    val app = Application(
      "application",
      "admin",
      Some(FbLoginData(applicationData.facebookAppId, applicationData.secret)),
      credentialsLogin = false,
      Some(applicationData.facebookRedirectURI))

    applicationService.save(app)
  }

  @RequestMapping(value=Array("/api/applications/{id}"), method=Array(RequestMethod.DELETE))
  def deleteApplication(@PathVariable id: String) = {
    val appId = new ObjectId(id)
    applicationService.remove(appId)
  }
}
