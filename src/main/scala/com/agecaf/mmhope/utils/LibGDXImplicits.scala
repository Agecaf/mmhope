package com.agecaf.mmhope.utils

import better.files.File
import com.badlogic.gdx.files.FileHandle
import scala.language.implicitConversions

object LibGDXImplicits {

  implicit def FileToFIleHandle(path: File): FileHandle =
    new FileHandle(path.toJava)

}
