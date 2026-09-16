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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.click
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private const val PICKER = 200
private const val LENGTH = 200
private const val THICKNESS = 40
private val NEAR_START = Offset(20f, THICKNESS / 2f)
private val RIGHT_OF_WHEEL = Offset(PICKER - 1f, PICKER / 2f)

/**
 * A slider used to tell the controller it was there and never that it had gone, so a slider hidden
 * behind a switch kept its last value folded into every color the picker reported afterwards.
 */
class SliderAttachmentTest {

  @Test
  fun aBrightnessSliderThatLeavesReleasesItsHoldOnTheColor() = runColorPickerUiTest {
    var showSlider by mutableStateOf(true)
    val controller = pickerWithOptionalSlider({ showSlider }) { c ->
      BrightnessSlider(sliderModifier(), c)
    }
    onNodeWithTag("slider").performTouchInput { click(NEAR_START) }
    waitForIdle()
    assertTrue(controller.selectedColor.value.red < 0.2f, "the slider never darkened the color")

    showSlider = false
    waitForIdle()

    assertFalse(controller.isAttachedBrightnessSlider)
    assertColorEquals(Color.White, controller.selectedColor.value)
  }

  @Test
  fun aPickerKeepsItsOwnBrightnessOnceTheSliderIsGone() = runColorPickerUiTest {
    var showSlider by mutableStateOf(true)
    val controller = pickerWithOptionalSlider({ showSlider }) { c ->
      BrightnessSlider(sliderModifier(), c)
    }
    onNodeWithTag("slider").performTouchInput { click(NEAR_START) }
    waitForIdle()
    showSlider = false
    waitForIdle()

    onNodeWithTag("picker").performTouchInput { click(RIGHT_OF_WHEEL) }

    assertColorEquals(Color.Red, controller.selectedColor.value, tolerance = 0.02f)
  }

  @Test
  fun anAlphaSliderThatLeavesGivesTheAlphaBack() = runColorPickerUiTest {
    var showSlider by mutableStateOf(true)
    val controller = pickerWithOptionalSlider({ showSlider }) { c ->
      AlphaSlider(sliderModifier(), c)
    }
    onNodeWithTag("slider").performTouchInput { click(NEAR_START) }
    waitForIdle()
    assertTrue(controller.selectedColor.value.alpha < 0.2f, "the slider never cleared the alpha")

    showSlider = false
    waitForIdle()

    assertFalse(controller.isAttachedAlphaSlider)
    assertEquals(1f, controller.alpha.value)
    assertEquals(1f, controller.selectedColor.value.alpha)
  }

  @Test
  fun aSaturationSliderThatLeavesReleasesItsHoldOnTheColor() = runColorPickerUiTest {
    var showSlider by mutableStateOf(true)
    val controller = pickerWithOptionalSlider({ showSlider }) { c ->
      SaturationSlider(sliderModifier(), c)
    }
    onNodeWithTag("picker").performTouchInput { click(RIGHT_OF_WHEEL) }
    onNodeWithTag("slider").performTouchInput { click(NEAR_START) }
    waitForIdle()
    assertTrue(controller.selectedColor.value.green > 0.7f, "the slider never washed out the color")

    showSlider = false
    waitForIdle()

    assertFalse(controller.isAttachedSaturationSlider)
    assertColorEquals(Color.Red, controller.selectedColor.value, tolerance = 0.02f)
  }

  @Test
  fun oneSliderLeavingDoesNotSpeakForTheOtherOfItsKind() = runColorPickerUiTest {
    var showSecond by mutableStateOf(true)
    lateinit var controller: ColorPickerController
    setContent {
      controller = rememberColorPickerController()
      Column {
        HsvColorPicker(Modifier.size(PICKER.dp).testTag("picker"), controller)
        BrightnessSlider(sliderModifier("first"), controller)
        if (showSecond) BrightnessSlider(sliderModifier("second"), controller)
      }
    }

    showSecond = false
    waitForIdle()

    assertTrue(
      controller.isAttachedBrightnessSlider,
      "the surviving slider was cut off from the controller",
    )
  }

  @Test
  fun theSurvivingSliderStillReachesTheColor() = runColorPickerUiTest {
    var showSecond by mutableStateOf(true)
    lateinit var controller: ColorPickerController
    setContent {
      controller = rememberColorPickerController()
      Column {
        HsvColorPicker(Modifier.size(PICKER.dp).testTag("picker"), controller)
        BrightnessSlider(sliderModifier("first"), controller)
        if (showSecond) BrightnessSlider(sliderModifier("second"), controller)
      }
    }
    showSecond = false
    waitForIdle()
    onNodeWithTag("first").performTouchInput { click(NEAR_START) }
    waitForIdle()

    // Picking again is what asks the controller which factors still apply.
    onNodeWithTag("picker").performTouchInput { click(RIGHT_OF_WHEEL) }
    waitForIdle()

    assertTrue(
      controller.selectedColor.value.red < 0.2f,
      "the surviving slider stopped reaching the color, which came out ${controller.selectedColor.value}",
    )
  }

  @Test
  fun bringingASliderBackDoesNotJumpTheColor() = runColorPickerUiTest {
    var showSlider by mutableStateOf(true)
    val controller = pickerWithOptionalSlider({ showSlider }) { c ->
      BrightnessSlider(sliderModifier(), c)
    }
    onNodeWithTag("slider").performTouchInput { click(NEAR_START) }
    waitForIdle()
    showSlider = false
    waitForIdle()
    val afterRemoval = controller.selectedColor.value

    showSlider = true
    waitForIdle()

    assertEquals(afterRemoval, controller.selectedColor.value)
  }

  /** A picker with a slider under it that a switch can take away. */
  private fun ComposeUiTest.pickerWithOptionalSlider(
    visible: () -> Boolean,
    slider: @Composable (ColorPickerController) -> Unit,
  ): ColorPickerController {
    lateinit var controller: ColorPickerController
    setContent {
      controller = rememberColorPickerController()
      Column {
        HsvColorPicker(Modifier.size(PICKER.dp).testTag("picker"), controller)
        if (visible()) slider(controller)
      }
    }
    return controller
  }
}

private fun sliderModifier(tag: String = "slider") =
  Modifier.width(LENGTH.dp).height(THICKNESS.dp).testTag(tag)
