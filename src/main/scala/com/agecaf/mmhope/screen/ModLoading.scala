package com.agecaf.mmhope.screen

import better.files._
import com.agecaf.mmhope.Mmhope
import com.agecaf.mmhope.core.Geometry._
import com.agecaf.mmhope.media.{Manager => g}
import com.agecaf.mmhope.modloading.Data.AssetSet
import com.agecaf.mmhope.modloading.Exceptions.CouldNotCompileFile
import com.agecaf.mmhope.modloading.{ModLoader, RuntimeCompiler}
import com.agecaf.mmhope.utils.GameLogging
import com.badlogic.gdx.{Gdx, Input}
import org.json4s._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util._

/**
  * Screen for loading a Mod.
  */
class ModLoading(val mod: JValue, val path: File) extends Screen with GameLogging {

  override val assets = AssetSet(fonts = Set("default-42", "default-20", "default-14"))

  var loading = false
  var loaded = false
  var message: String = ""

  override def render(center: Placement, alphaMultiplier: Float): Unit = {

    val top = center sideways 0.55 forward -0.3

    implicit val formats = DefaultFormats


    g.text("default-42", (mod\"name") extractOrElse "-", top, alphaMultiplier)
    g.text("default-20", (mod\"author") extractOrElse "", top sideways -0.1 , alphaMultiplier)
    g.text("default-20", path.toString, top sideways -0.15 , alphaMultiplier)

    if (loading)  g.text("default-20", "Loading...",  top sideways -0.25 , alphaMultiplier)
    if (loaded)   g.text("default-20", "Loaded!",     top sideways -0.25 , alphaMultiplier)

    g.text("default-14", message, top sideways -0.35 forward -0.1, alphaMultiplier)

  }

  override def logic(δt: Float): Unit = {

    // Go back
    if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) Mmhope.changeToScreen(ModsMenu)

    if(!loading && !loaded && Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
      loading = true
      ModLoader.load(mod, path) onComplete {
        case Success(_) => loaded = true; loading = false
        case Failure(x: CouldNotCompileFile) =>
          loading = false
          message = x.toString

        case Failure(t) =>
          error(t)
          loading = false
          println(t)
          println(RuntimeCompiler.toolbox.frontEnd.infos)
          throw t
      }
    }
  }
}
