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

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.unit.IntSize

internal inline fun Canvas.drawImage(image: ImageBitmap, topLeft: Offset = Offset.Zero) {
  drawImage(image, topLeft, emptyPaint)
}

internal inline fun Canvas.drawImageCenterAt(image: ImageBitmap, center: Offset = Offset.Zero) {
  drawImage(image, center - image.size.center)
}

internal inline fun Canvas.drawRect(size: IntSize, paint: Paint) {
  drawRect(0f, 0f, size.width.toFloat(), size.height.toFloat(), paint)
}

internal inline fun Canvas.drawRoundRect(size: IntSize, radius: Float, paint: Paint) {
  drawRoundRect(0f, 0f, size.width.toFloat(), size.height.toFloat(), radius, radius, paint)
}

internal val emptyPaint = Paint()
