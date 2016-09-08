package com.agecaf.mmhope.graphics
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Pixmap.Format
import com.badlogic.gdx.graphics.{Color, Pixmap, Texture}
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter

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
    case "default" =>
      val generator = new FreeTypeFontGenerator(new FileHandle("Tuffy.ttf"))
      val parameter = new FreeTypeFontParameter()
      parameter.size = 42
      val font = generator.generateFont(parameter)
      generator.dispose()
      font
    case _ => new BitmapFont()
  }

  /**
    * Loads a texture given its id.
    * @return the texture.
    */
  def loadTexture(textureId: String): Texture = {
    // Default texture
    if (textureId == "default") {
      val pixmap = new Pixmap(1, 1, Format.RGB888)
      pixmap.setColor(Color.WHITE)
      pixmap.fill()
      val texture = new Texture(pixmap)
      pixmap.dispose()
      return texture
    }
    new Texture("./mods/examples/modtemplate/bullets.png")
  }
}
