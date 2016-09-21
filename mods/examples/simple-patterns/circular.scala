
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

// A circle of bullets moving outwards.
def circlePattern(n: Int, speed: Float)(p: Pose): Bullet =
  lifespan(20)(p) {
    group {
      for (i <- 0 until n) yield {
        val ang = 2 * Pi * i / n
        val pose = p rotate ang
        diamond withMovement linear(speed)(pose)
      }
    }
  }

// A group of three circles. If n is odd they will be out of phase.
def threeCircles(n: Int, speed: Float)(p: Pose): Bullet =
  groupList(
    circlePattern(n, speed)(p),
    circlePattern(n, speed)(p delay 0.2 rotate Pi),
    circlePattern(n, speed)(p delay 0.4)
  )

// A spiral with n branches, m bullets.
def spiral(n: Int, speed: Float, m: Int)(pose: Pose): Bullet =
  group {
    for (j <- 0 to m) yield {
      val p = pose delay (0.15 * j) rotate (0.08 * j)
      circlePattern(n, speed)(p)
    }
  }

// Rays of many consecutive bullets.
def rays(n: Int, speed: Float, m: Int)(pose: Pose): Bullet =
  group {
    for (j <- 0 to m) yield {
      val p = pose delay (0.05 * j)
      circlePattern(n, speed)(p)
    }
  }

def bulletPart(p: Pose): Bullet =
  groupList(
    threeCircles(29, 0.2)(p),
    spiral(5, 0.25, 20)(p delay 1),
    rays(15, 0.4, 5)(p delay 2)
  )

def mainBullet =
  group {
    for (i <- 0 to 10) yield {
      bulletPart(origin delay (i * 3) rotate i)
    }
  }

Level(
  name = "Circular Patterns",
  duration = 10.0,
  music = "emotional",
  background = {t => ()},
  bullet = mainBullet,
  assets = AssetSet(textures = Set("bullets"), music = Set("emotional"))
)
