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

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.click
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private const val LENGTH = 200
private const val THICKNESS = 40
private const val MIDDLE = THICKNESS / 2f

/** A slider on its own, with no picker beside it, which is how a grey gets picked. */
class StandaloneSliderTest {

  @Test
  fun aBrightnessSliderOnItsOwnReportsGreys() = runColorPickerUiTest {
    val envelopes = mutableListOf<ColorEnvelope>()
    setContent {
      BrightnessSlider(
        modifier = Modifier.width(LENGTH.dp).height(THICKNESS.dp).testTag("slider"),
        controller = rememberColorPickerController(),
        onColorChanged = { envelopes += it },
      )
    }

    onNodeWithTag("slider").performTouchInput { click(Offset(LENGTH / 2f, MIDDLE)) }
    waitForIdle()

    val picked = envelopes.last()
    assertTrue(picked.fromUser)
    assertEquals(ColorChangeSource.Tap, picked.source)
    assertColorEquals(Color(0xFF808080), picked.color, tolerance = 0.02f)
  }

  @Test
  fun aBrightnessSliderOnItsOwnPaintsAGreyRamp() = runColorPickerUiTest {
    setContent {
      BrightnessSlider(
        modifier = Modifier.width(LENGTH.dp).height(THICKNESS.dp).testTag("slider"),
        controller = rememberColorPickerController(),
      )
    }

    val image = onNodeWithTag("slider").captureToImage()

    // The track used to inherit the alpha of a controller with nothing selected, which is zero, so
    // the whole slider came out invisible. It also assumed full saturation and painted red.
    val dark = image.getPixel(10, MIDDLE.toInt())
    val light = image.getPixel(160, MIDDLE.toInt())
    assertEquals(1f, dark.alpha)
    assertEquals(1f, light.alpha)
    assertTrue(dark.red < 0.3f, "the dark end was $dark")
    assertTrue(light.red > 0.6f, "the light end was $light")
    assertTrue(abs(light.red - light.blue) < 0.05f, "the ramp was not grey, it was $light")
  }

  @Test
  fun anAlphaSliderOnItsOwnReportsChanges() = runColorPickerUiTest {
    val envelopes = mutableListOf<ColorEnvelope>()
    setContent {
      AlphaSlider(
        modifier = Modifier.width(LENGTH.dp).height(THICKNESS.dp).testTag("slider"),
        controller = rememberColorPickerController(),
        onColorChanged = { envelopes += it },
      )
    }

    onNodeWithTag("slider").performTouchInput { click(Offset(LENGTH / 2f, MIDDLE)) }
    waitForIdle()

    assertTrue(envelopes.isNotEmpty())
    assertTrue(abs(envelopes.last().color.alpha - 0.5f) < 0.02f)
  }

  @Test
  fun aSaturationSliderOnItsOwnReportsChanges() = runColorPickerUiTest {
    val envelopes = mutableListOf<ColorEnvelope>()
    lateinit var controller: ColorPickerController
    setContent {
      controller = rememberColorPickerController()
      SaturationSlider(
        modifier = Modifier.width(LENGTH.dp).height(THICKNESS.dp).testTag("slider"),
        controller = controller,
        onColorChanged = { envelopes += it },
      )
    }

    onNodeWithTag("slider").performTouchInput { click(Offset(LENGTH / 2f, MIDDLE)) }
    waitForIdle()

    assertTrue(envelopes.isNotEmpty())
    assertTrue(abs(controller.saturation.value - 0.5f) < 0.02f)
  }
}
