package com.agecaf.mmhope.modloading

import com.agecaf.mmhope.modloading.Data.Level
import com.agecaf.mmhope.core.Character
import com.agecaf.mmhope.modloading.Exceptions.CouldNotCompileFile

import scala.concurrent.Future
import scala.concurrent.blocking
import scala.concurrent.ExecutionContext.Implicits.global
import scala.tools.reflect.ToolBoxError

object RuntimeCompiler {

  // Get Mirror and toolbox.
  import scala.reflect.runtime.currentMirror
  import scala.tools.reflect.ToolBox
  val toolbox = currentMirror.mkToolBox()

  /**
    * Parses and Compiles a string, returning the returned code ready to be run.
    * @param src the source.
    * @return A method to call to run the compiled code.
    */
  def parseAndCompile(src: String, path: String): Future[() => Any] = Future { blocking {

      // Parse the source into a tree.
      val tree =
        try {
          toolbox.parse(src)
        } catch {
          case e: ToolBoxError =>
            val seq = toolbox.frontEnd.infos.map { i =>
              s"""[$path]:${i.pos.line - prefixLines}: ${i.msg}"""
            }
            throw CouldNotCompileFile(path, e.message + "\n" + seq.mkString("\n"))
        }

      // Compile tree with toolbox.
      val compiled =
        try {
          toolbox.compile(tree)
        } catch {
          case e: ToolBoxError =>
            val seq = toolbox.frontEnd.infos.map { i =>
              s"""[$path]:${i.pos.line - prefixLines}: ${i.msg}"""
            }
            throw CouldNotCompileFile(path, e.message + "\n" + seq.mkString("\n"))
        }

      // Return compiled code.
      compiled
    }
  }

  def compileLevel(src: String, path: String): Future[Level] = {
    parseAndCompile(levelPrefix + src, path) map (_().asInstanceOf[Level])
  }

  def compileCharacter(src: String, path: String): Future[Character] = {
    parseAndCompile(characterPrefix + src, path) map (_().asInstanceOf[Character])
  }

  val levelPrefix =
    """import com.agecaf.mmhope.core.Geometry._
      |import com.agecaf.mmhope.core.BulletTypes._
      |import com.agecaf.mmhope.core.BulletMethods._
      |import com.agecaf.mmhope.modloading.Data._
      |import com.agecaf.mmhope.media.{Shared => g}
      |import scala.math._
      |import spire.math.Complex
      |import spire.implicits._
      |
      |
      |
      |
    """.stripMargin

  val characterPrefix =
    """import com.agecaf.mmhope.core.Geometry._
      |import com.agecaf.mmhope.core.BulletTypes._
      |import com.agecaf.mmhope.core.BulletMethods._
      |import com.agecaf.mmhope.core.Character
      |import com.agecaf.mmhope.modloading.Data._
      |import com.agecaf.mmhope.media.{Shared => g}
      |import scala.math._
      |import com.badlogic.gdx.{Gdx, Input}
      |import com.agecaf.mmhope.core.CharacterPosition
      |import spire.math.Complex
      |import spire.implicits._
      |
    """.stripMargin

  val prefixLines = 10
}
