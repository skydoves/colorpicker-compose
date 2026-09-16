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
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private const val SIDE = 200

class ImageColorPickerTest {

  @Test
  fun thePickerRenders() = runColorPickerUiTest {
    val palette = quadrantBitmap()
    setContent {
      ImageColorPicker(
        modifier = Modifier.size(SIDE.dp).testTag("picker"),
        controller = rememberColorPickerController(),
        paletteImageBitmap = palette,
      )
    }

    onNodeWithTag("picker").assertExists()
  }

  @Test
  fun tappingAQuadrantPicksThatQuadrantsColor() = runColorPickerUiTest {
    val palette = quadrantBitmap()
    lateinit var controller: ColorPickerController
    setContent {
      controller = rememberColorPickerController()
      ImageColorPicker(
        modifier = Modifier.size(SIDE.dp).testTag("picker"),
        controller = controller,
        paletteImageBitmap = palette,
      )
    }

    onNodeWithTag("picker").performTouchInput { click(Offset(50f, 50f)) }
    assertEquals(Color.Red, controller.selectedColor.value)

    onNodeWithTag("picker").performTouchInput { click(Offset(150f, 50f)) }
    assertEquals(Color.Green, controller.selectedColor.value)

    onNodeWithTag("picker").performTouchInput { click(Offset(50f, 150f)) }
    assertEquals(Color.Blue, controller.selectedColor.value)

    onNodeWithTag("picker").performTouchInput { click(Offset(150f, 150f)) }
    assertEquals(Color.Yellow, controller.selectedColor.value)
  }

  @Test
  fun fitLetterboxesAWidePaletteAndStillPicksTheRightHalves() = runColorPickerUiTest {
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
    assertEquals(Color.Red, controller.selectedColor.value)

    onNodeWithTag("picker").performTouchInput { click(Offset(150f, 100f)) }
    assertEquals(Color.Green, controller.selectedColor.value)
  }

  @Test
  fun cropFillsTheCanvasWithAWidePalette() = runColorPickerUiTest {
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

    onNodeWithTag("picker").performTouchInput { click(Offset(10f, 100f)) }
    assertEquals(Color.Red, controller.selectedColor.value)

    onNodeWithTag("picker").performTouchInput { click(Offset(190f, 100f)) }
    assertEquals(Color.Green, controller.selectedColor.value)
  }

  @Test
  fun aTapIsReportedThroughOnColorChanged() = runColorPickerUiTest {
    val palette = quadrantBitmap()
    val envelopes = mutableListOf<ColorEnvelope>()
    setContent {
      ImageColorPicker(
        modifier = Modifier.size(SIDE.dp).testTag("picker"),
        controller = rememberColorPickerController(),
        paletteImageBitmap = palette,
        onColorChanged = { envelopes += it },
      )
    }

    // The palette centers on the bottom right quadrant, so tap a different one to get an event:
    // the controller drops a selection that lands on the color already showing.
    onNodeWithTag("picker").performTouchInput { click(Offset(150f, 50f)) }
    waitForIdle()

    assertTrue(envelopes.last().fromUser)
    assertEquals(ColorChangeSource.Tap, envelopes.last().source)
    assertEquals("ff00ff00", envelopes.last().hexCode)
  }

  @Test
  fun settingAPaletteOnTheControllerReplacesTheOne() = runColorPickerUiTest {
    val palette = quadrantBitmap()
    val magenta = quadrantBitmap(
      topLeft = Color.Magenta,
      topRight = Color.Magenta,
      bottomLeft = Color.Magenta,
      bottomRight = Color.Magenta,
    )
    lateinit var controller: ColorPickerController
    setContent {
      controller = rememberColorPickerController()
      ImageColorPicker(
        modifier = Modifier.size(SIDE.dp).testTag("picker"),
        controller = controller,
        paletteImageBitmap = palette,
      )
    }

    controller.setPaletteImageBitmap(magenta)
    waitForIdle()
    onNodeWithTag("picker").performTouchInput { click(Offset(50f, 50f)) }

    assertEquals(Color.Magenta, controller.selectedColor.value)
  }
}
