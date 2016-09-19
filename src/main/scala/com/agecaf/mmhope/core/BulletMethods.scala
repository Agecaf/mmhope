package com.agecaf.mmhope.core

import Geometry._
import BulletTypes._
import scala.language.postfixOps

/**
  * Methods used to create, join and transform bullets.
  */
object BulletMethods {

  /**
    * Groups several bullets into a superbullet.
    *
    * Rendering the superbullet renders all its internal bullets.
    * Asking whether the superbullet is hitting is the same as
    * asking if any of its internal bullets are hitting.
    *
    * @param bullets the internal bullets to group.
    * @return the superbullet of joined bullets.
    */
  def group(bullets: Seq[Bullet]): Bullet =
    Bullet(
      render = {(t) =>
        bullets foreach (_ render t)
      },
      isHitting = {(t, p) =>
        bullets exists (_ isHitting (t, p))
      }
    )

  def after(pose: Pose)(bullet: Bullet): Bullet =
    Bullet(
      render = {(t) =>
        if (t > pose.timeOffset) bullet render t
      },
      isHitting = {(t, p) =>
        if (t > pose.timeOffset) bullet isHitting (t, p) else false
      }
    )

  def before(pose: Pose)(bullet: Bullet): Bullet =
    Bullet(
      render = {(t) =>
        if (t < pose.timeOffset) bullet render t
      },
      isHitting = {(t, p) =>
        if (t < pose.timeOffset) bullet isHitting (t, p) else false
      }
    )



  // TODO implement fade-ins, fade-outs
  /*def fade(alphaMultiplier: Float)(bullet: Bullet) =
    bullet.copy(render = {(t) => })*/

  // TODO zone limiters

  // TODO generators

  // Movements.
  def linear(speed: Float)(pose: Pose) : Movement = (t: Time) =>
    pose forward ((t - pose.timeOffset) * speed) placement

  def linearFunc(f: Float => Float)(speed: Float)(pose: Pose): Movement = (t: Time) =>
    pose forward f((t - pose.timeOffset) * speed) placement

  def linearInterpolation(from: Pose, to: Pose): Movement = (t: Time) =>
    from
      .towards (to.point)
      .forward (distance(from.point, to.point) * (t - from.timeOffset) / (to.timeOffset - from.timeOffset))
      .placement

  def bezier(fromSpeed: Float, toSpeed: Float)(from: Pose, to: Pose): Movement = {(t: Time) =>
    val tt = (t - from.timeOffset) / (to.timeOffset - from.timeOffset)
    val mt = 1 - tt
    val p0 = from.point.toVec2
    val p1 = (from forward fromSpeed).point.toVec2
    val p2 = (to forward -toSpeed).point.toVec2
    val p3 = to.point.toVec2
    val pt = (p0 * (mt*mt*mt)) + (p1 * (3*mt*mt*tt)) + (p2 * (3*mt*tt*tt)) + (p3 * (tt*tt*tt))
    val or = ((p1 - p0) * (3*mt*mt)) + ((p2 - p1) * (6*mt*tt)) + ((p3 - p2) * (3*tt*tt))
    Placement(pt.toPoint, or.orientation)
  }

  val nullBullet: Bullet =
    Bullet(
      render = {t => ()},
      isHitting = {(t, p) => false}
    )
}
