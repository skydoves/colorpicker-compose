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

internal val Color.hexCode: String
  inline get() {
    val a: Int = (alpha * 255).toInt()
    val r: Int = (red * 255).toInt()
    val g: Int = (green * 255).toInt()
    val b: Int = (blue * 255).toInt()
    return a.hex + r.hex + g.hex + b.hex
  }

private inline val Int.hex get() = this.toString(16).padStart(2, '0')

internal fun Color.toHSV(): Triple<Float, Float, Float> {
  val cmax = maxOf(red, green, blue)
  val cmin = minOf(red, green, blue)
  val diff = cmax - cmin

  val h = if (diff == 0f) 0.0f
  else if (cmax == red) (60 * ((green - blue) / diff) + 360f) % 360f
  else if (cmax == green) (60 * ((blue - red) / diff) + 120f) % 360f
  else /*if (cmax == blue)*/ (60 * ((red - green) / diff) + 240f) % 360f
  val s = if (cmax == 0f) 0f else diff / cmax
  val v = cmax

  return Triple(h, s, v)
}

/** Converts an HS(V) color to a coordinate on the hue/saturation circle. */
internal inline fun hsvToCoord(h: Float, s: Float, center: Offset) =
  Offset.fromAngle(hueToAngle(h), s * center.minCoordinate) + center

internal inline fun angleToHue(angle: Float) = (-angle.toDegrees() + 360f) % 360f
internal inline fun hueToAngle(hue: Float) = -hue.toRadians()
