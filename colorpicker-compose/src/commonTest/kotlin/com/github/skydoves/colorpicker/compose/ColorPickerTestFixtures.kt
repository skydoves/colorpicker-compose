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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import kotlin.math.abs
import kotlin.math.min
import kotlin.test.assertTrue

/**
 * Wires [this] to the same hue/saturation mapping [HsvColorPicker] installs, without needing a
 * composition. Hue runs counter clockwise from the right of the canvas and saturation grows with
 * the distance from the center.
 */
internal fun ColorPickerController.setupHsvPalette(
  size: Size = Size(200f, 200f),
  initialColor: Color? = null,
) {
  canvasSize = size
  val center = size.center
  val radius = size.minDimension * 0.5f
  val initialPosition = initialColor
    ?.let {
      val (h, s, _) = it.toHSV()
      hsvToCoord(h, s, center)
    }
    ?: center
  setup(initialPosition) { point ->
    val vector = point - center
    val angle = vector.angle()
    val sat = min(vector.length() / radius, 1f)
    Color.hsv(angleToHue(angle), sat, 1f) to Offset.fromAngle(angle, sat * radius) + center
  }
}

/** Floats coming out of the hue/saturation round trip land a hair off, so compare with a margin. */
internal fun assertColorEquals(expected: Color, actual: Color, tolerance: Float = 0.01f) {
  val matches = abs(expected.red - actual.red) <= tolerance &&
    abs(expected.green - actual.green) <= tolerance &&
    abs(expected.blue - actual.blue) <= tolerance &&
    abs(expected.alpha - actual.alpha) <= tolerance
  assertTrue(matches, "expected $expected but was $actual")
}

internal fun assertOffsetEquals(expected: Offset, actual: Offset, tolerance: Float = 0.01f) {
  val matches = abs(expected.x - actual.x) <= tolerance && abs(expected.y - actual.y) <= tolerance
  assertTrue(matches, "expected $expected but was $actual")
}
