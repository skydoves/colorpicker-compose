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

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize

/** Gets the pixel color at the given position. */
internal fun ImageBitmap.getPixel(x: Int, y: Int): Color {
  val buffer = IntArray(1)
  readPixels(buffer, x, y, 1, 1)
  return Color(buffer[0])
}

internal inline fun ImageBitmap.getPixel(point: IntOffset): Color =
  getPixel(point.x, point.y)

internal inline val ImageBitmap.size get() = IntSize(width, height)

internal inline fun ImageBitmap.Companion.fromDrawing(size: IntSize, draw: Canvas.() -> Unit) =
  ImageBitmap(size.width, size.height, ImageBitmapConfig.Argb8888).also { Canvas(it).draw() }

internal fun ImageBitmap.Companion.fromPaint(paint: Paint, size: IntSize) =
  ImageBitmap.fromDrawing(size) { drawRect(size, paint) }
