/** This is a very basic example. You can use any complex function!
  * These are just some really dumb ones as an example.
  *
  * These provide quite a lot of flexibility if you learn your complex
  * functions ;), or at least is a cooler way to visualize them.
  *
  * Note you can also separate Real (forward) and Imaginary (sideways)
  * parts if you want, but you probably should use of the more fancy functions.
  *
  */


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

val origin = Pose(Placement(Point(0, 0.4), 0), 0) towards 'SW


// Some complex functions.
def exp1(offset: Float)(z: Complex[Float]): Complex[Float] = {
  (z + offset * I).exp * 0.2
}

def exp2(offset: Float)(z: Complex[Float]): Complex[Float] = {
  (offset + z * I).exp * 0.2
}

def exp3(offset: Float)(z: Complex[Float]): Complex[Float] = {
  (z * (1 + I) + offset * (1 - I)).exp * 0.2
}


def sin1(offset: Float)(z: Complex[Float]): Complex[Float] = {
  (z + offset * I).sin * 0.2
}

def sin2(offset: Float)(z: Complex[Float]): Complex[Float] = {
  (offset + z * I).sin * 0.2
}

def sin3(offset: Float)(z: Complex[Float]): Complex[Float] = {
  (z * (1 + I) + offset * (1 - I)).sin * 0.2
}


def log1(offset: Float)(z: Complex[Float]): Complex[Float] = {
  (z + offset * I + 1).log * 0.2
}


def complexGroup(f: Float => Complex[Float] => Complex[Float])(p: Pose): Bullet = {
  lifespan(10)(p) {
    group {
      for (i <- -5 to 5) yield {
        diamond withMovement fromComplex(f(i*0.1))(p)
      }
    }
  }
}

def mainBullet(p: Pose): Bullet = group {
  List[Float => Complex[Float] => Complex[Float]](
      exp1, exp2, exp3,
      sin1, sin2, sin3,
      log1)
    .zipWithIndex
    .map {case (f, i) => complexGroup(f)(p delay i*3) }
}

Level(
  name = "Complex Maps",
  duration = 10.0,
  music = "emotional",
  background = {t => ()},
  bullet = mainBullet(origin),
  assets = AssetSet(textures = Set("bullets"), music = Set("emotional"))
)
