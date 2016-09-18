package com.agecaf.mmhope.modloading

import com.agecaf.mmhope.modloading.Data.Level

import scala.concurrent.Future
import scala.concurrent.blocking
import scala.concurrent.ExecutionContext.Implicits.global

object RuntimeCompiler {

  /**
    * Parses and Compiles a string, returning the returned code ready to be run.
    * @param src the source.
    * @return A method to call to run the compiled code.
    */
  def parseAndCompile(src: String): Future[() => Any] = Future { blocking {
      // Get Mirror and toolbox.
      import scala.reflect.runtime.currentMirror
      import scala.tools.reflect.ToolBox
      val toolbox = currentMirror.mkToolBox()

      // Parse the source into a tree.
      val tree = toolbox.parse(src)

      // Compile tree with toolbox.
      val compiled = toolbox.compile(tree)

      // Return compiled code.
      compiled
    }
  }

  def compileLevel(src: String): Future[Level] = {
    parseAndCompile(levelPrefix + src) map (_().asInstanceOf[Level])
  }

  val levelPrefix =
    """import com.agecaf.mmhope.core.Geometry._
      |import com.agecaf.mmhope.core.BulletTypes._
      |import com.agecaf.mmhope.core.BulletMethods._
      |import com.agecaf.mmhope.modloading.Data._
      |import com.agecaf.mmhope.media.{Shared => g}
      |
    """.stripMargin
}
