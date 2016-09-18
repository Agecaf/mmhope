package com.agecaf.mmhope.core

import com.agecaf.mmhope.core.Geometry._

import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import scala.language.postfixOps
/**
  * Keeps a record of the position of the character throughout
  * a level.
  */
object CharacterPosition {

  var lastTime: Time = 0
  var lastPos: Point = Point(0, 0)
  var lastTimeInt: Int = 0
  var firstPoint: Point = Point(0, 0)

  var pastPositionLists: ArrayBuffer[List[(Time, Point)]] = ArrayBuffer()
  var currentSecond: List[(Time, Point)] = Nil

  /** Add a new (time, pos) pair to our structure, and
    *
    * @param t the time.
    * @param p the point.
    */
  def update(t: Time, p: Point): Unit = {
    if (t > lastTime) {
      val i: Int = t.floor.toInt
      if (i > lastTimeInt) {
        pastPositionLists(lastTimeInt) = currentSecond
        lastTimeInt = i
        currentSecond = List()
      }
      currentSecond = (t, p) +: currentSecond
      lastTime = t
      lastPos = p
    } else {
      // Rewriting history!
      val i: Int = t.floor.toInt
      if (i == lastTimeInt) {
        currentSecond = (t, p) +: (currentSecond dropWhile (_._1 > t))
      } else {
        currentSecond = (t, p) +: (pastPositionLists(i) dropWhile (_._1 > t))
        for (j <- i until lastTimeInt) { pastPositionLists(j) = Nil }
        lastTimeInt = i
      }
      lastTime = t
      lastPos = p
    }
  }

  /** Get the character's position at a point in time.
    *
    * @param t the time.
    * @return the position of the character.
    */
  def getAt(t: Time): Point = {
    // Get the current one if they want info from the future.
    if (t > lastTime) return lastPos

    var i: Int = t.floor.toInt
    while (i >= 0) {
      if (i != lastTimeInt) {
        pastPositionLists(i) find (_._1 < t) map (_._2) match {
          case Some(pos) => return pos
          case None => i -= 1
        }
      } else {
        currentSecond find (_._1 < t) map (_._2) match {
          case Some(pos) => return pos
          case None => i -= 1
        }
      }
    }
    firstPoint
  }

  /** Reset the initial positions.
    *
    * @param p
    */
  def reset(p: Point): Unit = {
    firstPoint = p

    lastTime = 0
    lastPos = p
    lastTimeInt = 0

    pastPositionLists = ArrayBuffer()
    currentSecond = Nil
  }

  /** Saves the data to a list.
    *
    * @return
    */
  def save: List[(Time, Point)] = {
    val buffer = ListBuffer[(Time, Point)]()
    for (i <- 0 until lastTimeInt) {
      buffer ++= pastPositionLists(i).reverse
    }
    buffer ++= currentSecond.reverse
    buffer.toList
  }

  /** Loads the data from a list.
    *
    * @param data
    */
  def load(data: List[(Time, Point)]) = {
    for ((t, p) <- data) update(t, p)
  }
}
