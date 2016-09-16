package com.agecaf.mmhope.media

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

  var definedFonts: Map[String, FontData] = Map()
  var definedTextures: Map[String, TextureData] = Map()

  /**
    * Loads a font given its id.
    * @return the font.
    */
  def loadFont: String => BitmapFont = {

    case fontWithSize(fontName, size) if definedFonts contains fontName =>
      val generator = new FreeTypeFontGenerator(definedFonts(fontName).path)
      val parameter = new FreeTypeFontParameter()
      parameter.size = size.toInt
      val font = generator.generateFont(parameter)
      generator.dispose()
      font

    case fontName if definedFonts contains fontName =>
      val generator = new FreeTypeFontGenerator(definedFonts(fontName).path)
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
    // Texture in library.
    if (definedTextures contains textureId)
      new Texture(definedTextures(textureId).path)

    // Default Texture.
    else {
      val pixmap = new Pixmap(1, 1, Format.RGB888)
      pixmap.setColor(Color.WHITE)
      pixmap.fill()
      val texture = new Texture(pixmap)
      pixmap.dispose()
      return texture
    }
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
    val JArray(fonts) = data \ "fonts" // Todo add fault tolerance
    fonts foreach { font =>
      val JString(name) = font \ "name"
      val JString(path) = font \ "path"
      definedFonts += name -> FontData(name, rootPath / path)
    }

    // Define Textures.
    val JArray(textures) = data \ "textures"
    textures foreach { texture =>
      val JString(name) = texture \ "name"
      val JString(path) = texture \ "path"
      definedTextures += name -> TextureData(name, rootPath / path)
    }
  }

  // Regex patterns
  val fontWithSize = """([a-zA-Z_]+)-([0-9]+)""".r

  case class FontData(name: String, path: File)
  case class TextureData(name: String, path: File)
}
