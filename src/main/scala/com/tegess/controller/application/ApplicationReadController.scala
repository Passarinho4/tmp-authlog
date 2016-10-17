package com.tegess.controller.application

import com.tegess.config.newsecurity.TokenService
import com.tegess.controller.application.ApplicationReadController.{ApplicationData, MinuteStatistics}
import com.tegess.domain.admin.Admin
import com.tegess.persistance.service.application.ApplicationService
import com.tegess.persistance.service.event.LoginEventService
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation
import org.springframework.web.bind.annotation.{PathVariable, RequestMapping, RequestMethod}

import scala.util.{Failure, Success, Try}

@annotation.RestController
@Component
class ApplicationReadController {

  @Autowired var applicationService: ApplicationService = _
  @Autowired var loginEventService: LoginEventService = _

  @RequestMapping(value=Array("/api/applications"), method=Array(RequestMethod.GET))
  def getApplications(authentication: Authentication): List[ApplicationData] = {
    val admin = authentication.getPrincipal.asInstanceOf[Admin]
    val applications = applicationService.findAll(admin)
    for {
      application <- applications
    } yield {
      ApplicationData(application.id.toHexString,
        application.secret,
        application.fbLogin.map(_.id),
        application.redirectURL,
        application.fbLogin.map(_.secret))
    }

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
  case class ApplicationData(appId: String,
                             secret: String,
                             facebookAppId: Option[String],
                             facebookRedirectURI: Option[String],
                             facebookSecret: Option[String])
  case class MinuteStatistics(time: Long, amount: Long)
}
