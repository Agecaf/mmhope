package com.agecaf.mmhope.menu

import com.agecaf.mmhope.Mmhope
import com.agecaf.mmhope.core.Geometry._
import com.agecaf.mmhope.graphics.Screen
import com.agecaf.mmhope.modloading.Data.AssetSet
import com.agecaf.mmhope.modloading.IndexReader
import com.agecaf.mmhope.graphics.{Manager => g}
import com.badlogic.gdx.{Gdx, Input}
import org.json4s._
import scala.language.postfixOps


/**
  * Screen for choosing a Mod.
  */
object ModsMenuScreen extends Screen {

  var currentSelection: Int = 0

  /**
    * See [[Screen]]
    */
  override val assets = AssetSet(fonts = Set("default-42", "default-20"))

  override def reset(): Unit = {
    // Check the currentSelection is still valid
    if (currentSelection >= IndexReader.modIndices.length)
      currentSelection = IndexReader.modIndices.length - 1
  }

  override def render(δt: Float, center: Placement, α: Float): Unit = {

    val currentPos = center forward -0.3 sideways (currentSelection * 0.1)

    IndexReader.modIndices.zipWithIndex foreach {
      case ((_, mod), i) =>
        renderMod(mod, currentPos sideways -(i * 0.1), α)
    }
  }

  def renderMod(mod: JValue, placement: Placement, alphaMultiplier: Float): Unit = {
    implicit val formats = DefaultFormats

    g.text("default-42", (mod \ "name") extractOrElse "-", placement, alphaMultiplier)
  }

  override def logic(δt: Float): Unit = {
    // Change selection Up or down.
    if(Gdx.input.isKeyJustPressed(Input.Keys.UP) && currentSelection > 0)
      currentSelection -= 1

    if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && currentSelection < IndexReader.modIndices.length - 1)
      currentSelection += 1

    // Go back
    if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT))
      Mmhope.changeToScreen(MainMenuScreen)

    // Select
    if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
      val (path, mod) = IndexReader.modIndices(currentSelection)
      Mmhope.changeToScreen(new ModLoadingScreen(mod, path))
    }
  }
}
