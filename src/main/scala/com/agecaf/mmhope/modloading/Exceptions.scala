package com.agecaf.mmhope.modloading

import better.files._


object Exceptions {
  case class FolderDidNotExist(folder: File) extends Throwable

}
