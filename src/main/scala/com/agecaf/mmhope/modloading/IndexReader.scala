package com.agecaf.mmhope.modloading

import org.json4s._
import org.json4s.native.JsonMethods._
import java.io.File

import scala.io.Source
import scala.concurrent.{Future, Promise}
import scala.util._
import scala.concurrent.ExecutionContext.Implicits.global
import Exceptions._
import com.agecaf.mmhope.modloading.Data.ModIndex
import com.agecaf.mmhope.utils.GameLogging


/**
  * Accumulates information on the mods available.
  *
  * Goes through the /mods directory, looking for index.json files.
  * Once they are found, they are parsed and stored.
  * Then they are available for being queried by the user, to start loading mods.
  *
  */
object IndexReader extends GameLogging {

  // Indices of all Mods
  var modIndices: List[(File, ModIndex)] = List()

  /**
    * Refreshes all mods' index data.
    *
    * Looks for mods in the /mods directory, then parses their index files.
    * Stores this data in the IndexReader.
    *
    * @return
    */
  def refresh(): Future[Unit] = Future {
    infoStart("Indexing")

    // Ensures the root mod directory exists.
    val modDirectory = new File("../mods")
    if (!modDirectory.exists) throw FolderDidNotExist(modDirectory)
    if (!modDirectory.isDirectory) throw FolderDidNotExist(modDirectory)

    // Look for paths in mod directory.
    val modPaths: List[File] = getModPaths(modDirectory)

    // Parse indices.
    modIndices = modPaths map {f => (f, parseIndex(f))}

    infoEnd("Indexing")
  }

  /**
    * Gets the paths for mods in the given directory.
    *
    * Browses the path directory in search for index.json files. Recursive.
    * @return
    */
  def getModPaths(path: File): List[File] = {
    // Looks whether this contains an index.json
    if (path.listFiles exists (_.getName == "index.json"))
      List(path)

    // And otherwise looks through its subdirectories.
    else
      path.listFiles.toList filter (_.isDirectory) flatMap getModPaths
  }

  /**
    * Parses the index file of a mod.
    *
    * Note: Should be called only on directories where we know there's an index file.
    *
    * @param path the path to the mod.
    */
  def parseIndex(path: File): ModIndex = {
    infoStart(s"Parsing $path")

    // Get the file, it should exist!
    val indexFile: File = (path.listFiles filter (_.getName == "index.json"))(0)

    // Read the file
    val indexText: String = Source.fromFile(indexFile).getLines mkString "\n"

    // Parse the File
    val indexJSON = parse(indexText)

    // Extract data
    implicit val formats = DefaultFormats
    val modIndex: ModIndex = indexJSON.extract[ModIndex]

    infoEnd(s"Parsing $path")

    modIndex
  }
}
