package com.tegess.config

import java.util.Collections

import com.avsystem.commons.mongo.MongoOps.DBOps
import com.mongodb.client.{MongoCollection, MongoDatabase}
import com.mongodb.{MongoClient, MongoCredential, ServerAddress}
import com.tegess.domain.test.Test
import com.tegess.persistance.repository.test.TestRepository
import org.bson.BsonDocument
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class Config {
  @Bean def mongoClient: MongoClient = {
    val credential: MongoCredential = MongoCredential.createCredential("Passarinho", "admin", "Passarinho123".toCharArray)
    val serverAddress: ServerAddress = new ServerAddress("51.255.48.55", 27017)
    new MongoClient(serverAddress, Collections.singletonList(credential))
  }

  @Bean
  def db:MongoDatabase = {
    mongoClient.getDatabase("Authlog")
  }

  @Bean
  def adminCollection: MongoCollection[Test] = {
    new DBOps(db).getCollection[Test]("Admin", TestRepository.testCodec)
  }

  @Bean
  def testRepository:TestRepository = {
    new TestRepository(adminCollection)
  }

}
