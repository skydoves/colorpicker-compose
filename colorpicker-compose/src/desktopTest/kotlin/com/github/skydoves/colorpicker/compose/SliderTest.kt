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
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.click
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipe
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** The sliders are 200px wide and 30px tall in the test scene, which runs at a density of 1. */
private const val WIDTH = 200
private const val HEIGHT = 30
private val CENTER = Offset(WIDTH / 2f, HEIGHT / 2f)
private val LEFT_END = Offset(0f, HEIGHT / 2f)

class SliderTest {

  @Test
  fun everySliderRenders() = runColorPickerUiTest {
    setContent {
      val controller = rememberColorPickerController()
      Column {
        AlphaSlider(modifier = sliderModifier("alpha"), controller = controller)
        BrightnessSlider(modifier = sliderModifier("brightness"), controller = controller)
        HueSlider(modifier = sliderModifier("hue"), controller = controller)
        SaturationSlider(modifier = sliderModifier("saturation"), controller = controller)
      }
    }

    onNodeWithTag("alpha").assertExists()
    onNodeWithTag("brightness").assertExists()
    onNodeWithTag("hue").assertExists()
    onNodeWithTag("saturation").assertExists()
  }

  @Test
  fun tappingTheMiddleOfTheAlphaSliderHalvesTheAlpha() = runColorPickerUiTest {
    val controller = withPickerAndSlider { AlphaSlider(sliderModifier("slider"), it) }

    onNodeWithTag("slider").performTouchInput { click(CENTER) }

    assertNear(0.5f, controller.alpha.value)
  }

  @Test
  fun tappingTheStartOfTheAlphaSliderClearsTheAlpha() = runColorPickerUiTest {
    val controller = withPickerAndSlider { AlphaSlider(sliderModifier("slider"), it) }

    onNodeWithTag("slider").performTouchInput { click(LEFT_END) }

    assertEquals(0f, controller.alpha.value)
  }

  @Test
  fun tappingTheMiddleOfTheBrightnessSliderHalvesTheBrightness() = runColorPickerUiTest {
    val controller = withPickerAndSlider { BrightnessSlider(sliderModifier("slider"), it) }

    onNodeWithTag("slider").performTouchInput { click(CENTER) }

    assertNear(0.5f, controller.brightness.value)
  }

  @Test
  fun tappingTheStartOfTheBrightnessSliderTurnsTheColorBlack() = runColorPickerUiTest {
    val controller = withPickerAndSlider { BrightnessSlider(sliderModifier("slider"), it) }

    onNodeWithTag("slider").performTouchInput { click(LEFT_END) }

    assertEquals(0f, controller.brightness.value)
    assertColorEquals(Color.Black, controller.selectedColor.value)
  }

  @Test
  fun tappingTheMiddleOfTheSaturationSliderHalvesTheSaturation() = runColorPickerUiTest {
    val controller = withPickerAndSlider { SaturationSlider(sliderModifier("slider"), it) }

    onNodeWithTag("slider").performTouchInput { click(CENTER) }

    assertNear(0.5f, controller.saturation.value)
  }

  @Test
  fun tappingTheMiddleOfTheHueSliderLandsOnCyan() = runColorPickerUiTest {
    val controller = withPickerAndSlider { HueSlider(sliderModifier("slider"), it) }

    onNodeWithTag("slider").performTouchInput { click(CENTER) }

    assertColorEquals(Color.Cyan, controller.selectedColor.value, tolerance = 0.02f)
  }

  @Test
  fun draggingTowardsTheEndRaisesTheValue() = runColorPickerUiTest {
    val controller = withPickerAndSlider { AlphaSlider(sliderModifier("slider"), it) }
    onNodeWithTag("slider").performTouchInput { click(LEFT_END) }

    onNodeWithTag("slider").performTouchInput {
      swipe(start = LEFT_END, end = Offset(WIDTH * 0.8f, HEIGHT / 2f))
    }

    assertTrue(
      controller.alpha.value > 0.5f,
      "expected the drag to raise the alpha but it was ${controller.alpha.value}",
    )
  }

  @Test
  fun onStartAndOnFinishBracketADrag() = runColorPickerUiTest {
    var started = 0
    var finished = 0
    setContent {
      BrightnessSlider(
        modifier = sliderModifier("slider"),
        controller = rememberColorPickerController(),
        onStart = { started++ },
        onFinish = { finished++ },
      )
    }

    onNodeWithTag("slider").performTouchInput {
      swipe(start = LEFT_END, end = Offset(WIDTH * 0.8f, HEIGHT / 2f))
    }

    assertEquals(1, started)
    assertEquals(1, finished)
  }

  /** Composes a red picker with [slider] underneath and hands back the shared controller. */
  private fun ComposeUiTest.withPickerAndSlider(
    slider: @Composable (ColorPickerController) -> Unit,
  ): ColorPickerController {
    lateinit var controller: ColorPickerController
    setContent {
      controller = rememberColorPickerController()
      Column {
        HsvColorPicker(
          modifier = Modifier.size(200.dp).testTag("picker"),
          controller = controller,
          initialColor = Color.Red,
        )
        slider(controller)
      }
    }
    onNodeWithTag("picker").performTouchInput { click(Offset(199f, 100f)) }
    return controller
  }
}

private fun sliderModifier(tag: String) = Modifier.width(WIDTH.dp).height(HEIGHT.dp).testTag(tag)

private fun assertNear(expected: Float, actual: Float, tolerance: Float = 0.02f) = assertTrue(
  abs(expected - actual) <= tolerance,
  "expected $expected but was $actual",
)
