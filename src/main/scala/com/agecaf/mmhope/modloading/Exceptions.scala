package com.agecaf.mmhope.modloading

import java.io.File


object Exceptions {
  case class FolderDidNotExist(folder: File) extends Throwable

}
