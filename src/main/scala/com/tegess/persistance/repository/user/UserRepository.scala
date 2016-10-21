package com.tegess.persistance.repository.user

import java.util
import java.util.Locale

import com.avsystem.commons.mongo.{BsonCodec, Doc, DocKey, Filter}
import com.mongodb.client.MongoCollection
import com.tegess.domain.user.{LoginType, User}
import org.bson.{BsonArray, BsonDocument}
import org.bson.types.ObjectId
import com.avsystem.commons.jiop.JavaInterop._
import com.mongodb.client.model.UpdateOptions

class UserRepository (collection: MongoCollection[User]) {

  import UserRepository._
  def save(user: User) = collection.replaceOne(
    Filter.and(
      Filter.eq(usernameKey, user.username),
      Filter.eq(applicationKey, user.application)),
    user, new UpdateOptions().upsert(true))
  def findOne(application: ObjectId, username: String) = collection.find(
    Filter.and(Filter.eq(applicationKey, application), Filter.eq(usernameKey, username)))
    .first()
  def findAll(application: ObjectId) = collection.find(Filter.eq(applicationKey, application)).into(new util.ArrayList[User]()).asScala.toList
  def remove(application: ObjectId, username: String) = collection.deleteOne(
    Filter.and(Filter.eq(applicationKey, application), Filter.eq(usernameKey, username)))

}
object UserRepository {

  private val loginTypeKey = DocKey("loginType", BsonCodec.string)
  private val loginTypeCodec = BsonCodec.create[LoginType, BsonDocument](
    document => {
      val doc = new Doc(document)
      LoginType.forName(doc.require(loginTypeKey)).get
    },
    loginType => Doc().put(loginTypeKey, loginType.name).toBson
  )

  private val languageTagKey = DocKey("languageTag", BsonCodec.string)
  private val localeCodec = BsonCodec.create[Locale, BsonDocument](
    document => {
      val doc = new Doc(document)
      Locale.forLanguageTag(doc.require(languageTagKey))
    },
    locale => Doc().put(languageTagKey, locale.toLanguageTag).toBson
  )

  private val usernameKey = DocKey("username", BsonCodec.string)
  private val applicationKey = DocKey("application", BsonCodec.objectId)
  private val loginTypesKey: DocKey[List[LoginType], BsonArray] = DocKey("loginTypes", loginTypeCodec.collection)
  private val passwordKey = DocKey("password", BsonCodec.string)
  private val mailKey = DocKey("mail", BsonCodec.string)
  private val pictureUrlKey = DocKey("pictureURL", BsonCodec.string)
  private val genderKey = DocKey("gender", BsonCodec.string)
  private val localeKey = DocKey("locale", localeCodec)
  private val privilegesKey: DocKey[List[String], BsonArray] = DocKey("privileges", BsonCodec.string.collection)

  val codec = BsonCodec.create[User, BsonDocument](
    document => {
      val doc = new Doc(document)
      new User(doc.require(usernameKey),
        doc.require(applicationKey),
        doc.require(loginTypesKey),
        doc.get(passwordKey),
        doc.get(mailKey),
        doc.get(pictureUrlKey),
        doc.get(genderKey),
        doc.get(localeKey),
        doc.require(privilegesKey))
    },
    user => Doc()
      .put(usernameKey, user.username)
      .put(applicationKey, user.application)
      .put(loginTypesKey, user.loginTypes)
      .putOpt(passwordKey, user.password)
      .putOpt(mailKey, user.mail)
      .putOpt(pictureUrlKey, user.pictureUrl)
      .putOpt(genderKey, user.gender)
      .putOpt(localeKey, user.locale)
      .put(privilegesKey, user.privileges).toBson

  )

}
