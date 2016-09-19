new Character("Template") {

  val assets = AssetSet(textures = Set("bullets.png"))

  def render(alphaMultiplier: Float): Unit = {

    // Check whether we're in a level.
    levelOpt match {
      case Some(level) =>
        level.background.apply(t)
        level.bullet.render(t - 1.2)
        level.bullet.render(t - 0.1)
        level.bullet.render(t)

      case None =>
    }

    // Draw Player
    g.draw(
      textureId = "bullets",
      placement = playerPosition,
      targetRect = smallRect,
      sourceRect = ballSourceRect
    )
  }

  def logic(dt: Float): Unit = {
    // Increase time.
    t += dt

    // Move player.
    if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) playerPosition = playerPosition sideways speed*dt
    if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) playerPosition = playerPosition sideways -speed*dt
    if(Gdx.input.isKeyPressed(Input.Keys.UP)) playerPosition = playerPosition forward speed*dt
    if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) playerPosition = playerPosition forward -speed*dt

    // Check if we're in a level.
    levelOpt match {
      case Some(level) =>
        // Check if it is hitting.
        if (level.bullet isHitting (t, playerPosition.point)) { println("OUCH!") }
      case None => ()
    }
  }

  var playerPosition: Placement = Placement(Point(0, 0), Pi / 2)
  var t: Time = 0

  val ballSourceRect = Rect(0, 0, 20, 20)
  val smallRect = Rect(-0.01, -0.01, 0.02, 0.02)
  val speed = 0.3
}
