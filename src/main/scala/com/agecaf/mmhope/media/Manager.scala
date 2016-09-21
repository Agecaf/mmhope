package com.agecaf.mmhope.media

import com.agecaf.mmhope.modloading.Data.AssetSet
import com.agecaf.mmhope.utils.GameLogging
import com.badlogic.gdx.graphics._
import com.badlogic.gdx.graphics.g2d.{BitmapFont, SpriteBatch}
import com.agecaf.mmhope.media.AssetLibrary
import com.agecaf.mmhope.core.Geometry._
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.Pixmap.Format
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.utils.viewport.{FitViewport, Viewport}

/**
  * Manages the rendering as a whole.
  */
object Manager extends GameLogging {

  private var spriteBatchOpt: Option[SpriteBatch] = None
  def spriteBatch: SpriteBatch = spriteBatchOpt.get

  val camera: OrthographicCamera = new OrthographicCamera()
  camera.setToOrtho(false, 1.000, 1.25)
  val cameraText: OrthographicCamera = new OrthographicCamera()
  cameraText.setToOrtho(false, 500, 625)

  val viewport: Viewport = new FitViewport(1.000, 1.25, camera)
  val viewportText: Viewport = new FitViewport(500, 625, cameraText)

  var fonts: Map[String, BitmapFont] = Map()
  var textures: Map[String, Texture] = Map()
  var music: Map[String, Music] = Map()

  /**
    * Creates the necessary graphic objects for the whole execution of the program.
    */
  def create(): Unit = {
    debugStart("Creating com.agecaf.mmhope.graphics.Manager")

    // Sprite batch.
    spriteBatchOpt = Some(new SpriteBatch())
    spriteBatch.setProjectionMatrix(camera.projection)

    debugEnd("Creating com.agecaf.mmhope.graphics.Manager")
  }

  /**
    * Disposes everything needing disposal.
    */
  def dispose(): Unit = {
    debugStart("Disposing com.agecaf.mmhope.graphics.Manager")

    // Dispose of assets
    fonts foreach {case (id, font) => font.dispose()}
    fonts = Map()

    textures foreach {case (id, texture) => texture.dispose()}
    textures = Map()

    // Sprite batch.
    spriteBatch.dispose()
    spriteBatchOpt = None

    debugEnd("Disposing com.agecaf.mmhope.graphics.Manager")
  }

  /**
    * Load assets in AssetList, and dispose of all unused assets.
    * @param assets the AssetList to load.
    */
  def load(assets: AssetSet): Unit = {
    debugStart("Loading assets com.agecaf.mmhope.graphics.Manager")

    val oldAssets = AssetSet(
      fonts.keys.toSet,
      textures.keys.toSet,
      Set()
    )

    // Dispose of previous, now unnecessary Assets.
    val unusedAssets = oldAssets -- assets

    unusedAssets.fonts foreach {fonts(_).dispose()}
    unusedAssets.textures foreach {textures(_).dispose()}
    unusedAssets.music foreach {id => music(id).stop(); music(id).dispose()}

    fonts = fonts filter {case (id, font) => assets.fonts contains id}
    textures = textures filter {case (id, texture) => assets.textures contains id}
    music = music filter {case (id, music) => assets.music contains id}

    // Add new assets.
    val newAssets = assets -- oldAssets

    fonts ++=
      (newAssets.fonts map {id =>
        id -> AssetLibrary.loadFont(id)
      }).toMap

    textures ++=
      (newAssets.textures map {id =>
        id -> AssetLibrary.loadTexture(id)
      }).toMap

    music ++=
      (newAssets.music map {id =>
        id -> AssetLibrary.loadMusic(id)
      }).toMap

    debugEnd("Loading assets com.agecaf.mmhope.graphics.Manager")
  }

  /**
    * Clears screen and begins spriteBatch.
    */
  def begin(): Unit = {
    // Clear screen.
    Gdx.gl.glClearColor(0, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    camera.update()
    cameraText.update()

    // Begin.
    spriteBatch.begin()
    spriteBatch.setProjectionMatrix(camera.projection)
    spriteBatch.setTransformMatrix(Placement(Point(0, 0), 0))
  }

  /**
    * Ends spriteBatch.
    */
  def end(): Unit = spriteBatch.end()

  /**
    * Renders text at placement given a font.
    * @param fontId the font to use.
    * @param text the text to render
    * @param placement where to render the text.
    * @param alpha the transparency to use.
    * @param tint An optional tint.
    */
  def text(fontId: String,
           text: String,
           placement: Placement,
           alpha: Float = 1,
           tint: Option[Color] = None): Unit = {

    // TODO scale, tint and alpha.

    if (!(fonts contains fontId)) {error(s"Font $fontId was not found."); return}
    val font: BitmapFont = fonts(fontId)

    spriteBatch.setProjectionMatrix(cameraText.projection)
    spriteBatch.setTransformMatrix(
      Placement(
        Point(placement.x * 500, placement.y * 500),
        placement.orientation)
    )
    font.draw(spriteBatch, text, 0, 0)
    spriteBatch.setProjectionMatrix(camera.projection)
  }

  /**
    * Draw a texture at a placement.
    * @param textureId the ID of the texture to draw.
    * @param placement the placement to draw the texture.
    * @param targetRect the target rectangle (with respect to placement).
    * @param sourceRect the source rectangle.
    * @param flipX whether to flip horizontally. Defaults to true.
    * @param flipY whether to flip vertically. Defaults to false.
    */
  def draw(
      textureId: String,
      placement: Placement,
      targetRect: Rect[Float],
      sourceRect: Rect[Int],
      flipX: Boolean = false,
      flipY: Boolean = false): Unit = {

    // TODO tint, alpha

    if (!(textures contains textureId)) {error(s"Texture $textureId was not found."); return}
    val texture = textures(textureId)

    spriteBatch.setTransformMatrix(placement)
    spriteBatch.draw(
      texture,
      targetRect.x, targetRect.y,
      targetRect.w, targetRect.h,
      sourceRect.x, sourceRect.y,
      sourceRect.w, sourceRect.h,
      flipX, flipY
    )
  }

  def playMusic(musicId: String): Unit = {
    music(musicId).setLooping(true)
    music(musicId).play()
  }

  def stopMusic(musicId: String): Unit = {
    music(musicId).stop()
  }
}
