package com.agecaf.mmhope.modloading

import org.json4s._
import better.files._
import com.agecaf.mmhope.graphics.AssetLibrary

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import com.agecaf.mmhope.modloading.RuntimeCompiler._
import com.agecaf.mmhope.utils.GameLogging

object ModLoader extends GameLogging {

  /** Loads a mod.
    *
    * @param data The data (found in the index.json file).
    * @param rootPath The path of the mod.
    * @return A future Unit that resolves once all things have been properly stored.
    */
  def load(data: JValue, rootPath: File): Future[Unit] = { // TODO make this fault tolerant!
    val JString(modId) = data \ "id"

    infoStart(s"loading mod $modId")

    // Define assets.
    AssetLibrary.defineAssets(data \ "assets", rootPath) // TODO pass id to asset library.

    // Compile and store levels.
    val JArray(levels) = data \ "levels"
    val levelFutures = levels map { case JString(path) =>
      compileLevel((rootPath / path).lines mkString "\n").map (LevelLibrary.storeLevel(_, modId))
    }

    // Return the joint future of the level creation and storage.
    Future.sequence(levelFutures) map (_ => infoEnd(s"loading mod $modId"): Unit)
  }


}
