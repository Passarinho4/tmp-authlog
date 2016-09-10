package com.tegess.controller.admin

import com.tegess.controller.admin.AdminWriteController.AdminWriteData
import com.tegess.domain.admin.Admin
import com.tegess.persistance.service.admin.AdminService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.{RequestBody, RequestMapping, RequestMethod, RestController}

@RestController
@Component
class AdminWriteController {

  @Autowired
  var adminService: AdminService = _

  @RequestMapping(value=Array("/api/install"), method=Array(RequestMethod.POST))
  def install(@RequestBody adminWriteData: AdminWriteData) = {
    val admin: Admin = Admin(adminWriteData.username, adminWriteData.password, List())
    adminService.save(admin)
  }

}

object AdminWriteController {
  case class AdminWriteData(username: String, password: String)
}
