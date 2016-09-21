package com.agecaf.mmhope.modloading

import com.agecaf.mmhope.core.BulletTypes.{Bullet, RenderAtTime}
import com.badlogic.gdx.graphics.Color

import scala.language.implicitConversions



object Data {

  case class AssetSet(
    fonts: Set[String] = Set(),
    textures: Set[String] = Set(),
    sounds: Set[String] = Set(),
    music: Set[String] = Set()
  ) {
    def ++ (other: AssetSet): AssetSet =
      AssetSet(
        fonts ++ other.fonts,
        textures ++ other.textures,
        sounds ++ other.sounds,
        music ++ other.music
      )
    def -- (other: AssetSet): AssetSet =
      AssetSet(
        fonts -- other.fonts,
        textures -- other.textures,
        sounds -- other.sounds,
        music -- other.music
      )
    def | (other: AssetSet): AssetSet =
      AssetSet(
        fonts | other.fonts,
        textures | other.textures,
        sounds | other.sounds,
        music | other.music
      )
    def & (other: AssetSet): AssetSet =
      AssetSet(
        fonts & other.fonts,
        textures & other.textures,
        sounds & other.sounds,
        music & other.music
      )
    def &~ (other: AssetSet): AssetSet =
      AssetSet(
        fonts &~ other.fonts,
        textures &~ other.textures,
        sounds &~ other.sounds,
        music &~ other.music
      )
  }

  case class Level(
    name: String,
    duration: Float,
    music: String,
    background: RenderAtTime,
    bullet: Bullet,
    assets: AssetSet
  )
}

object InternalData {

  case class Font(
    path: String,
    borderWidth: Int = 0,
    borderColor: String = "black",
    borderStraight: Boolean = false,
    shadowOffsetX: Int = 0,
    shadowOffsetY: Int = 0,
    shadowColor: String = "black-0.75"
  )

  case class RGB(r: Float, g: Float, b: Float) {
    def clamp = RGB (
      r = if (r > 0) if (r < 1) r else 1 else 0,
      g = if (g > 0) if (g < 1) g else 1 else 0,
      b = if (b > 0) if (b < 1) b else 1 else 0
    )

    def + (other: RGB) = RGB(
      r + other.r,
      g + other.g,
      b + other.b
    ).clamp

    def - (other: RGB) = RGB(
      r - other.r,
      g - other.g,
      b - other.b
    ).clamp

    def * (other: RGB) = RGB(
      r * other.r,
      g * other.g,
      b * other.b
    ).clamp

    def withAlpha (alpha: Float) = new Color(r, g, b, alpha)
  }

  implicit def RGBtoColor(c: RGB): Color = c.withAlpha(1)
}
