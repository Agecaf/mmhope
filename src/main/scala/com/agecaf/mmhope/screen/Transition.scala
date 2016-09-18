package com.agecaf.mmhope.screen

import com.agecaf.mmhope.Mmhope
import com.agecaf.mmhope.core.Geometry._

/**
  * An interpolated transition between two screens.
  * @param tpe Type of transition.
  * @param from Previous screen.
  * @param to Target screen.
  * @param time Time taken for the transition.
  */
class Transition(tpe: Symbol, from: Screen, to: Screen, time: Time = 1) extends Screen {

  // Interpolation variable. Varies between 0 and 1.
  var t: Float = 0

  override val assets = from.assets ++ to.assets

  override def render(center: Placement, alphaMultiplier: Float): Unit = {
    val fromPos = tpe match {
      case 'left => center forward -t*t
      case 'right => center forward t*t
      case _ => center
    }
    val toPos = tpe match {
      case 'left => center forward 1-t*t
      case 'right => center forward t*t-1
      case _ => center
    }

    // Render Screens.
    from.render(fromPos, alphaMultiplier * (1-t))
    to.render(toPos, alphaMultiplier * t)
  }

  override def logic(dt: Float): Unit = {
    t += dt / time
    if (t >= 1) Mmhope.changeToScreen(to)

    to.logic(0)
  }
}
