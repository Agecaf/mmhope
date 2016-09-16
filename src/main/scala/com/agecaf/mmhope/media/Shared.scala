package com.agecaf.mmhope.media

import com.agecaf.mmhope.core.Geometry.{Placement, Rect}
import com.badlogic.gdx.graphics.Color

/**
  * Contains the methods that will be shared to externals.
  *
  * Internally simply forwards it to Manager.
  */
object Shared {
  /**
    * Draw a texture at a placement.
    * @param textureId the ID of the texture to draw.
    * @param placement the placement to draw the texture.
    * @param targetRect the target rectangle (with respect to placement).
    * @param sourceRect the source rectangle.
    * @param flipX whether to flip horizontally. Defaults to true.
    * @param flipY whether to flip vertically. Defaults to false.
    */
  def draw(
      textureId: String,
      placement: Placement,
      targetRect: Rect[Float],
      sourceRect: Rect[Int],
      flipX: Boolean = false,
      flipY: Boolean = false): Unit =
    Manager.draw(textureId, placement, targetRect, sourceRect, flipX, flipY)

  /**
    * Renders text at placement given a font.
    * @param fontId the font to use.
    * @param text the text to render
    * @param placement where to render the text.
    * @param alpha the transparency to use.
    * @param tint An optional tint.
    */
  def text(fontId: String,
      text: String,
      placement: Placement,
      alpha: Float = 1,
      tint: Option[Color] = None): Unit =
    Manager.text(fontId, text, placement, alpha, tint)
}
