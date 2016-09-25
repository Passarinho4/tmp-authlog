package com.tegess.persistance.repository.application

import com.avsystem.commons.mongo.{BsonCodec, Doc, DocKey, Filter}
import com.mongodb.client.MongoCollection
import com.tegess.domain.application.{Application, FbLoginData}
import org.bson.BsonDocument
import org.bson.types.ObjectId
import com.avsystem.commons.jiop.JavaInterop._

private[persistance] class ApplicationRepository(collection: MongoCollection[Application]) {

  import ApplicationRepository._

  def save(application: Application) = collection.insertOne(application)
  def findOne(id: ObjectId) = collection.find(Filter.eq(idKey, id)).first()
  def findAll(username: String) = collection.find(Filter.eq(adminKey, username)).into[JList[Application]](new JArrayList).asScala.toList
  def findAll() = collection.find().into[JList[Application]](new JArrayList).asScala.toList
  def remove(id: ObjectId) = collection.deleteOne(Filter.eq(idKey, id))

}
object ApplicationRepository {

  private val fbDataIdKey = DocKey("id", BsonCodec.string)
  private val fbDataSecretKey = DocKey("secret", BsonCodec.string)

  private val fbLoginDataCodec = BsonCodec.create[FbLoginData, BsonDocument](
    document => {
      val doc = new Doc(document)
      FbLoginData(doc.require(fbDataIdKey), doc.require(fbDataSecretKey))
    },
    data => Doc()
      .put(fbDataIdKey, data.id)
      .put(fbDataSecretKey, data.secret)
      .toBson)

  private val idKey = DocKey("_id", BsonCodec.objectId)
  private val nameKey = DocKey("name", BsonCodec.string)
  private val adminKey = DocKey("admin", BsonCodec.string)
  private val secretKey = DocKey("secret", BsonCodec.string)
  private val fbLoginKey = DocKey("fbLogin", fbLoginDataCodec)
  private val credentialsLoginKey = DocKey("credentialsLogin", BsonCodec.boolean)
  private val redirectURLKey = DocKey("redirectURL", BsonCodec.string)

  val codec = BsonCodec.create[Application, BsonDocument](
    document => {
      val doc = new Doc(document)
      Application(doc.require(idKey),
        doc.require(nameKey),
        doc.require(adminKey),
        doc.require(secretKey),
        doc.get(fbLoginKey),
        doc.require(credentialsLoginKey),
        doc.get(redirectURLKey)
      )
    },
    app => Doc()
      .put(idKey, app.id)
      .put(nameKey, app.name)
      .put(adminKey, app.admin)
      .put(secretKey, app.secret)
      .putOpt(fbLoginKey, app.fbLogin)
      .put(credentialsLoginKey, app.credentialsLogin)
      .putOpt(redirectURLKey, app.redirectURL)
      .toBson)
}
