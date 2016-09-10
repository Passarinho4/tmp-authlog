package com.tegess.persistance.repository.event

import com.avsystem.commons.mongo.{BsonCodec, Doc, Filter}
import com.mongodb.client.MongoCollection
import com.tegess.domain.event.LoginEvent
import org.bson.types.ObjectId
import org.bson.{BsonDateTime, BsonDocument}
import org.joda.time.DateTime

class LoginEventRepository(collection: MongoCollection[LoginEvent]) {

  import LoginEventRepository._

  def save(event: LoginEvent) = collection.insertOne(event)
  def count(applicationId: ObjectId, from: DateTime, to: DateTime): Long = {
    collection.count(
      Filter.and(
        Filter.eq(applicationIdKey, applicationId),
        Filter.gte(timeStampKey, from),
        Filter.lt(timeStampKey, to)))
  }

}
object LoginEventRepository {

  val dateTimeCodec = BsonCodec.create[DateTime, BsonDateTime](
    doc => new DateTime(doc.getValue),
    obj => new BsonDateTime(obj.getMillis))

  private val idKey = BsonCodec.objectId.key("_id")
  private val applicationIdKey = BsonCodec.objectId.key("applicationId")
  private val usernameKey = BsonCodec.string.key("username")
  private val timeStampKey = dateTimeCodec.key("timeStamp")

  val codec = BsonCodec.create[LoginEvent, BsonDocument](
    document => {
      val doc = new Doc(document)
      LoginEvent(
        doc.require(idKey),
        doc.require(applicationIdKey),
        doc.require(usernameKey),
        doc.require(timeStampKey)
      )
    },
    event => Doc()
      .put(idKey, event.id)
        .put(applicationIdKey, event.applicationId)
        .put(usernameKey, event.username)
      .put(timeStampKey, event.timeStamp).toBson
  )

}
