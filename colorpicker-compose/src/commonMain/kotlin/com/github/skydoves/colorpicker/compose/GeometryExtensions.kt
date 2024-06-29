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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

internal const val PI_F = PI.toFloat()
internal const val TO_DEGREES = 180f / PI_F
internal const val TO_RADIANS = PI_F / 180f
internal fun Float.toRadians() = this * TO_RADIANS
internal fun Float.toDegrees() = this * TO_DEGREES

// These exist for Size but not IntSize
internal val IntSize.center get() = Offset(width * 0.5f, height * 0.5f)
internal operator fun IntSize.times(scale: Float) =
  IntSize((width * scale).roundToInt(), (height * scale).roundToInt())

// New extensions
internal val IntSize.radius get() = min(width, height) * 0.5f

internal val Offset.minCoordinate get() = min(x, y) // exists for Size but not Offset
internal fun Offset.distanceTo(other: Offset) = sqrt(
  (x - other.x) * (x - other.x) + (y - other.y) * (y - other.y),
)

internal fun Offset.midpoint(other: Offset) = (this + other) * 0.5f
internal fun Offset.length() = sqrt(x * x + y * y)
internal fun Offset.angle() = atan2(y, x)

internal fun Offset.roundToInt() = IntOffset(x.roundToInt(), y.roundToInt())

internal fun Offset.Companion.fromAngle(radians: Float, magnitude: Float) =
  Offset(cos(radians) * magnitude, sin(radians) * magnitude)
