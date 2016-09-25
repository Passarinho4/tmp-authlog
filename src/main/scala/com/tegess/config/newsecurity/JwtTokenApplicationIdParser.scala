package com.tegess.config.newsecurity

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import io.jsonwebtoken.impl.TextCodec
import org.bson.types.ObjectId
import com.avsystem.commons._

import scala.util.Try

object JwtTokenApplicationIdParser {

  val mapper = new ObjectMapper().setup { _.registerModule(DefaultScalaModule) }

  def parse(token: String): Try[ObjectId] = Try {
    val headerPart = token.split("\\.")(0)
    val origValue: String = TextCodec.BASE64URL.decodeToString(headerPart)
    val m: Map[String, AnyRef] = mapper.readValue(origValue, classOf[Map[String, AnyRef]])
    new ObjectId(m.get(TokenService.APPLICATION_KEY).get.asInstanceOf[String])
  }

}
