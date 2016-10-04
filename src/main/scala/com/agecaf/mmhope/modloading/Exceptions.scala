package com.agecaf.mmhope.modloading

import better.files._


object Exceptions {
  case class FolderDidNotExist(folder: File) extends Throwable

  case class CouldNotCompileFile(path: String, msg: String) extends Throwable {
    override def toString = s"Could not compile $path:\n$msg"
  }
}
