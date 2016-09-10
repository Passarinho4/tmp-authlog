package com.tegess.controller.test

import com.tegess.domain.test.Test
import com.tegess.persistance.repository.test.TestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod, RestController}

@RestController
@Component
class TestController {

  @Autowired
  var repo:TestRepository = _

  case class HelloWorld(name:String)

  @RequestMapping(value = Array("api/test"), method = Array(RequestMethod.GET))
  def getTest = {
    HelloWorld("HelloWorld")
  }

  @RequestMapping(value = Array("api/test2"), method = Array(RequestMethod.GET))
  def getTest2 = {
    HelloWorld("HelloSzymek")

    repo.insert(new Test("Sam", 22, List()))
  }
}
