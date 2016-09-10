package com.agecaf.mmhope

import java.io._

import com.agecaf.mmhope.core.Geometry._
import com.agecaf.mmhope.graphics.Screen
import com.agecaf.mmhope.modloading.Data.AssetSet
import com.agecaf.mmhope.modloading.Exceptions._
import com.agecaf.mmhope.modloading.IndexReader
import com.agecaf.mmhope.menu.MainMenuScreen
import com.agecaf.mmhope.utils._
import com.badlogic.gdx.{Game, Gdx}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util._

object Mmhope extends Game with GameLogging {

  var currentScreen: Screen = MainMenuScreen

  /**
    * Called once Gdx is ready to run.
    */
  override def create() = {
    debugStart("Creating")

    // Load indexes.
    IndexReader.refresh().onComplete {
      case Success(()) =>

      case Failure(FolderDidNotExist(f)) =>
        error(s"Failed Indexing: Folder did not exist: $f")

      case Failure(t) =>
        error(s"Failed Indexing, unexpected: $t")
    }

    // Graphics Manager
    graphics.Manager.create()

    // Starts initial screen.
    changeToScreen(MainMenuScreen)




    // ROUGH WORKING
    //graphics.Manager.load(AssetList(fonts = List("default"), textures = List("example_bullets")))



    debugEnd("Creating")
  }

  /**
    * Called once the window is closed, to dispose of any disposables left.
    */
  override def dispose(): Unit = {
    debugStart("Disposing")

    // Graphics Manager
    graphics.Manager.dispose()

    debugEnd("Disposing")
    debug("Printing logs.")

    println(GameLogger.debug.toString)

    // Print Logs.
    val pwd = new PrintWriter(new File("../debug.log"))
    pwd.write(GameLogger.debug.toString)
    pwd.close()

    val pwi = new PrintWriter(new File("../info.log"))
    pwi.write(GameLogger.info.toString)
    pwi.close()
  }

  /**
    * Called once every frame.
    */
  override def render(): Unit = {
    val dt = Gdx.graphics.getDeltaTime

    // Triggers logic on current screen.
    currentScreen.logic(dt)

    graphics.Manager.begin()

      // Render the current screen.
      // This also contains any frame logic.
      currentScreen.render(
        dt = dt,
        center = Placement(Point(0.0, 0.0), 0.0),
        alphaMultiplier = 1
      )

    graphics.Manager.end()
  }

  /**
    * Called when the screen is resized.
    */
  override def resize(width: Int, height: Int): Unit = {
    graphics.Manager.viewport.update(width, height)
    graphics.Manager.viewportText.update(width, height)
  }

  /**
    * Changes into the given screen.
    *
    * Notes: Loads assets required by screen.
    */
  def changeToScreen(screen: Screen): Unit = {
    graphics.Manager.load(screen.assets)
    currentScreen = screen
    currentScreen.reset()
  }
}
