package com.agecaf.mmhope.screen

import com.agecaf.mmhope.core.Geometry.Placement
import com.agecaf.mmhope.modloading.Data.AssetSet

/**
  * An abstraction for a Screen.
  *
  * Superscreens can use this to mix and match several screens,
  * superimposing them, or for transition from one to the other.
  */
abstract class Screen {
  /**
    * Called on construction and every time we return to this screen from another one.
    *
    * This is for screens which "retain" state,
    * to refresh some of that state.
    */
  def reset(): Unit = {}

  /**
    * Al rendering of the screen should be here.
    * @param center the central placement, for the superscreen's use.
    * @param alphaMultiplier An alpha multiplier, for the superscreen's use.
    */
  def render(center: Placement, alphaMultiplier: Float): Unit = {}

  /**
    * Any dealing with user input should be here.
    *
    * This method is here so that superscreens can cut input.
    * @param dt The change of time between frames.
    */
  def logic(dt: Float): Unit = {}

  val assets: AssetSet = AssetSet()
}
