package com.tegess.persistance.service.application

import com.tegess.domain.admin.Admin
import com.tegess.domain.application.Application
import com.tegess.persistance.MongoConfig
import com.tegess.persistance.repository.application.ApplicationRepository
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.{Configuration, Import}

@Configuration
@Import(Array(classOf[MongoConfig]))
class ApplicationService @Autowired() (val repository: ApplicationRepository) {

  def save(application: Application) = repository.save(application)
  def findOne(id: ObjectId) = Option(repository.findOne(id))
  def findAll(admin: Admin) = repository.findAll(admin.getRealUsername)
  def findAll() = repository.findAll()
  def remove(id: ObjectId) = repository.remove(id)
  def remove(application: Application) = repository.remove(application.id)

}
