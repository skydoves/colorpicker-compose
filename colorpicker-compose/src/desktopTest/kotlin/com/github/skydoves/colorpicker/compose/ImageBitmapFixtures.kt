/*
 * Designed and developed by 2022 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.skydoves.colorpicker.compose

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.unit.IntSize

/**
 * A square bitmap split into four solid quadrants, so a test can tap a spot and name the color it
 * expects back.
 */
internal fun quadrantBitmap(
  side: Int = 4,
  topLeft: Color = Color.Red,
  topRight: Color = Color.Green,
  bottomLeft: Color = Color.Blue,
  bottomRight: Color = Color.Yellow,
): ImageBitmap = ImageBitmap.fromDrawing(IntSize(side, side)) {
  val half = side / 2f
  val full = side.toFloat()
  fill(Rect(0f, 0f, half, half), topLeft)
  fill(Rect(half, 0f, full, half), topRight)
  fill(Rect(0f, half, half, full), bottomLeft)
  fill(Rect(half, half, full, full), bottomRight)
}

/** A bitmap split down the middle, for checking how a non square palette is scaled into a canvas. */
internal fun horizontalSplitBitmap(
  width: Int,
  height: Int,
  left: Color,
  right: Color,
): ImageBitmap = ImageBitmap.fromDrawing(IntSize(width, height)) {
  val half = width / 2f
  fill(Rect(0f, 0f, half, height.toFloat()), left)
  fill(Rect(half, 0f, width.toFloat(), height.toFloat()), right)
}

internal fun solidBitmap(size: IntSize, color: Color): ImageBitmap = ImageBitmap.fromDrawing(size) {
  fill(Rect(0f, 0f, size.width.toFloat(), size.height.toFloat()), color)
}

private fun androidx.compose.ui.graphics.Canvas.fill(rect: Rect, color: Color) =
  drawRect(rect, Paint().apply { this.color = color })
