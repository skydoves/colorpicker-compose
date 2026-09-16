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
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GeometryExtensionsTest {

  @Test
  fun degreesAndRadiansConvertBothWays() {
    assertTrue(abs(180f.toRadians() - PI_F) < 1e-5f)
    assertTrue(abs(PI_F.toDegrees() - 180f) < 1e-3f)
    assertTrue(abs(90f.toRadians().toDegrees() - 90f) < 1e-3f)
  }

  @Test
  fun intSizeCenterIsHalfOfEachSide() {
    assertEquals(Offset(50f, 30f), IntSize(100, 60).center)
  }

  @Test
  fun intSizeRadiusFollowsTheShorterSide() {
    assertEquals(30f, IntSize(100, 60).radius)
    assertEquals(30f, IntSize(60, 100).radius)
  }

  @Test
  fun intSizeScalesAndRounds() {
    assertEquals(IntSize(50, 30), IntSize(100, 60) * 0.5f)
    assertEquals(IntSize(33, 20), IntSize(100, 60) * 0.333f)
  }

  @Test
  fun minCoordinateTakesTheSmallerAxis() {
    assertEquals(3f, Offset(3f, 7f).minCoordinate)
    assertEquals(3f, Offset(7f, 3f).minCoordinate)
  }

  @Test
  fun distanceToMeasuresAStraightLine() {
    assertEquals(5f, Offset.Zero.distanceTo(Offset(3f, 4f)))
    assertEquals(0f, Offset(2f, 2f).distanceTo(Offset(2f, 2f)))
  }

  @Test
  fun midpointSitsHalfwayBetweenTwoPoints() {
    assertEquals(Offset(5f, 10f), Offset.Zero.midpoint(Offset(10f, 20f)))
  }

  @Test
  fun lengthIsTheDistanceFromTheOrigin() {
    assertEquals(5f, Offset(3f, 4f).length())
  }

  @Test
  fun angleIsMeasuredCounterClockwiseFromTheXAxis() {
    assertEquals(0f, Offset(1f, 0f).angle())
    assertTrue(abs(Offset(0f, 1f).angle() - PI_F / 2f) < 1e-5f)
    assertTrue(abs(abs(Offset(-1f, 0f).angle()) - PI_F) < 1e-5f)
  }

  @Test
  fun roundToIntRoundsBothAxes() {
    assertEquals(IntOffset(3, 5), Offset(2.6f, 4.5f).roundToInt())
    assertEquals(IntOffset(2, 4), Offset(2.4f, 4.4f).roundToInt())
  }

  @Test
  fun fromAngleBuildsAVectorOfTheGivenLength() {
    val point = Offset.fromAngle((PI / 2).toFloat(), 10f)
    assertTrue(abs(point.x) < 1e-5f, "expected no horizontal component but was ${point.x}")
    assertTrue(abs(point.y - 10f) < 1e-5f, "expected the full length but was ${point.y}")
    assertTrue(abs(point.length() - 10f) < 1e-5f)
  }

  @Test
  fun fromAngleAndAngleAreInverses() {
    for (degrees in 0 until 360 step 30) {
      val radians = degrees.toFloat().toRadians()
      val recovered = Offset.fromAngle(radians, 5f).angle()
      val delta = abs(recovered - radians).let { if (it > PI_F) 2 * PI_F - it else it }
      assertTrue(delta < 1e-4f, "angle $radians came back as $recovered")
    }
  }
}
