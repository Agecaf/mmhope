package com.agecaf.mmhope.core

import com.agecaf.mmhope.modloading.Data.{AssetSet, Level}


abstract class Character(val name: String) {

  var levelOpt: Option[Level] = None

  val assets: AssetSet

  def render(alphaMultiplier: Float): Unit

  def logic(dt: Float): Unit

  final def goToNextLevel(): Unit = {
    ()
  }

  final def endLevel(): Unit = {
    levelOpt = None
  }
}
