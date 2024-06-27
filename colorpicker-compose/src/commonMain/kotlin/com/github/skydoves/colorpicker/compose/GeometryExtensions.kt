package com.github.skydoves.colorpicker.compose

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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
internal inline fun Float.toRadians() = this * TO_RADIANS
internal inline fun Float.toDegrees() = this * TO_DEGREES

// These exist for Size but not IntSize
internal inline val IntSize.center get() = Offset(width * 0.5f, height * 0.5f)
internal inline operator fun IntSize.times(scale: Float) =
  IntSize((width * scale).roundToInt(), (height * scale).roundToInt())

// New extensions
internal inline val IntSize.radius get() = min(width, height) * 0.5f

internal inline val Offset.minCoordinate get() = min(x, y) // exists for Size but not Offset
internal inline fun Offset.distanceTo(other: Offset) = sqrt(
  (x - other.x) * (x - other.x) + (y - other.y) * (y - other.y)
)
internal inline fun Offset.midpoint(other: Offset) = (this + other) * 0.5f
internal inline fun Offset.length() = sqrt(x * x + y * y)
internal inline fun Offset.angle() = atan2(y, x)

internal inline fun Offset.roundToInt() = IntOffset(x.roundToInt(), y.roundToInt())

internal inline fun Offset.Companion.fromAngle(radians: Float, magnitude: Float) =
  Offset(cos(radians) * magnitude, sin(radians) * magnitude)
