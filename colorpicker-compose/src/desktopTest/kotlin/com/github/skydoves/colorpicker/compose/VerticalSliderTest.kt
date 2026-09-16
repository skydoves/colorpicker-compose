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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.click
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipe
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private const val LENGTH = 200
private const val THICKNESS = 40
private const val MIDDLE = THICKNESS / 2f
private const val THUMB_RADIUS = 12

class VerticalSliderTest {

  @Test
  fun theBottomOfAVerticalSliderIsTheLowestValue() = runColorPickerUiTest {
    lateinit var controller: ColorPickerController
    setContent {
      controller = rememberColorPickerController()
      BrightnessSlider(
        modifier = Modifier.width(THICKNESS.dp).height(LENGTH.dp).testTag("slider"),
        controller = controller,
        orientation = SliderOrientation.Vertical,
      )
    }

    onNodeWithTag("slider").performTouchInput { click(Offset(MIDDLE, LENGTH - 1f)) }

    assertEquals(0f, controller.brightness.value)
  }

  @Test
  fun theTopOfAVerticalSliderIsTheHighestValue() = runColorPickerUiTest {
    lateinit var controller: ColorPickerController
    setContent {
      controller = rememberColorPickerController()
      BrightnessSlider(
        modifier = Modifier.width(THICKNESS.dp).height(LENGTH.dp).testTag("slider"),
        controller = controller,
        orientation = SliderOrientation.Vertical,
      )
    }
    onNodeWithTag("slider").performTouchInput { click(Offset(MIDDLE, LENGTH - 1f)) }

    onNodeWithTag("slider").performTouchInput { click(Offset(MIDDLE, 0f)) }

    assertEquals(1f, controller.brightness.value)
  }

  @Test
  fun theMiddleOfAVerticalSliderIsTheMiddleOfTheRange() = runColorPickerUiTest {
    lateinit var controller: ColorPickerController
    setContent {
      controller = rememberColorPickerController()
      AlphaSlider(
        modifier = Modifier.width(THICKNESS.dp).height(LENGTH.dp).testTag("slider"),
        controller = controller,
        orientation = SliderOrientation.Vertical,
      )
    }

    onNodeWithTag("slider").performTouchInput { click(Offset(MIDDLE, LENGTH / 2f)) }

    assertTrue(
      abs(controller.alpha.value - 0.5f) < 0.02f,
      "expected the middle of the range but was ${controller.alpha.value}",
    )
  }

  @Test
  fun aVerticalDragMovesTheValue() = runColorPickerUiTest {
    lateinit var controller: ColorPickerController
    setContent {
      controller = rememberColorPickerController()
      BrightnessSlider(
        modifier = Modifier.width(THICKNESS.dp).height(LENGTH.dp).testTag("slider"),
        controller = controller,
        orientation = SliderOrientation.Vertical,
      )
    }

    onNodeWithTag("slider").performTouchInput {
      swipe(start = Offset(MIDDLE, 20f), end = Offset(MIDDLE, 180f))
    }

    assertTrue(
      controller.brightness.value < 0.2f,
      "dragging to the bottom should have lowered it but it was ${controller.brightness.value}",
    )
  }

  @Test
  fun theThumbOfAVerticalSliderSitsAtTheBottomForTheLowestValue() = runColorPickerUiTest {
    setContent {
      BrightnessSlider(
        modifier = Modifier.width(THICKNESS.dp).height(LENGTH.dp).testTag("slider"),
        controller = rememberColorPickerController(),
        orientation = SliderOrientation.Vertical,
      )
    }

    onNodeWithTag("slider").performTouchInput { click(Offset(MIDDLE, LENGTH - 1f)) }

    val pixel = onNodeWithTag("slider").captureToImage()
      .getPixel(MIDDLE.toInt(), LENGTH - THUMB_RADIUS * 2 + 4)
    assertColorEquals(androidx.compose.ui.graphics.Color.White, pixel, tolerance = 0.05f)
  }
}
