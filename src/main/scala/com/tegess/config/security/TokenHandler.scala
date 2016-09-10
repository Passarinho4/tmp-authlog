package com.tegess.config.security

import com.avsystem.commons.jiop.JavaInterop._
import com.tegess.domain.admin.Admin
import com.tegess.persistance.service.admin.AdminService
import io.jsonwebtoken.{Claims, JwtBuilder, Jwts, SignatureAlgorithm}
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.codec.Base64

class TokenHandler(val secret: String, val adminService: AdminService) {
  def parseUserFromToken(token: String): Admin = {
    val claims: Claims = Jwts.parser.setSigningKey(Base64.encode(secret.getBytes)).parseClaimsJws(token).getBody
    val userDetails: UserDetails = adminService.loadUserByUsername(claims.get("username", classOf[String]))
    val admin: Admin = Admin(userDetails.getUsername, userDetails.getPassword, userDetails.getAuthorities.asScala.toList)
    admin
  }

  def createTokenForAdmin(username: String, authorities: List[_ <: GrantedAuthority]): String = {
    val jwtBuilder: JwtBuilder = Jwts.builder.setSubject(username).signWith(SignatureAlgorithm.HS256, Base64.encode(secret.getBytes)).setHeaderParam("typ", "JWT")
    val authoritiesAsString: List[String] = authorities.map(_.getAuthority)
    val hashMap: JHashMap[String, AnyRef] = new JHashMap[String, AnyRef]
    hashMap.put("username", username)
    hashMap.put("authorities", authoritiesAsString)
    jwtBuilder.setClaims(hashMap)
    jwtBuilder.compact
  }
}
