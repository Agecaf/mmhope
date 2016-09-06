package com.agecaf.mmhope

import java.io._

import com.agecaf.mmhope.modloading.IndexReader
import com.agecaf.mmhope.modloading.Exceptions._
import com.badlogic.gdx.Game
import utils._

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

    debugEnd("Creating")
  }

  override def dispose(): Unit = {
    debug("Printing logs.")

    // Print Logs.
    val pwd = new PrintWriter(new File("../debug.log"))
    pwd.write(GameLogger.debug.toString)
    pwd.close()

    val pwi = new PrintWriter(new File("../info.log"))
    pwi.write(GameLogger.info.toString)
    pwi.close()
  }
}
