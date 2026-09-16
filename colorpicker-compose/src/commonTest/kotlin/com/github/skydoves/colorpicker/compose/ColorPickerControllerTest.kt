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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ColorPickerControllerTest {

  private fun TestScope.collectEnvelopes(controller: ColorPickerController): List<ColorEnvelope> =
    mutableListOf<ColorEnvelope>().also { sink ->
      backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
        controller.getColorFlow().toList(sink)
      }
    }

  @Test
  fun selectingTheRightEdgeOfThePalettePicksRed() = runTest {
    val controller = ColorPickerController()
    controller.setupHsvPalette()

    controller.selectByCoordinate(Offset(200f, 100f), fromUser = true)

    assertColorEquals(Color.Red, controller.selectedColor.value)
  }

  @Test
  fun selectingTheLeftEdgeOfThePalettePicksCyan() = runTest {
    val controller = ColorPickerController()
    controller.setupHsvPalette()

    controller.selectByCoordinate(Offset(0f, 100f), fromUser = true)

    assertColorEquals(Color.Cyan, controller.selectedColor.value)
  }

  @Test
  fun selectingTheCenterOfThePalettePicksWhite() = runTest {
    val controller = ColorPickerController()
    controller.setupHsvPalette()

    controller.selectCenter(fromUser = true)

    assertColorEquals(Color.White, controller.selectedColor.value)
    assertOffsetEquals(Offset(100f, 100f), controller.selectedPoint.value)
  }

  @Test
  fun aPointOutsideTheWheelIsSnappedOntoItsEdge() = runTest {
    val controller = ColorPickerController()
    controller.setupHsvPalette()

    controller.selectByCoordinate(Offset(400f, 100f), fromUser = true)

    assertOffsetEquals(Offset(200f, 100f), controller.selectedPoint.value)
    assertColorEquals(Color.Red, controller.selectedColor.value)
  }

  @Test
  fun selectByColorRoundTripsAHueThroughTheCoordinateSpace() = runTest {
    val controller = ColorPickerController()
    controller.setupHsvPalette()

    controller.selectByColor(Color.Magenta, fromUser = false)

    assertColorEquals(Color.Magenta, controller.selectedColor.value)
  }

  @Test
  fun selectByHsvAcceptsAFloatArray() = runTest {
    val controller = ColorPickerController()
    controller.setupHsvPalette()

    controller.selectByHsv(floatArrayOf(120f, 1f, 1f), alpha = 1f, fromUser = false)

    assertColorEquals(Color.Green, controller.selectedColor.value)
  }

  @Test
  fun setAlphaAppliesToTheSelectedColor() = runTest {
    val controller = ColorPickerController()
    controller.setupHsvPalette()
    controller.selectByCoordinate(Offset(200f, 100f), fromUser = false)

    controller.setAlpha(0.5f, fromUser = true)

    assertColorEquals(Color.Red.copy(alpha = 0.5f), controller.selectedColor.value)
  }

  @Test
  fun setBrightnessDarkensTheSelectedColor() = runTest {
    val controller = ColorPickerController()
    controller.setupHsvPalette()
    controller.selectByCoordinate(Offset(200f, 100f), fromUser = false)

    controller.setBrightness(0.5f, fromUser = true)

    assertColorEquals(Color(0xFF800000), controller.selectedColor.value)
  }

  @Test
  fun setSaturationWashesOutTheSelectedColor() = runTest {
    val controller = ColorPickerController()
    controller.setupHsvPalette()
    controller.selectByCoordinate(Offset(200f, 100f), fromUser = false)

    controller.setSaturation(0f, fromUser = true)

    assertColorEquals(Color.White, controller.selectedColor.value)
  }

  @Test
  fun setHueRotatesTheSelectedColor() = runTest {
    val controller = ColorPickerController()
    controller.setupHsvPalette()

    controller.setHue(240f / 360f, fromUser = true)

    assertColorEquals(Color.Blue, controller.selectedColor.value)
  }

  @Test
  fun theColorFlowReportsEveryChangeWithItsSource() = runTest {
    val controller = ColorPickerController()
    controller.setupHsvPalette()
    val envelopes = collectEnvelopes(controller)

    controller.selectByCoordinate(Offset(200f, 100f), true, ColorChangeSource.Tap)
    controller.selectByCoordinate(Offset(100f, 0f), true, ColorChangeSource.Drag)

    assertEquals(ColorChangeSource.Tap, envelopes[envelopes.lastIndex - 1].source)
    assertEquals(ColorChangeSource.Drag, envelopes.last().source)
    assertTrue(envelopes.last().fromUser)
  }

  @Test
  fun theColorFlowCarriesTheHexCodeOfTheSelectedColor() = runTest {
    val controller = ColorPickerController()
    controller.setupHsvPalette()
    val envelopes = collectEnvelopes(controller)

    controller.selectByCoordinate(Offset(200f, 100f), fromUser = true)

    assertEquals("ffff0000", envelopes.last().hexCode)
  }

  @Test
  fun programmaticChangesAreNotReportedAsComingFromTheUser() = runTest {
    val controller = ColorPickerController()
    controller.setupHsvPalette()
    val envelopes = collectEnvelopes(controller)

    controller.selectByCoordinate(Offset(200f, 100f), fromUser = false)

    assertFalse(envelopes.last().fromUser)
    assertEquals(ColorChangeSource.Programmatic, envelopes.last().source)
  }

  @Test
  fun reselectingTheSameColorDoesNotEmitAgain() = runTest {
    val controller = ColorPickerController()
    controller.setupHsvPalette()
    val envelopes = collectEnvelopes(controller)
    controller.selectByCoordinate(Offset(200f, 100f), fromUser = true)
    val count = envelopes.size

    controller.selectByCoordinate(Offset(200f, 100f), fromUser = true)

    assertEquals(count, envelopes.size)
  }

  @Test
  fun aDisabledControllerIgnoresSelection() = runTest {
    val controller = ColorPickerController()
    controller.setupHsvPalette()
    controller.selectCenter(fromUser = false)
    controller.enabled = false

    controller.selectByCoordinate(Offset(200f, 100f), fromUser = true)

    assertColorEquals(Color.White, controller.selectedColor.value)
  }

  @Test
  fun aDisabledControllerStartsAcceptingSelectionAgainOnceReenabled() = runTest {
    val controller = ColorPickerController()
    controller.setupHsvPalette()
    controller.enabled = false
    controller.selectByCoordinate(Offset(200f, 100f), fromUser = true)

    controller.enabled = true
    controller.selectByCoordinate(Offset(200f, 100f), fromUser = true)

    assertColorEquals(Color.Red, controller.selectedColor.value)
  }

  @Test
  fun growingTheCanvasKeepsTheSelectionInTheSameRelativeSpot() = runTest {
    val controller = ColorPickerController()
    val palette = controller.setupHsvPalette()
    controller.selectByCoordinate(Offset(200f, 100f), fromUser = false)

    palette.resize(Size(400f, 400f))

    assertOffsetEquals(Offset(400f, 200f), controller.selectedPoint.value)
  }

  @Test
  fun aNonUniformResizeKeepsTheIndicatorOnTheWheel() = runTest {
    val controller = ColorPickerController()
    val palette = controller.setupHsvPalette()
    controller.selectByCoordinate(Offset(200f, 100f), fromUser = false)

    palette.resize(Size(400f, 200f))

    // Scaling each axis on its own would have parked it at (400, 100), well off a wheel that now
    // has a radius of 100 around (200, 100).
    assertOffsetEquals(Offset(300f, 100f), controller.selectedPoint.value)
  }

  @Test
  fun theFirstCanvasSizeNeverLeavesAnUnspecifiedPoint() = runTest {
    val controller = ColorPickerController()

    controller.canvasSize = Size(200f, 200f)

    assertTrue(
      controller.selectedPoint.value.isSpecified,
      "the indicator sat at ${controller.selectedPoint.value}",
    )
  }

  @Test
  fun aSecondSetupLeavesTheCurrentSelectionAlone() = runTest {
    val controller = ColorPickerController()
    val palette = controller.setupHsvPalette()
    controller.selectByCoordinate(Offset(200f, 100f), fromUser = true)

    palette.attach(Size(200f, 200f), initialColor = null)

    assertColorEquals(Color.Red, controller.selectedColor.value)
    assertOffsetEquals(Offset(200f, 100f), controller.selectedPoint.value)
  }

  @Test
  fun aColorChosenBeforeSetupIsAppliedWhenThePickerArrives() = runTest {
    val controller = ColorPickerController()

    controller.selectByColor(Color.Cyan, fromUser = false)
    controller.setupHsvPalette()

    assertColorEquals(Color.Cyan, controller.selectedColor.value)
  }

  @Test
  fun theDebounceDurationIsReadBack() = runTest {
    val controller = ColorPickerController()

    controller.debounceDuration = 300L

    assertEquals(300L, controller.debounceDuration)
  }
}
