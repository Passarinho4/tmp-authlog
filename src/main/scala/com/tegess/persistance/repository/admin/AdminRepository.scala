package com.tegess.persistance.repository.admin

import java.util

import com.avsystem.commons.mongo.{BsonCodec, Doc, DocKey, Filter}
import com.mongodb.client.MongoCollection
import com.tegess.domain.admin.Admin
import org.bson.{BsonArray, BsonDocument}
import org.springframework.security.core.authority.SimpleGrantedAuthority
import com.avsystem.commons.jiop.JavaInterop._

private[persistance] class AdminRepository(collection: MongoCollection[Admin]) {
  import AdminRepository._

  def save(admin:Admin) = collection.insertOne(admin)
  def findOne(username: String) = collection.find(Filter.eq(usernameKey, username)).first()
  def findAll(): List[Admin] = collection.find().into[JList[Admin]](new util.ArrayList[Admin]()).asScala.toList
  def remove(username: String) = collection.deleteOne(Filter.eq(usernameKey, username))

}
object AdminRepository {

  private val usernameKey = DocKey("username", BsonCodec.string)
  private val passwordKey = DocKey("password", BsonCodec.string)
  private val grantedAuthorityKey = DocKey[List[String], BsonArray]("grantedAuthority", BsonCodec.string.collection)
  private val enabledKey = DocKey("enabled", BsonCodec.boolean)
  private val accountNonExpiredKey = DocKey("accountNonExpired", BsonCodec.boolean)
  private val credentialsNonExpiredKey = DocKey("credentialsNonExpired", BsonCodec.boolean)
  private val accountNonLockedKey = DocKey("accountNonLocked", BsonCodec.boolean)

  import com.avsystem.commons.jiop.JavaInterop._

  val codec = BsonCodec.create[Admin, BsonDocument](
    document => {
      val doc = new Doc(document)
      Admin(
        doc.require(usernameKey),
        doc.require(passwordKey),
        doc.require(grantedAuthorityKey).map(new SimpleGrantedAuthority(_)),
        doc.require(enabledKey),
        doc.require(accountNonExpiredKey),
        doc.require(credentialsNonExpiredKey),
        doc.require(accountNonLockedKey)
      )
    },
    admin => Doc()
      .put(usernameKey, admin.getUsername)
      .put(passwordKey, admin.getPassword)
      .put(grantedAuthorityKey, admin.getAuthorities.asScala.map(_.getAuthority).toList)
      .put(enabledKey, admin.isEnabled)
      .put(accountNonExpiredKey, admin.isAccountNonExpired)
      .put(credentialsNonExpiredKey, admin.isCredentialsNonExpired)
      .put(accountNonLockedKey, admin.isAccountNonLocked).toBson
  )
}
