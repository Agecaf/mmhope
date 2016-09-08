package com.agecaf.mmhope.graphics

import com.agecaf.mmhope.modloading.Data.AssetList
import com.agecaf.mmhope.utils.GameLogging
import com.badlogic.gdx.graphics._
import com.badlogic.gdx.graphics.g2d.{BitmapFont, SpriteBatch}
import com.agecaf.mmhope.graphics.AssetLibrary
import com.agecaf.mmhope.core.Geometry._
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Pixmap.Format
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
  cameraText.setToOrtho(false, 1000, 1618)

  val viewport: Viewport = new FitViewport(1.000, 1.618, camera)
  val viewportText: Viewport = new FitViewport(1000, 1618, cameraText)

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



    // TODO Debug
    val pixmap = new Pixmap(1, 1, Format.RGB888)
    pixmap.setColor(Color.BLUE)
    pixmap.fill()
    textures = Map("default" -> new Texture(pixmap))
    pixmap.dispose()
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

    fonts = fonts filter {case (id, font) => assets.fonts contains id}

    // Load new assets.
    val newFonts: Map[String, BitmapFont] = {
      val ls = for (id <- assets.fonts if !(fonts contains id))
        yield id -> AssetLibrary.loadFont(id)
      ls.toMap
    }
    fonts ++= newFonts

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

    spriteBatch.draw(textures("default"), - 0.6f, -1.0f, 2f, 3f)
    val font: BitmapFont = new BitmapFont()
    font.getData.setScale(0.1)
    //font.draw(spriteBatch, "Hello", 0, 0)
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

    if (!(fonts contains fontId)) {error(s"Font $fontId was not loaded."); return}
    val font: BitmapFont = fonts(fontId)

    spriteBatch.setProjectionMatrix(cameraText.projection)
    spriteBatch.setTransformMatrix(
      Placement(
        Point(placement.x * 1000, placement.y * 1000),
        placement.orientation)
    )
    font.getData.setScale(5, 5)
    font.draw(spriteBatch, text, 0, 0)
    spriteBatch.setProjectionMatrix(camera.projection)
  }
}
