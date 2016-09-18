
val ballSourceRect = Rect(0, 0, 20, 20)
val smallRect = Rect(-0.1, -0.1, 0.2, 0.2)

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
      false
    }
  )

val center = Placement(Point(0, 0), 0)

val mainBullet = Bullet(
  render = {(t: Time) =>
    ballBullet.render(center rotate (t * 0.2) forward (0.2))
  },
  isHitting = {(t: Time, pt: Point) =>
    false
  }
)


Level(
    name = "Level",
    duration = 10.0,
    background = {t => ()},
    bullet = mainBullet,
    assets = AssetSet(textures = Set("bullets"))
  )
