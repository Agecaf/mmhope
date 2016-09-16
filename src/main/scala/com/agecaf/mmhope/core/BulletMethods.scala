package com.agecaf.mmhope.core

import Geometry._
import BulletTypes._

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
  def fade(alphaMultiplier: Float)(bullet: Bullet) =
    bullet.copy(render = {(t) => })

  // TODO zone limiters

  // TODO generators

  val nullBullet: Bullet =
    Bullet(
      render = {t => ()},
      isHitting = {(t, p) => false}
    )
}
