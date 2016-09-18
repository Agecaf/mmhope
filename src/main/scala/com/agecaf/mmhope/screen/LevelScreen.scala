package com.agecaf.mmhope.screen

import com.agecaf.mmhope.core.Geometry._
import com.agecaf.mmhope.modloading.Data.{AssetSet, Level}


class LevelScreen(level: Level) extends Screen {

  var t: Time = 0

  override val assets = level.assets

  override def render(center: Placement, alphaMultiplier: Float): Unit = {

    // Render.
    level.background.apply(t)
    level.bullet.render(t)
  }

  override def logic(dt: Float): Unit = {
    t += dt
  }
}