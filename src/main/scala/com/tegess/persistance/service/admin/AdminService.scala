package com.tegess.persistance.service.admin

import com.tegess.domain.admin.Admin
import com.tegess.persistance.MongoConfig
import com.tegess.persistance.repository.admin.AdminRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.{Configuration, Import}
import org.springframework.security.core.userdetails.{UserDetails, UserDetailsService, UsernameNotFoundException}

@Configuration
@Import(Array(classOf[MongoConfig]))
class AdminService @Autowired() (repository: AdminRepository) extends UserDetailsService {
  def save(admin: Admin) = repository.save(admin)
  def find(username: String):Option[Admin] = Option(repository.findOne(username))
  def findAll(): List[Admin] = repository.findAll()
  def remove(username: String) = repository.remove(username)
  def remove(admin: Admin) = repository.remove(admin.getUsername)

  override def loadUserByUsername(username: String): UserDetails = {
    val adminOpt: Option[Admin] = find(username)
    if(adminOpt.isDefined) {
      adminOpt.get
    } else {
      throw new UsernameNotFoundException(s"User not found! - username = $username")
    }
  }
}
