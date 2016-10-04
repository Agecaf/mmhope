package com.agecaf.mmhope.media

import better.files._
import com.agecaf.mmhope.utils.GameLogging
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Pixmap.Format
import com.badlogic.gdx.graphics.{Color, Pixmap, Texture}
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import org.json4s._
import org.json4s.native.JsonMethods._

import scala.language.postfixOps
import com.agecaf.mmhope.utils.LibGDXImplicits._
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music

/**
  * Stores construction information for all the assets loaded.
  *
  * This is not responsible for disposing of assets!
  */
object AssetLibrary extends GameLogging {

  var definedFonts: Map[String, FontData] = Map()
  var definedTextures: Map[String, TextureData] = Map()
  var definedMusic: Map[String, MusicData] = Map()

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

  def loadMusic(musicId: String): Music = {
    // Texture in library
    if (definedMusic contains musicId)
      Gdx.audio.newMusic(definedMusic(musicId).path)
    else
      Gdx.audio.newMusic(definedMusic("emotional").path)
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
    val fonts = data \ "fonts" match {
      case JArray(ls) => ls
      case JNothing => List()
      case _ => error(s"Malformed asset JSON in $rootPath: ${render(data)}. Incorrect 'fonts' field."); List()
    }
    fonts foreach { font =>
      (font \ "name", font \ "path") match {
        case (JString(name), JString(path)) =>
          definedFonts += name -> FontData(name, rootPath / path)
        case _ =>
          error(s"Malformed font JSON in $rootPath: ${render(font)}. Missing 'name' and/or 'path'.")
      }
    }

    // Define Textures.
    val textures = data \ "textures" match {
      case JArray(ls) => ls
      case JNothing => List()
      case _ => error(s"Malformed asset JSON in $rootPath: ${render(data)}. Incorrect 'textures' field."); List()
    }
    textures foreach { texture =>
      (texture \ "name", texture \ "path") match {
        case (JString(name), JString(path)) =>
          definedTextures += name -> TextureData(name, rootPath / path)
        case _ =>
          error(s"Malformed texture JSON in $rootPath: ${render(texture)}. Missing 'name' and/or 'path'.")
      }
    }

    // Define music.
    val music = data \ "music" match {
      case JArray(ls) => ls
      case JNothing => List()
      case _ => error(s"Malformed asset JSON in $rootPath: ${render(data)}. Incorrect 'music' field."); List()
    }
    music foreach { song =>
      (song \ "name", song \ "path") match {
        case (JString(name), JString(path)) =>
          definedMusic += name -> MusicData(name, rootPath / path)
        case _ =>
          error(s"Malformed music JSON in $rootPath: ${render(song)}. Missing 'name' and/or 'path'.")

      }
    }

  }

  // Regex patterns
  val fontWithSize = """([a-zA-Z_]+)-([0-9]+)""".r

  case class FontData(name: String, path: File)
  case class TextureData(name: String, path: File)
  case class MusicData(name: String, path: File)
}
