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
