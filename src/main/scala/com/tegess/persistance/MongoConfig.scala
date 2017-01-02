package com.tegess.persistance

import java.util.Collections

import com.avsystem.commons.mongo.MongoOps.DBOps
import com.mongodb.client.MongoDatabase
import com.mongodb._
import com.tegess.domain.application.Application
import com.tegess.domain.event.LoginEvent
import com.tegess.domain.user.{User, UserPhoto}
import com.tegess.persistance.repository.application.ApplicationRepository
import com.tegess.persistance.repository.event.LoginEventRepository
import com.tegess.persistance.repository.user.{UserPhotoRepository, UserRepository}
import com.tegess.persistance.service.admin.AdminService
import com.tegess.persistance.service.application.ApplicationService
import com.tegess.persistance.service.event.LoginEventService
import com.tegess.persistance.service.user.UserService
import org.springframework.context.annotation.{Bean, Configuration}

@Configuration
class MongoConfig {

  @Bean def mongoClient: MongoClient = {
    val credential: MongoCredential = MongoCredential.createCredential("Passarinho", "admin", "Passarinho123".toCharArray)
    val serverAddress: ServerAddress = new ServerAddress("51.255.48.55", 27017)
    val options = MongoClientOptions.builder().connectionsPerHost(10).build()
    new MongoClient(serverAddress, Collections.singletonList(credential), options)
  }

  @Bean
  def db:MongoDatabase = mongoClient.getDatabase("Authlog")

  @Bean
  def adminService:AdminService = {
    new AdminService(userService)
  }

  @Bean
  def applicationService: ApplicationService = {
    val applicationCollection = new DBOps(db).getCollection[Application]("Application", ApplicationRepository.codec)
    val applicationRepository = new ApplicationRepository(applicationCollection)
    new ApplicationService(applicationRepository)
  }

  @Bean
  def userService: UserService = {
    val userCollection = new DBOps(db).getCollection[User]("User", UserRepository.codec)
    val userRepository = new UserRepository(userCollection)
    val userPhotoCollection = new DBOps(db).getCollection[UserPhoto]("UserPhoto", UserPhotoRepository.codec)
    val userPhotoRepository = new UserPhotoRepository(userPhotoCollection)
    new UserService(userRepository, userPhotoRepository)
  }

  @Bean
  def loginEventService: LoginEventService = {
    val loginEventCollection = new DBOps(db).getCollection[LoginEvent]("LoginEvent", LoginEventRepository.codec)
    val loginEventRepository = new LoginEventRepository(loginEventCollection)
    new LoginEventService(loginEventRepository)
  }

}
