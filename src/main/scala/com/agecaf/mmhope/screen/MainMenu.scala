package com.agecaf.mmhope.screen

import com.agecaf.mmhope.Mmhope
import com.agecaf.mmhope.core.Geometry._
import com.agecaf.mmhope.media.{Manager => g}
import com.agecaf.mmhope.modloading.Data.AssetSet
import com.badlogic.gdx.{Gdx, Input}

/**
  * Main Menu.
  */
object MainMenu extends Screen {

  // 0 Mods  1 Challenge  2 Practice  3 Options  4 Logs.
  var currentSelection: Int = 0

  /**
    * See [[Screen]]
    */
  override val assets = AssetSet(fonts = Set("default-42", "default-20"))

  /**
    * See [[Screen]]
    */
  override def render(center: Placement, alphaMultiplier: Float): Unit = {

    val top = center sideways 0.55 forward -0.3

    val subtitle = top sideways -0.1 forward -0.1

    val menuPos = center sideways 0.3 forward -0.35

    // Title
    g.text("default-42", "Mod-Me: HOPE", top, alphaMultiplier)
    g.text("default-20", "Use the arrow keys [Up/Down] to change selection.", subtitle, alphaMultiplier)
    g.text("default-20", "Use the arrow keys [Right/Left] to select/go back.", subtitle sideways -0.05, alphaMultiplier)

    // Mods
    g.text("default-42", "Mods",
      menuPos forward (if(currentSelection == 0) 0.1 else 0),
      alphaMultiplier
    )

    // Challenge
    g.text("default-42", "Challenge",
      menuPos forward (if(currentSelection == 1) 0.1 else 0) sideways -0.15,
      alphaMultiplier
    )

    // Practice
    g.text("default-42", "Practice",
      menuPos forward (if(currentSelection == 2) 0.1 else 0) sideways -0.3,
      alphaMultiplier
    )

    // Options
    g.text("default-42", "Options",
      menuPos forward (if(currentSelection == 3) 0.1 else 0) sideways -0.45,
      alphaMultiplier
    )

    // Logs
    g.text("default-42", "Logs",
      menuPos forward (if(currentSelection == 4) 0.1 else 0) sideways -0.6,
      alphaMultiplier
    )
  }

  /**
    * See [[Screen]]
    */
  override def logic(δt: Float): Unit = {
    // Change selection Up or down.
    if(Gdx.input.isKeyJustPressed(Input.Keys.UP) && currentSelection > 0)
      currentSelection -= 1

    if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && currentSelection < 4)
      currentSelection += 1

    // Select
    if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) currentSelection match {
      case 0 => Mmhope.changeToScreen(new Transition('left, this, ModsMenu, 0.3))
      case 2 => Mmhope.changeToScreen(new Transition('left, this, Practice, 0.3))
      case _ => ()
    }

  }

}
