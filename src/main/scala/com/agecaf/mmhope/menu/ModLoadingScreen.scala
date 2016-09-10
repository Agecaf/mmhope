package com.agecaf.mmhope.menu

import java.io.File

import com.agecaf.mmhope.Mmhope
import com.agecaf.mmhope.core.Geometry._
import com.agecaf.mmhope.graphics.{Manager => g}
import com.agecaf.mmhope.graphics.Screen
import com.agecaf.mmhope.modloading.Data.{AssetSet, ModIndex}
import com.badlogic.gdx.{Gdx, Input}

/**
  * Screen for loading a Mod.
  */
class ModLoadingScreen(val mod: ModIndex, val path: File) extends Screen {

  override val assets = AssetSet(fonts = Set("default-42", "default-20"))

  override def render(δt: Float, center: Placement, alphaMultiplier: Float): Unit = {

    val top = center sideways 0.55 forward -0.3

    g.text("default-42", mod.name, top, alphaMultiplier)
    g.text("default-20", mod.author getOrElse "", top sideways -0.1 , alphaMultiplier)
    g.text("default-20", path.toString, top sideways -0.15 , alphaMultiplier)


  }

  override def logic(δt: Float): Unit = {

    // Go back
    if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) Mmhope.changeToScreen(ModsMenuScreen)

  }
}
