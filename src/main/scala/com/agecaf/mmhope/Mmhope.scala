package com.agecaf.mmhope

import java.io._

import com.agecaf.mmhope.core.Geometry._
import com.agecaf.mmhope.modloading.Data.AssetList
import com.agecaf.mmhope.modloading.Exceptions._
import com.agecaf.mmhope.modloading.IndexReader
import com.agecaf.mmhope.utils._
import com.badlogic.gdx.Game

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util._

class Mmhope extends Game with GameLogging {

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





    // ROUGH WORKING
    graphics.Manager.load(AssetList(fonts = List("default"), textures = List("example_bullets")))



    debugEnd("Creating")
  }

  override def dispose(): Unit = {
    debugStart("Disposing")

    // Graphics Manager
    graphics.Manager.dispose()

    debugEnd("Disposing")
    debug("Printing logs.")

    // Print Logs.
    val pwd = new PrintWriter(new File("../debug.log"))
    pwd.write(GameLogger.debug.toString)
    pwd.close()

    val pwi = new PrintWriter(new File("../info.log"))
    pwi.write(GameLogger.info.toString)
    pwi.close()
  }

  override def render(): Unit = {
    graphics.Manager.begin()

    val center = Placement(Point(0.0, 0.0), 0.0)

    val left = Placement(Point(-0.5, 0.0), 0.2)

    graphics.Manager.text("default", "Hello World!", center, 1, 1)
    graphics.Manager.text("default", "Mods", left, 1, 1)
    graphics.Manager.text("default", "Levels", left sideways 0.1, 1, 1)

    val tr = Rect[Float](-0.02f, -0.02f, 0.04f, 0.04f)
    val sr = Rect[Int](20, 20, 20, 20)

    graphics.Manager.draw("example_bullets", center, tr, sr)

    graphics.Manager.end()
  }

  override def resize(width: Int, height: Int): Unit = {
    graphics.Manager.viewport.update(width, height)
    graphics.Manager.viewportText.update(width, height)
  }
}