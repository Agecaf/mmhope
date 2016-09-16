package com.agecaf.mmhope.modloading

import com.agecaf.mmhope.modloading.Data.Level
import com.agecaf.mmhope.utils.GameLogging

object LevelLibrary extends GameLogging{
  var storedLevels: Map[String, Level] = Map()

  def storeLevel(lvl: Level, modId: String): Unit = {
    debug(s"Stored level $modId/${lvl.name}.")
    storedLevels += s"$modId/${lvl.name}" -> lvl
  }
}
