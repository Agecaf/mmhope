package com.agecaf.mmhope.core


import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.{Matrix3, Matrix4}

import scala.math._
import scala.language.implicitConversions


/**
  * Definitions of important geometric concepts.
  *
  * Note:
  *   In this sense, think of "Geometry" as including space and time!
  */
object Geometry {
  type Time = Float

  // Implicit conversions.
  implicit def DoubleToFloat(x: Double): Float = x.toFloat
  implicit def PlacementToMatrix3(p: Placement): Matrix3 = p.matrix3
  implicit def PlacementToMatrix4(p: Placement): Matrix4 = p.matrix4
  implicit def PoseToMatrix3(p: Pose): Matrix3 = p.matrix3
  implicit def PoseToMatrix4(p: Pose): Matrix4 = p.matrix4

  /**
    * A point in 2D space.
    *
    * @param x Its x-coordinate
    * @param y Its y-coordinate
    */
  case class Point(x: Float, y: Float) {
    def translate(dx: Float, dy: Float): Point =
      Point(x + dx, y + dy)

    def sqrDistFrom(other: Point): Float =
      (x - other.x)*(x - other.x)+(y - other.y)*(y - other.y)

  }

  val originPt: Point = Point(0, 0)
  val centerPt: Point = Point(0.5, 0.5)

  /**
    * A frame of reference with a position and an orientation.
    *
    * @param point Its position.
    * @param orientation Its orientation.
    */
  case class Placement(point: Point, orientation: Float) {
    val x: Float = point.x
    val y: Float = point.y

    def translate(dx: Float, dy: Float): Placement =
      Placement(point translate (dx, dy), orientation)

    def rotate(theta: Float): Placement =
      Placement(point, orientation + theta)

    def forward(d: Float): Placement =
      Placement(
        Point(
          x + d * cos(orientation)    ,
          y + d * sin(orientation)  //,
        )                            ,
        orientation                //,
      )

    def sideways(d: Float): Placement =
      Placement(
        Point(
          x - d * sin(orientation)    ,
          y + d * cos(orientation)  //,
        )                             ,
        orientation                 //,
      )

    // Towards Point

    def matrix3: Matrix3 =
      new Matrix3().setToTranslation(x, y).rotateRad(orientation)

    def matrix4: Matrix4 =
      new Matrix4().setToTranslation(x, y, 0).rotateRad(0, 0, 1, orientation)
  }

  /**
    * A frame of reference with a position, an orientation and a time offset.
    *
    * @param placementOpt Its position and orientation, if defined.
    * @param timeOffset Its time offset.
    */
  case class Pose(placementOpt: Option[Placement], timeOffset: Time) {
    val isDefined = placementOpt.isDefined
    val placement = placementOpt getOrElse Placement(Point(0, 0), 0)
    val x = placement.x
    val y = placement.y
    val point = placement.point
    val orientation = placement.orientation

    def translate(dx: Float, dy: Float): Pose =
      Pose(placementOpt map (_ translate (dx, dy)), timeOffset)

    def rotate(theta: Float): Pose =
      Pose(placementOpt map (_ rotate theta), timeOffset)

    def forward(d: Float): Pose =
      Pose(placementOpt map (_ forward d), timeOffset)

    def sideways(d: Float): Pose =
      Pose(placementOpt map (_ sideways d), timeOffset)

    def delay(dt: Time): Pose =
      Pose(placementOpt, timeOffset + dt)

    // Towards Option[Point]

    def matrix3: Matrix3 = placement.matrix3
    def matrix4: Matrix4 = placement.matrix4
  }

  object Pose {
    def apply(placement: Placement, timeOffset: Time): Pose =
      Pose(Some(placement), timeOffset)
  }

  case class Rect[T](x: T, y: T, w: T, h: T)

}
