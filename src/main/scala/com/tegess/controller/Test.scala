package com.tegess.controller

object Test {

  type Set = Int => Boolean

  def singletonSet(a: Int): Set = (i:Int) => i == a

}
