package com.agecaf.mmhope

import com.badlogic.gdx.backends.lwjgl._

/**
  * Created by agecaf on 9/8/16.
  */
object Main extends App {
  val cfg = new LwjglApplicationConfiguration
  cfg.title = "mmhope"
  cfg.height = 750
  cfg.width = 500
  cfg.forceExit = false
  new LwjglApplication(new Mmhope, cfg)
}