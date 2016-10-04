
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

/** A bullet aimed towards the Player, starting from the position and time
  * defined by the pose. The direction is towards where the player was
  * at the pose timeOffset.
  */
def towardsPlayer(p: Pose): Bullet = {
  // Live for 10s after the pose.
  lifespan(10)(p){

    // Make sure to update the player's position.
    updating {

      // diamond              --- The static bullet to use (shape, hitbox).
      // withMovement         --- Define a movement for our bullet.
      // linear               --- The basic movement type, with configurable
      //                          speed and initial position.
      // "p towards 'player"  --- A special direction to aim for the player. Other
      //                          special directions include cardinal directions
      //                          ('NE, 'S, etc.)
      diamond withMovement linear(0.4 /* speed */)(p towards 'player /* Pose */)
    }
  }
}

/** A short burst of targetted bullets towards the player!
  */
def burst(n: Int)(p: Pose): Bullet = {
  lifespan(30)(p){
    group {
      for (i <- 1 to n) yield {
        towardsPlayer(p delay i*0.1)
      }
    }
  }
}

/** A circle of bullets following the player, then closing in.
  * You'll remember this from Flowey and Junko ;)
  *
  * 8 Bullets.
  * starts following from second -1, lauches at second 0.
  * 0.4 circle radius.
  */
def deathCircle(p: Pose): Bullet = {
  lifespan(10)(p delay -1) {
    updating {
      // Track the position of the player!
      val center = playerAtPose(p)

      group {
        for (i <- 1 to 8) yield {
          val angle = Tau / 8 * i

          groupList(
            before(p) {
              diamond withMovement static(center rotate angle forward 0.4 rotate Pi+0.2)
            },
            after(p) {
              diamond withMovement linear(0.3)(center rotate angle forward 0.4 rotate Pi+0.2)
            }
          )

        }
      }
    }
  }
}

def mainBullet(p: Pose): Bullet = {
  group {
    for (i <- 1 to 30) yield {
      groupList(
        deathCircle(p delay i),
        burst(3)(p delay i)
      )
    }
  }
}

Level(
  name = "Target",
  duration = 40.0,
  music = "emotional",
  background = {t => ()},
  bullet = mainBullet(origin),
  assets = AssetSet(textures = Set("bullets"), music = Set("emotional"))
)
