
// Rectangles used to define our static bullet.
val ballSourceRect = Rect(40, 20, 20, 20)
val smallRect = Rect(-0.02, -0.02, 0.04, 0.04)

// Define our static bullet.
val diamond: StaticBullet =
StaticBullet(
  render = {(p: Placement) =>
    g.draw(
      textureId = "bullets",
      placement = p,
      targetRect = smallRect,
      sourceRect = ballSourceRect
    )
  },
  isHitting = {(p: Placement, pt: Point) =>
    distance(p.point, pt) < 0.001
  }
)

val origin = Pose(Placement(Point(0, 0.4), 0), 0)

/** A bullet which moves sort of in a curve, then straight.
  *
  * It uses the bezier movement for that, as it is a smooth and easy to use movement!
  */
def squiggly(p: Pose): Bullet = {
  // Live for 10s after pose.
  lifespan(10)(p) {

    val p1 = p rotate -0.2
    val p2 = p forward 0.3 rotate 0.4 delay 1.5
    val p3 = p forward 0.8 rotate -0.4 delay 6

    // It has 3 unrelated steps.
    groupList(

      // First Part,
      before(p2) {
        // diamond      -- The static bullet (shape, hitbox) to use.
        // withMovement -- The simple movement to use.
        // bezier       -- Interpolates from pose p1 to p2,
        //                 with inital speed 0.0, final speed 0.2.
        diamond withMovement bezier(0.0, 0.2)(p1, p2)
      },

      // Second Part.
      after(p2) {
        before(p3) {
          diamond withMovement bezier(0.2, 0.15)(p2, p3)
        }
      },

      // Third Part.
      after(p3) {
        diamond withMovement linear(0.15)(p3)
      }
    )
  }
}

/** A circle of squiggly bullets!
  */
def circle(n: Int)(p: Pose): Bullet =
  group {
    for (i <- 1 to n) yield {
      val angle = Tau / n * i
      squiggly(p rotate angle)
    }
  }


def mainBullet(p: Pose): Bullet =
  group {
    for (i <- 1 to 40) yield {
      circle(20)(p delay i*0.5 rotate i*0.1)
    }
  }

Level(
  name = "Bezier",
  duration = 10.0,
  music = "emotional",
  background = {t => ()},
  bullet = mainBullet(origin),
  assets = AssetSet(textures = Set("bullets"), music = Set("emotional"))
)
