package com.tegess.persistance.service.user

import com.tegess.domain.application.Application
import com.tegess.domain.user.User
import com.tegess.persistance.repository.user.UserRepository
import org.bson.types.ObjectId

class UserService(repository: UserRepository) {
  def save(user: User) = repository.save(user)
  def findOne(application: Application, username: String) = repository.findOne(application.id, username)
  def findOne(applicationId: ObjectId, username: String) = Option(repository.findOne(applicationId, username))
  def findAll(application: Application) = repository.findAll(application.id)
  def findAll(applicationId: ObjectId) = repository.findAll(applicationId)
  def remove(application: Application, username: String) = repository.remove(application.id, username)
  def remove(applicationId: ObjectId, username: String) = repository.remove(applicationId, username)
}
