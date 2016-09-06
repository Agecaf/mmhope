package com.agecaf.mmhope.modloading

import org.json4s._
import org.json4s.native.JsonMethods._
import java.io.File

import scala.io.Source
import scala.concurrent.{Future, Promise}
import scala.util._
import scala.concurrent.ExecutionContext.Implicits.global

import Exceptions._


/**
  * Accumulates information on the mods available.
  *
  * Goes through the /mods directory, looking for index.json files.
  * Once they are found, they are parsed and stored.
  * Then they are available for being queried by the user, to start loading mods.
  *
  */
object IndexReader {

  /**
    * Refreshes all mods' index data.
    *
    * Looks for mods in the /mods directory, then parses their index files.
    * Stores this data in the IndexReader.
    *
    * @return
    */
  def refresh(): Future[Unit] = Future {

    // Ensures the root mod directory exists.
    val modDirectory = new File("../mods")
    if (!modDirectory.exists) throw FolderDidNotExist(modDirectory)
    if (!modDirectory.isDirectory) throw FolderDidNotExist(modDirectory)

    // Looks for paths in
    val modPaths: List[File] = getModPaths(modDirectory)
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



}
