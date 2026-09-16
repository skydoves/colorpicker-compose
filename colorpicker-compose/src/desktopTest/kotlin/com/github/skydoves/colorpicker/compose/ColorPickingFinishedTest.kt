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
 * onColorChanged fires for every step of a drag, which is too often for saving a choice or logging
 * one. onColorPickingFinished fires once, when the gesture is over.
 */
class ColorPickingFinishedTest {

  @Test
  fun aTapFinishesThePickOnce() = runColorPickerUiTest {
    val finished = mutableListOf<ColorEnvelope>()
    setContent {
      HsvColorPicker(
        modifier = Modifier.size(SIDE.dp).testTag("picker"),
        controller = rememberColorPickerController(),
        onColorPickingFinished = { finished += it },
      )
    }

    onNodeWithTag("picker").performTouchInput { click(Offset(199f, 100f)) }

    assertEquals(1, finished.size)
    assertColorEquals(Color.Red, finished.single().color, tolerance = 0.02f)
    assertEquals(ColorChangeSource.Tap, finished.single().source)
    assertTrue(finished.single().fromUser)
  }

  @Test
  fun aDragFinishesThePickOnceAtTheEnd() = runColorPickerUiTest {
    val changed = mutableListOf<ColorEnvelope>()
    val finished = mutableListOf<ColorEnvelope>()
    setContent {
      HsvColorPicker(
        modifier = Modifier.size(SIDE.dp).testTag("picker"),
        controller = rememberColorPickerController(),
        onColorChanged = { changed += it },
        onColorPickingFinished = { finished += it },
      )
    }

    onNodeWithTag("picker").performTouchInput {
      swipe(start = Offset(100f, 100f), end = Offset(199f, 100f))
    }

    assertTrue(changed.size > 1, "the drag only reported ${changed.size} steps")
    assertEquals(1, finished.size)
    assertColorEquals(Color.Red, finished.single().color, tolerance = 0.05f)
    assertEquals(ColorChangeSource.Drag, finished.single().source)
  }

  @Test
  fun theFinishedColorIsTheOneTheGestureSettledOn() = runColorPickerUiTest {
    lateinit var controller: ColorPickerController
    val finished = mutableListOf<ColorEnvelope>()
    setContent {
      controller = rememberColorPickerController()
      HsvColorPicker(
        modifier = Modifier.size(SIDE.dp).testTag("picker"),
        controller = controller,
        onColorPickingFinished = { finished += it },
      )
    }

    onNodeWithTag("picker").performTouchInput {
      swipe(start = Offset(199f, 100f), end = Offset(1f, 100f))
    }

    assertEquals(controller.selectedColor.value, finished.single().color)
    assertEquals(controller.selectedColor.value.hexCode, finished.single().hexCode)
  }

  @Test
  fun aProgrammaticSelectionNeverFinishesAPick() = runColorPickerUiTest {
    lateinit var controller: ColorPickerController
    val finished = mutableListOf<ColorEnvelope>()
    setContent {
      controller = rememberColorPickerController()
      HsvColorPicker(
        modifier = Modifier.size(SIDE.dp).testTag("picker"),
        controller = controller,
        onColorPickingFinished = { finished += it },
      )
    }

    controller.selectByColor(Color.Green, fromUser = false)
    waitForIdle()

    assertTrue(finished.isEmpty(), "a programmatic change reported ${finished.size} picks")
  }

  @Test
  fun theImagePickerFinishesAPickToo() = runColorPickerUiTest {
    val palette = quadrantBitmap()
    val finished = mutableListOf<ColorEnvelope>()
    setContent {
      ImageColorPicker(
        modifier = Modifier.size(SIDE.dp).testTag("picker"),
        controller = rememberColorPickerController(),
        paletteImageBitmap = palette,
        onColorPickingFinished = { finished += it },
      )
    }

    onNodeWithTag("picker").performTouchInput { click(Offset(50f, 150f)) }

    assertEquals(1, finished.size)
    assertEquals(Color.Blue, finished.single().color)
  }

  @Test
  fun eachTapFinishesItsOwnPick() = runColorPickerUiTest {
    val finished = mutableListOf<ColorEnvelope>()
    setContent {
      HsvColorPicker(
        modifier = Modifier.size(SIDE.dp).testTag("picker"),
        controller = rememberColorPickerController(),
        onColorPickingFinished = { finished += it },
      )
    }

    onNodeWithTag("picker").performTouchInput { click(Offset(199f, 100f)) }
    onNodeWithTag("picker").performTouchInput { click(Offset(1f, 100f)) }

    assertEquals(2, finished.size)
    assertColorEquals(Color.Red, finished.first().color, tolerance = 0.02f)
    assertColorEquals(Color.Cyan, finished.last().color, tolerance = 0.02f)
  }
}
