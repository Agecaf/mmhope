package com.agecaf.mmhope.screen

import com.agecaf.mmhope.Mmhope
import com.agecaf.mmhope.core.Geometry._
import com.agecaf.mmhope.media.{Manager => g}
import com.agecaf.mmhope.modloading.Data.{AssetSet, Level}
import com.agecaf.mmhope.modloading.{CharacterLibrary, IndexReader, LevelLibrary}
import com.badlogic.gdx.{Gdx, Input}
import org.json4s.{DefaultFormats, _}

class CharacterSelect(level: Level) extends Screen {
  var currentSelection: Int = 0

  override val assets = AssetSet(fonts = Set("default-42", "default-20"))

  override def render(center: Placement, alphaMultiplier: Float): Unit = {

    val currentPos = center forward -0.3 sideways (currentSelection * 0.1)

    CharacterLibrary.storedCharacters.zipWithIndex foreach {case ((name, char), i) =>
      g.text(
        fontId = "default-42",
        text = char.name,
        placement = currentPos sideways (-0.1 * i),
        alpha = alphaMultiplier
      )
    }
  }

  override def logic(δt: Float): Unit = {
    // Change selection Up or down.
    if(Gdx.input.isKeyJustPressed(Input.Keys.UP) && currentSelection > 0)
      currentSelection -= 1

    if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && currentSelection < IndexReader.modIndices.length - 1)
      currentSelection += 1

    // Go back
    if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT))
      Mmhope.changeToScreen(new Transition('right, this, Practice, 0.3))

    // Select
    if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
      val (_, char) = CharacterLibrary.storedCharacters.toList(currentSelection)
      char.levelOpt = Some(level)
      Mmhope.changeToScreen(new GameScreen(char))
    }
  }
}
