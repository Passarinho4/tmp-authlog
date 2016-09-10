package com.tegess.controller.login

import java.net.URLEncoder

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.tegess.domain.application.FbLoginData
import org.bson.types.ObjectId
import org.springframework.http.{HttpEntity, HttpHeaders}
import org.springframework.util.MultiValueMap

object FacebookHelper {
  def prepareCodeRequest(appId: ObjectId, fbLogin: FbLoginData): String = {
    s"https://facebook.com/dialog/oauth?client_id=${fbLogin.id}" +
      s"&redirect_uri=http://51.255.48.55:8085/api/logged/facebook?appId=$appId&scope=public_profile,email"
  }

  def prepareTokenRequest(appId: ObjectId, fbLogin: FbLoginData, code: String): String = {
    s"https://graph.facebook.com/v2.6/oauth/access_token?client_id=${fbLogin.id}" +
      s"&redirect_uri=http://51.255.48.55:8085/api/logged/facebook?appId=$appId" +
      s"&client_secret=${fbLogin.secret}&code=$code"
  }

  def prepareApplicationTokenRequest(fbLoginData: FbLoginData): String = {
    s"https://graph.facebook.com/v2.6/oauth/access_token?client_id=${fbLoginData.id}" +
      s"&client_secret=${fbLoginData.secret}&grant_type=client_credentials"
  }

  def prepareValidateTokenRequest(tokenResponse: FacebookTokenResponse, applicationTokenResponse: FacebookApplicationTokenResponse) = {
    s"https://graph.facebook.com/debug_token?input_token=${tokenResponse.access_token}" +
      s"&access_token=${URLEncoder.encode(applicationTokenResponse.access_token, "UTF-8")}"
  }

  def prepareUserInfoRequest(userId: String) = {
    s"https://graph.facebook.com/v2.6/$userId?fields=id,name,email,gender,locale,picture"
  }

  def prepareHeadersForFacebookAuthorization(tokenResponse: FacebookTokenResponse): HttpEntity[AnyVal] = {
    val headers:MultiValueMap[String, String] = new HttpHeaders()
    headers.add("Authorization", s"Bearer ${tokenResponse.access_token}")
    new HttpEntity[AnyVal](headers)
  }


  case class FacebookTokenResponse(access_token: String, token_type: String, expires_in: Int)
  case class FacebookApplicationTokenResponse(access_token: String, token_type: String)

  @JsonIgnoreProperties(ignoreUnknown = true)
  case class FacebookValidateTokenDateResponse(app_id: String, user_id: String)

  @JsonIgnoreProperties(ignoreUnknown = true)
  case class FacebookValidateTokenResponse(data: FacebookValidateTokenDateResponse)

  case class FacebookUserData(id: String, name: String, email: String, gender: String, locale: String, picture: FacebookPicture)
  case class FacebookPicture(data: FacebookPictureData)
  case class FacebookPictureData(is_silhouette: Boolean, url: String)

}
