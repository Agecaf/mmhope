package com.agecaf.mmhope

import java.io._

import com.agecaf.mmhope.modloading.IndexReader
import com.agecaf.mmhope.modloading.Exceptions._
import com.badlogic.gdx.Game
import utils.{GameLogger, GameLogging}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util._

class Mmhope extends Game with GameLogging {
  override def create() = {

    // Load indexes.
    infoStart("Indexing")
    IndexReader.refresh().onComplete {
      case Success(()) =>
        infoEnd("Indexing")
        println(GameLogger.info)

      case Failure(FolderDidNotExist(f)) =>
        error(s"Failed Indexing: Folder did not exist: $f")
        println(GameLogger.info)


      case Failure(t) =>
        error(s"Failed Indexing, unexpected: $t")
        println(GameLogger.info)

    }

  }

  override def dispose(): Unit = {
    println("Printing logs")
    // Print Logs.
    val pwd = new PrintWriter(new File("../debug.log"))
    pwd.write(GameLogger.debug.toString)
    pwd.close()

    val pwi = new PrintWriter(new File("../info.log"))
    pwi.write(GameLogger.info.toString)
    pwi.close()
  }
}
