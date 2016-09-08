package com.agecaf.mmhope.graphics

import com.agecaf.mmhope.modloading.Data.AssetList
import com.agecaf.mmhope.utils.GameLogging
import com.badlogic.gdx.graphics._
import com.badlogic.gdx.graphics.g2d.{BitmapFont, SpriteBatch}
import com.agecaf.mmhope.graphics.AssetLibrary
import com.agecaf.mmhope.core.Geometry._
import com.badlogic.gdx.Gdx
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
  camera.setToOrtho(false, 1.000, 1.618)
  val cameraText: OrthographicCamera = new OrthographicCamera()
  cameraText.setToOrtho(false, 500, 809)

  val viewport: Viewport = new FitViewport(1.000, 1.618, camera)
  val viewportText: Viewport = new FitViewport(500, 809, cameraText)

  var fonts: Map[String, BitmapFont] = Map()
  var textures: Map[String, Texture] = Map()

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
  def load(assets: AssetList): Unit = {
    debugStart("Loading assets com.agecaf.mmhope.graphics.Manager")

    // Dispose of previous, now unnecessary Assets.
    for ((id, font) <- fonts if !(assets.fonts contains id))
      font.dispose()

    for ((id, texture) <- textures if !(assets.textures contains id))
      texture.dispose()

    fonts = fonts filter {case (id, font) => assets.fonts contains id}
    textures = textures filter {case (id, texture) => assets.textures contains id}

    // Load new assets.
    val newFonts: Map[String, BitmapFont] = {
      val ls = for (id <- assets.fonts if !(fonts contains id))
        yield id -> AssetLibrary.loadFont(id)
      ls.toMap
    }
    val newTextures: Map[String, Texture] = {
      val ls = for (id <- assets.textures if !(textures contains id))
        yield id -> AssetLibrary.loadTexture(id)
      ls.toMap
    }

    fonts ++= newFonts
    textures ++= newTextures

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
    * @param scale the scale.
    * @param alpha the transparency to use.
    * @param tint An optional tint.
    */
  def text(fontId: String,
           text: String,
           placement: Placement,
           scale: Float,
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
}
