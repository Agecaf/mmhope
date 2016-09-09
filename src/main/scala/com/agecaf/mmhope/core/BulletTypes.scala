package com.agecaf.mmhope.core

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.{Matrix3, Matrix4}

import scala.math._
import scala.language.implicitConversions
import Geometry._

/**
  * The type definitions for Bullets.
  *
  * Note:
  *   In this sense, "Bullet" is something that can be rendered and which has
  *   a "hit-box". In a sense, even the background and text can be "hit-less" bullets!
  */
object BulletTypes {
  type RenderShapeAtPlace = (Placement) => Unit
  type IsHittingAtPlace = (Placement, Point) => Boolean

  type Movement = Time => Placement

  type RenderShapeAtTime = (Time) => Unit
  type IsHittingAtTime = (Time, Point) => Boolean

  case class StaticBullet(render: RenderShapeAtPlace, isHitting: IsHittingAtPlace)
  case class Bullet(render: RenderShapeAtTime, isHitting: IsHittingAtTime)
}
