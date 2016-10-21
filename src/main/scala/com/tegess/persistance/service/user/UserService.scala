package com.tegess.persistance.service.user

import com.tegess.domain.application.Application
import com.tegess.domain.user.{User, UserPhoto}
import com.tegess.persistance.repository.user.{UserPhotoRepository, UserRepository}
import org.bson.types.ObjectId

class UserService(userRepo: UserRepository, userPhotoRepo: UserPhotoRepository) {

  //user
  def save(user: User) = userRepo.save(user)
  def findOne(application: Application, username: String) = userRepo.findOne(application.id, username)
  def findOne(applicationId: ObjectId, username: String) = Option(userRepo.findOne(applicationId, username))
  def findAll(application: Application) = userRepo.findAll(application.id)
  def findAll(applicationId: ObjectId) = userRepo.findAll(applicationId)
  def remove(application: Application, username: String) = userRepo.remove(application.id, username)
  def remove(applicationId: ObjectId, username: String) = userRepo.remove(applicationId, username)

  //user photo
  def save(userPhoto: UserPhoto) = userPhotoRepo.save(userPhoto)
  def findOne(filename: String) = userPhotoRepo.findOne(filename)
  def remove(filename: String) = userPhotoRepo.remove(filename)
}
