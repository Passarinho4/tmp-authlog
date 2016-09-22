package com.tegess.persistance.repository.user

import com.avsystem.commons.mongo.{BsonCodec, Doc, Filter}
import com.mongodb.client.MongoCollection
import com.tegess.domain.user.UserPhoto
import org.bson.{BsonDocument, BsonString}
import org.springframework.util.MimeType

class UserPhotoRepository(collection: MongoCollection[UserPhoto]) {
  import UserPhotoRepository._

  def save(userPhoto: UserPhoto) = collection.insertOne(userPhoto)
  def findOne(filename: String) = Option(collection.find(Filter.eq(filenameKey, filename)).first())
  def remove(filename: String) = collection.deleteOne(Filter.eq(filenameKey, filename))
}
object UserPhotoRepository {

  private val mimeTypeCodec = BsonCodec.create[MimeType, BsonString](
    document => MimeType.valueOf(document.getValue),
    obj => new BsonString(obj.toString))

  private val filenameKey = BsonCodec.string.key("filename")
  private val photoKey = BsonCodec.byteArray.key("photo")
  private val mimeTypeKey = mimeTypeCodec.key("mimeType")

  val codec = BsonCodec.create[UserPhoto, BsonDocument](
    document => {
      val doc = new Doc(document)
      UserPhoto(doc.require(filenameKey), doc.require(photoKey), doc.require(mimeTypeKey))
    },
    obj => Doc()
      .put(filenameKey, obj.filename)
      .put(photoKey, obj.photo)
      .put(mimeTypeKey, obj.mimeType).toBson)
}