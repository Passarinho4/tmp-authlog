package com.tegess.controller.login

import com.tegess.domain.application.Application
import com.tegess.domain.user.User
import com.avsystem.commons.jiop.JavaInterop._
import com.tegess.config.newsecurity.TokenService
import io.jsonwebtoken.{JwtBuilder, Jwts, SignatureAlgorithm}
import org.springframework.security.crypto.codec.Base64

object TokenGenerator {

  def generateJWTTokenForUser(application: Application, user: User): String = {
    val claims = JHashMap[String, Object]()

    claims.put(TokenService.USERNAME_KEY, user.username)
    claims.put("privileges", user.privileges.asJava)

    val jwtBuilder: JwtBuilder = Jwts.builder
      .setSubject(user.username)
      .setClaims(claims)
      .setHeaderParam(TokenService.TYP_KEY, TokenService.JWT)
      .setHeaderParam(TokenService.APPLICATION_KEY, application.id.toHexString)
      .signWith(SignatureAlgorithm.HS256, Base64.encode(application.secret.getBytes))

    jwtBuilder.compact
  }
}
