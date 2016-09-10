package com.tegess.persistance.service.event

import com.tegess.domain.application.Application
import com.tegess.domain.event.LoginEvent
import com.tegess.domain.user.User
import com.tegess.persistance.MongoConfig
import com.tegess.persistance.repository.event.LoginEventRepository
import org.bson.types.ObjectId
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.{Configuration, Import}

@Configuration
@Import(Array(classOf[MongoConfig]))
class LoginEventService @Autowired() (val repository: LoginEventRepository) {

  def publishLoginEvent(application: Application, user: User) = {
    repository.save(LoginEvent(new ObjectId, application.id, user.username, new DateTime()))
  }

  def getHourLoginStats(applicationId: ObjectId): IndexedSeq[(Long, Long)] = {
    val now = new DateTime()
      .withSecondOfMinute(0)
      .withMillisOfSecond(0)
    for {
      i <- 0 to 60
      from = now.minusMinutes(i+1)
      to = now.minusMinutes(i)
    } yield (from.getMillis, repository.count(applicationId, from, to))
  }



}
