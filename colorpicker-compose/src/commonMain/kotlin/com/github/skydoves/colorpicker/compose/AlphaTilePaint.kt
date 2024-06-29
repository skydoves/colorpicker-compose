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
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.TileMode

/**
 * AlphaTilePaint displays ARGB colors including transparency with tiles on a canvas.
 *
 * @param tileSize DP size of tiles.
 * @param tileOddColor Color of the odd tiles.
 * @param tileEvenColor Color of the even tiles.
 */
internal fun alphaTilePaint(
  tileSize: Float,
  tileOddColor: Color,
  tileEvenColor: Color,
): Paint {
  val size = tileSize.toInt()
  val imageBitmap = ImageBitmap(size * 2, size * 2, ImageBitmapConfig.Argb8888)
  val canvas = Canvas(imageBitmap)
  val rect = Rect(0f, 0f, tileSize, tileSize)

  val paint = Paint().apply {
    style = PaintingStyle.Fill
    isAntiAlias = true
  }

  paint.color = tileOddColor
  canvas.drawRect(rect, paint)
  canvas.drawRect(rect.translate(tileSize, tileSize), paint)

  paint.color = tileEvenColor
  canvas.drawRect(rect.translate(0f, tileSize), paint)
  canvas.drawRect(rect.translate(tileSize, 0f), paint)

  return Paint().apply {
    isAntiAlias = true
    shader = ImageShader(
      imageBitmap,
      TileMode.Repeated,
      TileMode.Repeated,
    )
  }
}
