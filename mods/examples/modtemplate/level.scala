
val ballSourceRect = Rect(0, 0, 20, 20)
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

val center = Placement(Point(0, 0), 0)

val mainBullet = Bullet(
  render = {(t: Time) =>
    ballBullet.render(center rotate (t * 0.2) forward (0.2))
  },
  isHitting = {(t: Time, pt: Point) =>
    ballBullet.isHitting(center rotate (t * 0.2) forward (0.2), pt)
  }
)


Level(
    name = "Level",
    duration = 10.0,
    background = {t => ()},
    bullet = mainBullet,
    assets = AssetSet(textures = Set("bullets"))
  )
