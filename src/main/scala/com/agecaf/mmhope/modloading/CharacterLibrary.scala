package com.agecaf.mmhope.modloading

import com.agecaf.mmhope.utils.GameLogging
import com.agecaf.mmhope.core.Character

object CharacterLibrary extends GameLogging {
  var storedCharacters: Map[String, Character] = Map()

  def storeCharacter(char: Character, modId: String): Unit = {
    debug(s"Stored character $modId/${char.name}.")
    storedCharacters += s"$modId/${char.name}" -> char
  }
}
