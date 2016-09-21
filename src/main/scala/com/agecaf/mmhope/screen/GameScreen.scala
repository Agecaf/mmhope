package com.agecaf.mmhope.screen

import com.agecaf.mmhope.core.Geometry._
import com.agecaf.mmhope.modloading.Data.{AssetSet, Level}
import com.agecaf.mmhope.core.Character
import com.agecaf.mmhope.media.Manager

class GameScreen(character: Character) extends Screen {

  var t: Time = 0

  override val assets =
    character.levelOpt map (_.assets) getOrElse AssetSet() ++ character.assets

  override def reset(): Unit = {
    character.levelOpt match {
      case Some(level) => Manager.playMusic(level.music)
    }
  }

  override def render(center: Placement, alphaMultiplier: Float): Unit = {

    // Render.
    character.render(alphaMultiplier)
  }

  override def logic(dt: Float): Unit = {
    character.logic(dt)
  }
}