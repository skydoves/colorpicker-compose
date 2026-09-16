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
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ColorExtensionsTest {

  @Test
  fun toHsvReadsTheHueOfEveryPrimaryAndSecondary() {
    assertEquals(0f, Color.Red.toHSV().first)
    assertEquals(60f, Color.Yellow.toHSV().first)
    assertEquals(120f, Color.Green.toHSV().first)
    assertEquals(180f, Color.Cyan.toHSV().first)
    assertEquals(240f, Color.Blue.toHSV().first)
    assertEquals(300f, Color.Magenta.toHSV().first)
  }

  @Test
  fun toHsvReportsFullSaturationAndValueForPrimaries() {
    val (_, s, v) = Color.Red.toHSV()
    assertEquals(1f, s)
    assertEquals(1f, v)
  }

  @Test
  fun toHsvReportsNoSaturationForGreys() {
    assertEquals(Triple(0f, 0f, 1f), Color.White.toHSV())
    assertEquals(Triple(0f, 0f, 0f), Color.Black.toHSV())

    val (h, s, v) = Color(0xFF808080).toHSV()
    assertEquals(0f, h)
    assertEquals(0f, s)
    assertTrue(abs(v - 0.5019608f) < 1e-5f, "expected a mid value but was $v")
  }

  @Test
  fun toHsvKeepsTheValueOfADarkColor() {
    val (h, s, v) = Color(0xFF000080).toHSV()
    assertEquals(240f, h)
    assertEquals(1f, s)
    assertTrue(abs(v - 0.5019608f) < 1e-5f, "expected a halved value but was $v")
  }

  @Test
  fun hexCodeIsArgbAndAlwaysEightCharacters() {
    assertEquals("ffff0000", Color.Red.hexCode)
    assertEquals("ff00ff00", Color.Green.hexCode)
    assertEquals("ff0000ff", Color.Blue.hexCode)
    assertEquals("ff000000", Color.Black.hexCode)
    assertEquals("00000000", Color.Transparent.hexCode)
    assertEquals("80ff0000", Color(0x80FF0000).hexCode)
  }

  @Test
  fun angleToHueAndHueToAngleAreInverses() {
    for (hue in 0 until 360 step 15) {
      val roundTripped = angleToHue(hueToAngle(hue.toFloat()))
      assertTrue(
        abs(roundTripped - hue) < 1e-3f,
        "hue $hue came back as $roundTripped",
      )
    }
  }

  @Test
  fun hsvToCoordPutsRedToTheRightOfTheCenter() {
    val center = Offset(100f, 100f)
    val point = hsvToCoord(h = 0f, s = 1f, center = center)
    assertTrue(abs(point.x - 200f) < 1e-3f, "expected the right edge but was ${point.x}")
    assertTrue(abs(point.y - 100f) < 1e-3f, "expected the vertical middle but was ${point.y}")
  }

  @Test
  fun hsvToCoordPutsCyanToTheLeftOfTheCenter() {
    val center = Offset(100f, 100f)
    val point = hsvToCoord(h = 180f, s = 1f, center = center)
    assertTrue(abs(point.x) < 1e-3f, "expected the left edge but was ${point.x}")
    assertTrue(abs(point.y - 100f) < 1e-3f, "expected the vertical middle but was ${point.y}")
  }

  @Test
  fun hsvToCoordCollapsesToTheCenterWhenSaturationIsZero() {
    val center = Offset(100f, 100f)
    assertEquals(center, hsvToCoord(h = 210f, s = 0f, center = center))
  }

  @Test
  fun hsvToCoordScalesWithSaturation() {
    val center = Offset(100f, 100f)
    val half = hsvToCoord(h = 0f, s = 0.5f, center = center)
    assertTrue(abs(half.x - 150f) < 1e-3f, "expected half the radius but was ${half.x}")
  }
}
