package com.tegess.controller.application

import com.tegess.controller.application.ApplicationReadController.ApplicationData
import com.tegess.persistance.service.application.ApplicationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod, RestController}

@RestController
@Component
class ApplicationReadController {

  @Autowired
  var applicationService: ApplicationService = _

  @RequestMapping(value=Array("/api/applications"), method=Array(RequestMethod.GET))
  def getApplications(): List[ApplicationData] = {
    val applications = applicationService.findAll()
    for{
      application <- applications
      fbLogin <- application.fbLogin
      redirectUrl <- application.redirectURL
    } yield ApplicationData(application.id.toHexString, fbLogin.id, redirectUrl, fbLogin.secret)
  }


}
object ApplicationReadController {
  case class ApplicationData(appId: String, facebookAppId: String, facebookRedirectURI: String, secret: String)
}
