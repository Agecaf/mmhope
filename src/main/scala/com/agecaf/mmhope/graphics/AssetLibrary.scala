package com.agecaf.mmhope.graphics

import better.files._
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Pixmap.Format
import com.badlogic.gdx.graphics.{Color, Pixmap, Texture}
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import org.json4s._
import com.agecaf.mmhope.utils.LibGDXImplicits._

/**
  * Stores construction information for all the assets loaded.
  *
  * This is not responsible for disposing of assets!
  */
object AssetLibrary {

  var fontsDefined: Map[String, FontData] = Map()

  /**
    * Loads a font given its id.
    * @return the font.
    */
  def loadFont: String => BitmapFont = {

    case fontWithSize(fontName, size) if fontsDefined contains fontName =>
      val generator = new FreeTypeFontGenerator(fontsDefined(fontName).path)
      val parameter = new FreeTypeFontParameter()
      parameter.size = size.toInt
      val font = generator.generateFont(parameter)
      generator.dispose()
      font

    case fontName if fontsDefined contains fontName =>
      val generator = new FreeTypeFontGenerator(fontsDefined(fontName).path)
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

  /** Adds assets to the asset library.
    *
    * Note they are not actually loaded, only defined.
    *
    * @param data The data with the asset definitions.
    * @param rootPath The path from which the assets are referred by.
    */
  def defineAssets(data: JValue, rootPath: File): Unit = {
    // Define fonts.
    val JArray(fonts) = data \ "fonts"
    fonts foreach { font =>
      val JString(name) = font \ "name"
      val JString(relPath) = font \ "path"
      val path = rootPath / relPath
      fontsDefined += name -> FontData(name, path)
    }
  }

  // Regex patterns
  val fontWithSize = """([a-zA-Z_]+)-([0-9]+)""".r

  case class FontData(name: String, path: File)
}
