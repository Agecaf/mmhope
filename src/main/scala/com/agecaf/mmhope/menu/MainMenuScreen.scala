package com.agecaf.mmhope.menu

import com.agecaf.mmhope.Mmhope
import com.agecaf.mmhope.core.Geometry._
import com.agecaf.mmhope.graphics.Screen
import com.agecaf.mmhope.graphics.{Manager => g}
import com.agecaf.mmhope.modloading.Data.AssetList
import com.badlogic.gdx.{Gdx, Input}

/**
  * Main Menu.
  */
object MainMenuScreen extends Screen {

  // 0 Mods  1 Challenge  2 Practice  3 Options  4 Logs.
  var currentSelection: Int = 0

  /**
    * See [[Screen]]
    */
  override val assets = AssetList(fonts = List("default-42", "default-20"))

  /**
    * See [[Screen]]
    */
  override def render(δt: Float, center: Placement, α: Float): Unit = {

    val top = center sideways 0.55 forward -0.3

    val subtitle = top sideways -0.1 forward -0.1

    val menuPos = center sideways 0.3 forward -0.35

    // Title
    g.text("default-42", "Mod-Me: HOPE", top, α)
    g.text("default-20", "Use the arrow keys [Up/Down] to change selection.", subtitle, α)
    g.text("default-20", "Use the arrow keys [Right/Left] to select/go back.", subtitle sideways -0.05, α)

    // Mods
    g.text("default-42", "Mods",
      menuPos forward (if(currentSelection == 0) 0.1 else 0),
      α
    )

    // Challenge
    g.text("default-42", "Challenge",
      menuPos forward (if(currentSelection == 1) 0.1 else 0) sideways -0.15,
      α
    )

    // Practice
    g.text("default-42", "Practice",
      menuPos forward (if(currentSelection == 2) 0.1 else 0) sideways -0.3,
      α
    )

    // Options
    g.text("default-42", "Options",
      menuPos forward (if(currentSelection == 3) 0.1 else 0) sideways -0.45,
      α
    )

    // Logs
    g.text("default-42", "Logs",
      menuPos forward (if(currentSelection == 4) 0.1 else 0) sideways -0.6,
      α
    )
  }

  /**
    * See [[Screen]]
    */
  override def logic(δt: Float): Unit = {
    // Change selection Up or down.
    if(Gdx.input.isKeyJustPressed(Input.Keys.UP) && currentSelection > 0) currentSelection -= 1
    if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && currentSelection < 4) currentSelection += 1

    // Select
    if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) currentSelection match {
      case 0 => Mmhope.changeToScreen(ModsMenuScreen)
      case _ => ()
    }

  }

}
