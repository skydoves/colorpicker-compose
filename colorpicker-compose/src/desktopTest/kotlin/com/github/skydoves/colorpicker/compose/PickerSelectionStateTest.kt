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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.click
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

private const val SIDE = 200

/** Regressions for the ways a picker used to lose, or never take, the selection it was given. */
class PickerSelectionStateTest {

  @Test
  fun aPickerDisabledBeforeItsFirstLayoutStillDraws() = runColorPickerUiTest {
    lateinit var controller: ColorPickerController
    setContent {
      controller = rememberColorPickerController()
      controller.enabled = false
      HsvColorPicker(
        modifier = Modifier.size(SIDE.dp).testTag("picker"),
        controller = controller,
      )
    }

    // Drawing the wheel at an unspecified offset is what used to take the app down on launch.
    onNodeWithTag("picker").captureToImage()

    assertTrue(
      controller.selectedPoint.value.isSpecified,
      "the indicator sat at ${controller.selectedPoint.value}",
    )
  }

  @Test
  fun aDisabledPickerStillIgnoresTaps() = runColorPickerUiTest {
    lateinit var controller: ColorPickerController
    setContent {
      controller = rememberColorPickerController()
      controller.enabled = false
      HsvColorPicker(
        modifier = Modifier.size(SIDE.dp).testTag("picker"),
        controller = controller,
      )
    }
    val before = controller.selectedColor.value

    onNodeWithTag("picker").performTouchInput { click(Offset(199f, 100f)) }

    assertEquals(before, controller.selectedColor.value)
  }

  @Test
  fun aPaletteRebuiltOnEveryRecompositionKeepsTheSelection() = runColorPickerUiTest {
    lateinit var controller: ColorPickerController
    var preview by mutableStateOf(Color.Transparent)
    setContent {
      controller = rememberColorPickerController()
      Column {
        ImageColorPicker(
          modifier = Modifier.size(SIDE.dp).testTag("picker"),
          controller = controller,
          // A fresh instance every recomposition, which is what reading a Bitmap inline does.
          paletteImageBitmap = quadrantBitmap(),
          onColorChanged = { preview = it.color },
        )
        Box(Modifier.size(20.dp).background(preview))
      }
    }

    onNodeWithTag("picker").performTouchInput { click(Offset(150f, 50f)) }
    waitForIdle()

    assertEquals(Color.Green, controller.selectedColor.value)
    assertOffsetEquals(Offset(150f, 50f), controller.selectedPoint.value, tolerance = 1f)
  }

  @Test
  fun anInitialColorKeepsItsBrightness() = runColorPickerUiTest {
    lateinit var controller: ColorPickerController
    setContent {
      controller = rememberColorPickerController()
      HsvColorPicker(
        modifier = Modifier.size(SIDE.dp).testTag("picker"),
        controller = controller,
        initialColor = Color(0xFF000080),
      )
    }

    assertColorEquals(Color(0xFF000080), controller.selectedColor.value, tolerance = 0.02f)
  }

  @Test
  fun anInitialColorKeepsItsAlpha() = runColorPickerUiTest {
    lateinit var controller: ColorPickerController
    setContent {
      controller = rememberColorPickerController()
      Column {
        HsvColorPicker(
          modifier = Modifier.size(SIDE.dp).testTag("picker"),
          controller = controller,
          initialColor = Color.Red.copy(alpha = 0.5f),
        )
        AlphaSlider(modifier = Modifier.size(SIDE.dp, 30.dp), controller = controller)
      }
    }

    assertColorEquals(Color.Red.copy(alpha = 0.5f), controller.selectedColor.value, 0.02f)
  }

  @Test
  fun aColorChosenBeforeTheFirstLayoutIsAppliedOnceThePickerIsReady() = runColorPickerUiTest {
    lateinit var controller: ColorPickerController
    setContent {
      controller = rememberColorPickerController()
      LaunchedEffect(Unit) { controller.selectByColor(Color.Cyan, fromUser = false) }
      HsvColorPicker(
        modifier = Modifier.size(SIDE.dp).testTag("picker"),
        controller = controller,
      )
    }
    waitForIdle()

    assertColorEquals(Color.Cyan, controller.selectedColor.value, tolerance = 0.02f)
  }

  @Test
  fun aNewWheelBitmapIsDrawn() = runColorPickerUiTest {
    var wheelColor by mutableStateOf(Color.Blue)
    lateinit var controller: ColorPickerController
    setContent {
      controller = rememberColorPickerController()
      HsvColorPicker(
        modifier = Modifier.size(SIDE.dp).testTag("picker"),
        controller = controller,
        wheelImageBitmap = solidBitmap(IntSize(40, 40), wheelColor),
      )
    }
    val before = onNodeWithTag("picker").captureToImage().getPixel(100, 100)

    wheelColor = Color.Green
    waitForIdle()
    val after = onNodeWithTag("picker").captureToImage().getPixel(100, 100)

    assertEquals(Color.Blue, before)
    assertEquals(Color.Green, after)
    assertNotEquals(before, after)
  }

  @Test
  fun theControllerHoldsTheWheelBitmapItWasLastGiven() = runColorPickerUiTest {
    val first = solidBitmap(IntSize(40, 40), Color.Blue)
    val second = solidBitmap(IntSize(40, 40), Color.Green)
    var wheel by mutableStateOf(first)
    lateinit var controller: ColorPickerController
    setContent {
      controller = rememberColorPickerController()
      HsvColorPicker(
        modifier = Modifier.size(SIDE.dp).testTag("picker"),
        controller = controller,
        wheelImageBitmap = wheel,
      )
    }
    assertSame(first, controller.wheelBitmap)

    wheel = second
    waitForIdle()

    assertSame(second, controller.wheelBitmap)
  }
}
