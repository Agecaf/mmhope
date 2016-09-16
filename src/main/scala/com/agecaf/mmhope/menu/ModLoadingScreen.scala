package com.agecaf.mmhope.menu

import better.files._
import com.agecaf.mmhope.Mmhope
import com.agecaf.mmhope.core.Geometry._
import com.agecaf.mmhope.graphics.{Manager => g}
import com.agecaf.mmhope.graphics.Screen
import com.agecaf.mmhope.modloading.Data.AssetSet
import com.agecaf.mmhope.modloading.ModLoader
import com.badlogic.gdx.{Gdx, Input}
import org.json4s._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util._

/**
  * Screen for loading a Mod.
  */
class ModLoadingScreen(val mod: JValue, val path: File) extends Screen {

  override val assets = AssetSet(fonts = Set("default-42", "default-20"))

  var loading = false
  var loaded = false

  override def render(δt: Float, center: Placement, alphaMultiplier: Float): Unit = {

    val top = center sideways 0.55 forward -0.3

    implicit val formats = DefaultFormats

    g.text("default-42", (mod\"name") extractOrElse "-", top, alphaMultiplier)
    g.text("default-20", (mod\"author") extractOrElse "", top sideways -0.1 , alphaMultiplier)
    g.text("default-20", path.toString, top sideways -0.15 , alphaMultiplier)


  }

  override def logic(δt: Float): Unit = {

    // Go back
    if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) Mmhope.changeToScreen(ModsMenuScreen)

    if(!loading && !loaded && Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
      ModLoader.load(mod, path) onComplete {
        case Success(_) => loaded = true; loading = false
        case Failure(t) => println(t)
      }
    }
  }
}
