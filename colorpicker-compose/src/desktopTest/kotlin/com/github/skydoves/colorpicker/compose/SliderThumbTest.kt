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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
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

/** The slider is 200x40 and the thumb has the default 12dp radius, at a density of 1. */
private const val LENGTH = 200
private const val THICKNESS = 40
private const val THUMB_RADIUS = 12
private const val MIDDLE = THICKNESS / 2f

class SliderThumbTest {

  @Test
  fun theThumbStaysInsideTheTrackAtTheEnd() = runColorPickerUiTest {
    lateinit var controller: ColorPickerController
    setContent {
      controller = rememberColorPickerController()
      Column {
        HsvColorPicker(
          modifier = Modifier.size(200.dp).testTag("picker"),
          controller = controller,
          initialColor = Color.Red,
        )
        AlphaSlider(
          modifier = Modifier.width(LENGTH.dp).height(THICKNESS.dp).testTag("slider"),
          controller = controller,
        )
      }
    }

    onNodeWithTag("slider").performTouchInput { click(Offset(LENGTH - 1f, MIDDLE)) }

    // The thumb center belongs a radius in from the end, so this pixel is under it. It used to sit
    // on the very edge with half of it clipped away, leaving the gradient showing here.
    val pixel = onNodeWithTag("slider").captureToImage()
      .getPixel(LENGTH - THUMB_RADIUS * 2 + 4, MIDDLE.toInt())
    assertColorEquals(Color.White, pixel, tolerance = 0.05f)
  }

  @Test
  fun theThumbStaysInsideTheTrackAtTheStart() = runColorPickerUiTest {
    setContent {
      HueSlider(
        modifier = Modifier.width(LENGTH.dp).height(THICKNESS.dp).testTag("slider"),
        controller = rememberColorPickerController(),
      )
    }

    onNodeWithTag("slider").performTouchInput { click(Offset(0f, MIDDLE)) }

    val pixel = onNodeWithTag("slider").captureToImage()
      .getPixel(THUMB_RADIUS * 2 - 4, MIDDLE.toInt())
    assertColorEquals(Color.White, pixel, tolerance = 0.05f)
  }

  @Test
  fun theTrackRunsFromOneThumbRadiusToTheOther() = runColorPickerUiTest {
    lateinit var controller: ColorPickerController
    setContent {
      controller = rememberColorPickerController()
      AlphaSlider(
        modifier = Modifier.width(LENGTH.dp).height(THICKNESS.dp).testTag("slider"),
        controller = controller,
      )
    }

    onNodeWithTag("slider").performTouchInput { click(Offset(THUMB_RADIUS.toFloat(), MIDDLE)) }
    assertEquals(0f, controller.alpha.value)

    onNodeWithTag("slider").performTouchInput {
      click(Offset(LENGTH - THUMB_RADIUS.toFloat(), MIDDLE))
    }
    assertEquals(1f, controller.alpha.value)
  }

  @Test
  fun theMiddleOfTheTrackIsStillTheMiddleOfTheRange() = runColorPickerUiTest {
    lateinit var controller: ColorPickerController
    setContent {
      controller = rememberColorPickerController()
      AlphaSlider(
        modifier = Modifier.width(LENGTH.dp).height(THICKNESS.dp).testTag("slider"),
        controller = controller,
      )
    }

    onNodeWithTag("slider").performTouchInput { click(Offset(LENGTH / 2f, MIDDLE)) }

    assertTrue(
      abs(controller.alpha.value - 0.5f) < 0.02f,
      "expected the middle of the range but was ${controller.alpha.value}",
    )
  }
}
