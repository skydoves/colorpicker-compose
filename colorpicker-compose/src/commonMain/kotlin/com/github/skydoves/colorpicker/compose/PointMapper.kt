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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import kotlin.math.min
import kotlin.math.sqrt

/**
 * PointMapper calculates correct coordinates corresponding to bitmap ratio and size.
 */
internal object PointMapper {
  internal fun getColorPoint(controller: ColorPickerController, point: Offset): Offset {
    val size = IntSize(100, 100) // controller.canvasSize.value
    return approximatedPoint(controller, point, size.center)
  }

  private fun approximatedPoint(
    controller: ColorPickerController,
    start: Offset,
    end: Offset,
  ): Offset {
    if (start.distanceTo(end) <= 3f) return end
    val center = start.midpoint(end)
    val color = Color.Transparent // controller.extractPixelColor(center)
    return if (color == Color.Transparent) {
      approximatedPoint(controller, center, end)
    } else {
      approximatedPoint(controller, start, center)
    }
  }
}
