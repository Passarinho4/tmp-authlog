package com.tegess.domain.event

import org.bson.types.ObjectId
import org.joda.time.DateTime

case class LoginEvent(id:ObjectId, applicationId:ObjectId, username:String, timeStamp: DateTime)
