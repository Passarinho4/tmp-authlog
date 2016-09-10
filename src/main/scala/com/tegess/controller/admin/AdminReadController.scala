package com.tegess.controller.admin

import com.tegess.persistance.service.admin.AdminService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod, RestController}

@RestController
@Component
class AdminReadController @Autowired() (adminService: AdminService) {

  import AdminReadController._

  @RequestMapping(value = Array("/api/admins"), method = Array(RequestMethod.GET))
  def getAdmins(): List[AdminReadData] = {
    adminService.findAll().map(admin => AdminReadData(admin.getUsername))
  }

}
object AdminReadController {

  case class AdminReadData(username: String)

}