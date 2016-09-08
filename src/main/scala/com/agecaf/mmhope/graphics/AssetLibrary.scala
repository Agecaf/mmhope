package com.agecaf.mmhope.graphics
import com.badlogic.gdx.graphics.g2d.BitmapFont

/**
  * Stores construction information for all the assets loaded.
  *
  * This is not responsible for disposing of assets!
  */
object AssetLibrary {

  /**
    * Loads a font given its id.
    * @return the font.
    */
  def loadFont: String => BitmapFont = {
    case "default" => new BitmapFont()
    case _ => new BitmapFont()
  }
}
