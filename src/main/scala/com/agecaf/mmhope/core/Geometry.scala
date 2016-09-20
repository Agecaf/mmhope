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
  implicit def DoubleRectToFloatRect(r: Rect[Double]): Rect[Float] = Rect[Float](r.x, r.y, r.w, r.h)
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

    def + (vec: Vec2) = Point(x + vec.x, y + vec.y)
    def toVec2 = Vec2(x, y)
    def to (other: Point) = Vec2(other.x - x, other.y - y)

    def sqrDistFrom(other: Point): Float =
      (x - other.x)*(x - other.x)+(y - other.y)*(y - other.y)

  }

  val originPt: Point = Point(0, 0)

  case class Vec2(x: Float, y: Float) {
    def + (other: Vec2) = Vec2(x + other.x , y + other.y)
    def - (other: Vec2) = Vec2(x - other.x , y - other.y)
    def * (scalar: Float) = Vec2(x * scalar, y * scalar)
    def dot (other: Vec2) = x * other.x + y * other.y
    def toPoint: Point = Point(x, y)
    def orientation: Float = atan2(y, x)
    def abs: Float = sqrt(x*x + y*y)
  }

  def distance(p1: Point, p2: Point) = {
    hypot(p1.x - p2.x, p1.y - p2.y)
  }

  def distance(v1: Vec2, v2: Vec2) = {
    hypot(v1.x - v2.x, v1.y - v2.y)
  }

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

    def towards (p: Point): Placement =
      Placement(point, (point to p).orientation)

    def towards (s: Symbol): Placement =
      Placement(point, s match {
        case 'N => Pi*0.5
        case 'W => Pi
        case 'S => Pi*1.5
        case 'E => 0
        case 'NE => Pi*0.25
        case 'NW => Pi*0.75
        case 'SW => Pi*1.25
        case 'SE => Pi*1.75
        case _ => orientation
      })

    def matrix3: Matrix3 =
      new Matrix3().setToTranslation(x, y).rotateRad(orientation)

    def matrix4: Matrix4 =
      new Matrix4().setToTranslation(x, y, 0).rotateRad(0, 0, 1, orientation)
  }

  /**
    * A frame of reference with a position, an orientation and a time offset.
    *
    * @param placement Its position and orientation.
    * @param timeOffset Its time offset.
    */
  case class Pose(placement: Placement, timeOffset: Time) {
    val x = placement.x
    val y = placement.y
    val point = placement.point
    val orientation = placement.orientation

    def translate(dx: Float, dy: Float): Pose =
      Pose(placement translate (dx, dy), timeOffset)

    def rotate(theta: Float): Pose =
      Pose(placement rotate theta, timeOffset)

    def forward(d: Float): Pose =
      Pose(placement forward d, timeOffset)

    def sideways(d: Float): Pose =
      Pose(placement sideways d, timeOffset)

    def delay(dt: Time): Pose =
      Pose(placement, timeOffset + dt)

    def towards (p: Point): Pose =
      Pose(placement towards p, timeOffset)

    def towards (s: Symbol): Pose =
      Pose(timeOffset = timeOffset, placement = s match {
        case 'player => placement towards CharacterPosition.at(timeOffset)
        case _ => placement towards s
      })

    def matrix3: Matrix3 = placement.matrix3
    def matrix4: Matrix4 = placement.matrix4
  }

  case class Rect[T](x: T, y: T, w: T, h: T)

}
