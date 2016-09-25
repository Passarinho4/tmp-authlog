package com.tegess.domain.user

import org.springframework.util.MimeType

/**
  * Wrapper for profile photo. There is the requirement that size has to be smaller than 16MB.
  */
case class UserPhoto(filename: String, photo: Array[Byte], mimeType: MimeType){
  require((photo.length/(1024 * 1024)) < 16)
}

