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

import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.click
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipe
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private const val SIDE = 200

/**
 * A palette rarely covers its whole canvas, and an image can carry holes. Neither has a color to
 * report, so the picker leaves the selection where it was.
 */
class ImagePaletteBoundsTest {

  @Test
  fun aTapInTheLetterboxBandIsIgnored() = runColorPickerUiTest {
    // A 4x2 palette scaled to fit a square canvas leaves a 50px band above and below it.
    val palette = horizontalSplitBitmap(4, 2, Color.Red, Color.Green)
    lateinit var controller: ColorPickerController
    setContent {
      controller = rememberColorPickerController()
      ImageColorPicker(
        modifier = Modifier.size(SIDE.dp).testTag("picker"),
        controller = controller,
        paletteImageBitmap = palette,
        paletteContentScale = PaletteContentScale.FIT,
      )
    }
    onNodeWithTag("picker").performTouchInput { click(Offset(50f, 100f)) }
    val before = controller.selectedPoint.value

    onNodeWithTag("picker").performTouchInput { click(Offset(150f, 10f)) }

    assertEquals(Color.Red, controller.selectedColor.value)
    assertEquals(before, controller.selectedPoint.value)
  }

  @Test
  fun aTapInTheLetterboxBandReportsNothing() = runColorPickerUiTest {
    val palette = horizontalSplitBitmap(4, 2, Color.Red, Color.Green)
    val envelopes = mutableListOf<ColorEnvelope>()
    setContent {
      ImageColorPicker(
        modifier = Modifier.size(SIDE.dp).testTag("picker"),
        controller = rememberColorPickerController(),
        paletteImageBitmap = palette,
        paletteContentScale = PaletteContentScale.FIT,
        onColorChanged = { envelopes += it },
      )
    }
    // Land on the red half first, so the band above the green half would be a visible change.
    onNodeWithTag("picker").performTouchInput { click(Offset(50f, 100f)) }
    val before = envelopes.size

    onNodeWithTag("picker").performTouchInput { click(Offset(150f, 10f)) }

    assertEquals(before, envelopes.size)
  }

  @Test
  fun aTapOnATransparentPixelIsIgnored() = runColorPickerUiTest {
    val palette = halfTransparentBitmap(side = 4, color = Color.Red)
    lateinit var controller: ColorPickerController
    setContent {
      controller = rememberColorPickerController()
      ImageColorPicker(
        modifier = Modifier.size(SIDE.dp).testTag("picker"),
        controller = controller,
        paletteImageBitmap = palette,
      )
    }
    onNodeWithTag("picker").performTouchInput { click(Offset(50f, 100f)) }

    onNodeWithTag("picker").performTouchInput { click(Offset(150f, 100f)) }

    assertEquals(Color.Red, controller.selectedColor.value)
  }

  @Test
  fun theFixtureReallyHasATransparentHalf() {
    val palette = halfTransparentBitmap(side = 4, color = Color.Red)

    assertEquals(Color.Red, palette.getPixel(0, 0))
    assertEquals(0f, palette.getPixel(3, 0).alpha)
  }

  @Test
  fun draggingOffThePaletteHoldsTheLastColorItCovered() = runColorPickerUiTest {
    val palette = horizontalSplitBitmap(4, 2, Color.Red, Color.Green)
    lateinit var controller: ColorPickerController
    setContent {
      controller = rememberColorPickerController()
      ImageColorPicker(
        modifier = Modifier.size(SIDE.dp).testTag("picker"),
        controller = controller,
        paletteImageBitmap = palette,
        paletteContentScale = PaletteContentScale.FIT,
      )
    }

    onNodeWithTag("picker").performTouchInput {
      swipe(start = Offset(150f, 100f), end = Offset(150f, 5f))
    }

    assertEquals(Color.Green, controller.selectedColor.value)
    assertTrue(
      controller.selectedPoint.value.y >= 50f,
      "the indicator left the palette at ${controller.selectedPoint.value}",
    )
  }

  @Test
  fun aTapInsideTheCroppedPaletteStillWorks() = runColorPickerUiTest {
    val palette = horizontalSplitBitmap(4, 2, Color.Red, Color.Green)
    lateinit var controller: ColorPickerController
    setContent {
      controller = rememberColorPickerController()
      ImageColorPicker(
        modifier = Modifier.size(SIDE.dp).testTag("picker"),
        controller = controller,
        paletteImageBitmap = palette,
        paletteContentScale = PaletteContentScale.CROP,
      )
    }

    // CROP fills the canvas, so every corner names a real pixel.
    onNodeWithTag("picker").performTouchInput { click(Offset(2f, 2f)) }

    assertEquals(Color.Red, controller.selectedColor.value)
  }
}
