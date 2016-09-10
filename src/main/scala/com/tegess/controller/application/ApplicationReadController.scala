package com.tegess.controller.application

import com.tegess.controller.application.ApplicationReadController.{ApplicationData, MinuteStatistics}
import com.tegess.persistance.service.application.ApplicationService
import com.tegess.persistance.service.event.LoginEventService
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.{PathVariable, RequestMapping, RequestMethod, RestController}

import scala.util.{Failure, Success, Try}

@RestController
@Component
class ApplicationReadController {

  @Autowired var applicationService: ApplicationService = _
  @Autowired var loginEventService: LoginEventService = _

  @RequestMapping(value=Array("/api/applications"), method=Array(RequestMethod.GET))
  def getApplications(): List[ApplicationData] = {
    val applications = applicationService.findAll()
    for{
      application <- applications
      fbLogin <- application.fbLogin
      redirectUrl <- application.redirectURL
    } yield ApplicationData(application.id.toHexString, fbLogin.id, redirectUrl, fbLogin.secret)
  }

  @RequestMapping(value=Array("/api/applications/{appId}/hourLoginStats"), method=Array(RequestMethod.GET))
  def getHourLoginStats(@PathVariable appId: String): List[MinuteStatistics] = {
    val result = for {
      applicationId <- Try(new ObjectId(appId))
    } yield loginEventService.getHourLoginStats(applicationId).map(pair => MinuteStatistics(pair._1, pair._2))

    result match {
      case Success(r) => r.toList
      case Failure(e) => throw new IllegalArgumentException("Something went wrong -", e)
    }
  }

}
object ApplicationReadController {
  case class ApplicationData(appId: String, facebookAppId: String, facebookRedirectURI: String, secret: String)
  case class MinuteStatistics(time: Long, amount: Long)
}
