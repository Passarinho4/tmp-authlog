package com.tegess.persistance.service.admin

import com.tegess.domain.admin.Admin
import com.tegess.persistance.MongoConfig
import com.tegess.persistance.service.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.{Configuration, Import}
import org.springframework.security.core.userdetails.{UserDetails, UserDetailsService}

import scala.util.{Failure, Success, Try}

@Configuration
@Import(Array(classOf[MongoConfig]))
class AdminService @Autowired() (userService: UserService) extends UserDetailsService {

  override def loadUserByUsername(username: String): UserDetails = {

    val result = for {
      applicationId <- Try(Admin.getApplicationIdFromUsername(username))
      realUsername <- Try(Admin.getRealUsernameFromUsername(username))
      user <- Try(userService.findOne(applicationId, realUsername).get)
    } yield Admin(user)

    result match {
      case Success(admin) => admin
      case Failure(e) => throw e
    }
  }
}
