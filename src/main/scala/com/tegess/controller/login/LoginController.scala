package com.tegess.controller.login

import java.util
import java.util.Locale

import com.avsystem.commons._
import com.tegess.controller._
import com.tegess.controller.login.FacebookHelper.{FacebookApplicationTokenResponse, FacebookTokenResponse, FacebookUserData, FacebookValidateTokenResponse}
import com.tegess.controller.login.LoginController._
import com.tegess.domain.user.{FacebookLogin, User}
import com.tegess.persistance.service.application.ApplicationService
import com.tegess.persistance.service.event.LoginEventService
import com.tegess.persistance.service.user.UserService
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.util.Base64Utils
import org.springframework.web.bind
import org.springframework.web.bind.annotation._
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.view.RedirectView

import scala.util.{Failure, Success, Try}

@bind.annotation.RestController
@Component
class LoginController {

  @Autowired var applicationService: ApplicationService = _
  @Autowired var userService: UserService = _
  @Autowired var loginEventService: LoginEventService = _
  @Autowired var restTemplate: RestTemplate = _
  @Autowired var passwordEncoder:PasswordEncoder = _

  @RequestMapping(value = Array("api/login/credentials"), method = Array(RequestMethod.GET))
  def loginByCredentials(@RequestParam appId: String, @RequestHeader(value = "Authorization") credentials: String): TokenResponse = {
    val result = for {
      loginPassword <- decodeUserFromCredentials(credentials)
      applicationId <- Try(new ObjectId(appId))
      user <- Try(userService.findOne(applicationId, loginPassword.login).get)
      password <- Try(user.password.get)
      if passwordEncoder.matches(loginPassword.password, password)
      application <- Try(applicationService.findOne(applicationId).get)
      token = TokenGenerator.generateJWTTokenForUser(application, user)
    } yield {
      loginEventService.publishLoginEvent(application, user)
      TokenResponse(token)
    }
    result match {
      case Success(token) => token
      case Failure(e) => throw new IllegalArgumentException("Wrong parameters - ", e)
    }
  }

  @RequestMapping(value=Array("api/login/facebook"), method=Array(RequestMethod.GET))
  def loginByFacebook(@RequestParam appId: String): RedirectView = {
    val response = for {
      applicationId <- Try(new ObjectId(appId))
      application <- Try(applicationService.findOne(applicationId).get)
      fbLogin <- Try(application.fbLogin.get)
    } yield new RedirectView().setup { _.setUrl(FacebookHelper.prepareCodeRequest(applicationId, fbLogin)) }

    response match {
      case Success(r) => r
      case Failure(e) => throw new IllegalArgumentException("Something went wrong - ", e)
    }
  }

  @RequestMapping(value=Array("api/logged/facebook"), method=Array(RequestMethod.GET))
  def loggedByFacebook(@RequestParam appId: String, @RequestParam code: String) = {

    val result = for {
      applicationId <- Try(new ObjectId(appId))
      application <- applicationService.findOne(applicationId).toTry
      fbLogin <- application.fbLogin.toTry
      tokenResponse <- Try(restTemplate.getForObject(
        FacebookHelper.prepareTokenRequest(applicationId, fbLogin, code), classOf[FacebookTokenResponse]))
      applicationTokenResponse <- Try(restTemplate.getForObject(
        FacebookHelper.prepareApplicationTokenRequest(fbLogin), classOf[FacebookApplicationTokenResponse]))
      validateTokenResponse <- Try(restTemplate.getForObject(
        FacebookHelper.prepareValidateTokenRequest(tokenResponse, applicationTokenResponse), classOf[FacebookValidateTokenResponse]))
      if fbLogin.id == validateTokenResponse.data.app_id
      userData <- Try(restTemplate.exchange(
        FacebookHelper.prepareUserInfoRequest(validateTokenResponse.data.user_id),
        HttpMethod.GET,
        FacebookHelper.prepareHeadersForFacebookAuthorization(tokenResponse),
        classOf[FacebookUserData]).getBody)
      user <- Try(User(userData.name,
        applicationId, List(FacebookLogin), None, Some(userData.email),
        Some(userData.picture.data.url), Some(userData.gender), Some(new Locale(userData.locale)), List()))
      token <- Try(TokenGenerator.generateJWTTokenForUser(application, user))
      url <- Try(application.redirectURL.get + s"?token=$token")
    } yield {
      userService.save(user)
      loginEventService.publishLoginEvent(application, user)
      new RedirectView().setup(_.setUrl(url))
    }

    result match {
      case Success(r) => r
      case Failure(e) => throw new IllegalArgumentException("Something went wrong -", e)
    }
  }

}
object LoginController {

  case class LoginPassword(login: String, password: String)
  case class TokenResponse(token: String)

  def decodeUserFromCredentials(credentials: String): Try[LoginPassword] = {
    for {
      decodedCredentials <- Try(new String(Base64Utils.decodeFromString(credentials)))
      slittedCredentials = decodedCredentials.split(":")
      login <- Try(slittedCredentials(0))
      password <- Try(slittedCredentials(1))
    } yield LoginPassword(login, password)
  }

}