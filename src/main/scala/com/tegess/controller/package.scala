package com.tegess

import scala.util.Try

package object controller {

  implicit class OptToTry[T](val option:Option[T]) {
    def toTry:Try[T] = Try(option.get)
  }
}
