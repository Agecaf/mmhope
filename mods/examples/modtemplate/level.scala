
val ballSourceRect = Rect(20, 0, 20, 20)
val smallRect = Rect(-0.01, -0.01, 0.02, 0.02)

val ballBullet: StaticBullet =
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
        (p.x - pt.x) * (p.x - pt.x) + (p.y - pt.y) * (p.y - pt.y) < 0.001
    }
  )

def strange(p: Pose) =
  lifespan(30)(p) {
    group {
      for (i <- 1 to 40) yield {
        val p2 = p rotate i forward 0.3
        groupList (
          before(p2 delay 3) {
            updating {
              ballBullet withMovement bezier(0.5, 0.1)(p, p2 towards 'player delay 3)
            }
          },
          after(p2 delay 3) {
            updating {
              ballBullet withMovement linear(0.2)(p2 towards 'player delay 3)
            }
          }
        )
      }
    }
  }


val origin = Pose(Placement(Point(0, 0), 0), 0)

def mainBullet = group {
  for (j <- 1 to 200) yield {
    val p = origin delay j rotate j
    strange(p)
  }
}



Level(
    name = "Level",
    duration = 10.0,
    background = {t => ()},
    bullet = mainBullet,
    assets = AssetSet(textures = Set("bullets"))
  )
