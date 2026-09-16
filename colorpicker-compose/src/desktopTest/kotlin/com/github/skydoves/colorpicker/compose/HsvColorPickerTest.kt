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

/** The test scene runs at a density of 1, so this picker is exactly 200px on each side. */
private const val SIDE = 200

class HsvColorPickerTest {

  @Test
  fun thePickerRenders() = runColorPickerUiTest {
    setContent {
      HsvColorPicker(
        modifier = Modifier.size(SIDE.dp).testTag("picker"),
        controller = rememberColorPickerController(),
      )
    }

    onNodeWithTag("picker").assertExists()
  }

  @Test
  fun tappingTheCenterPicksWhite() = runColorPickerUiTest {
    lateinit var controller: ColorPickerController
    setContent {
      controller = rememberColorPickerController()
      HsvColorPicker(
        modifier = Modifier.size(SIDE.dp).testTag("picker"),
        controller = controller,
      )
    }

    onNodeWithTag("picker").performTouchInput { click(Offset(100f, 100f)) }

    assertColorEquals(Color.White, controller.selectedColor.value)
  }

  @Test
  fun tappingTheRightEdgePicksRed() = runColorPickerUiTest {
    lateinit var controller: ColorPickerController
    setContent {
      controller = rememberColorPickerController()
      HsvColorPicker(
        modifier = Modifier.size(SIDE.dp).testTag("picker"),
        controller = controller,
      )
    }

    onNodeWithTag("picker").performTouchInput { click(Offset(199f, 100f)) }

    assertColorEquals(Color.Red, controller.selectedColor.value, tolerance = 0.02f)
  }

  @Test
  fun tappingTheLeftEdgePicksCyan() = runColorPickerUiTest {
    lateinit var controller: ColorPickerController
    setContent {
      controller = rememberColorPickerController()
      HsvColorPicker(
        modifier = Modifier.size(SIDE.dp).testTag("picker"),
        controller = controller,
      )
    }

    onNodeWithTag("picker").performTouchInput { click(Offset(1f, 100f)) }

    assertColorEquals(Color.Cyan, controller.selectedColor.value, tolerance = 0.02f)
  }

  @Test
  fun aTapIsReportedAsComingFromTheUser() = runColorPickerUiTest {
    val envelopes = mutableListOf<ColorEnvelope>()
    setContent {
      HsvColorPicker(
        modifier = Modifier.size(SIDE.dp).testTag("picker"),
        controller = rememberColorPickerController(),
        onColorChanged = { envelopes += it },
      )
    }

    onNodeWithTag("picker").performTouchInput { click(Offset(199f, 100f)) }

    assertTrue(envelopes.last().fromUser)
    assertEquals(ColorChangeSource.Tap, envelopes.last().source)
  }

  @Test
  fun aDragIsReportedAsADrag() = runColorPickerUiTest {
    val envelopes = mutableListOf<ColorEnvelope>()
    setContent {
      HsvColorPicker(
        modifier = Modifier.size(SIDE.dp).testTag("picker"),
        controller = rememberColorPickerController(),
        onColorChanged = { envelopes += it },
      )
    }

    onNodeWithTag("picker").performTouchInput {
      swipe(start = Offset(100f, 100f), end = Offset(190f, 100f))
    }

    assertEquals(ColorChangeSource.Drag, envelopes.last().source)
  }

  @Test
  fun onStartAndOnFinishBracketADrag() = runColorPickerUiTest {
    var started = 0
    var finished = 0
    setContent {
      HsvColorPicker(
        modifier = Modifier.size(SIDE.dp).testTag("picker"),
        controller = rememberColorPickerController(),
        onStart = { started++ },
        onFinish = { finished++ },
      )
    }

    onNodeWithTag("picker").performTouchInput {
      swipe(start = Offset(100f, 100f), end = Offset(190f, 100f))
    }

    assertEquals(1, started)
    assertEquals(1, finished)
  }

  @Test
  fun theSelectedPointFollowsTheTap() = runColorPickerUiTest {
    lateinit var controller: ColorPickerController
    setContent {
      controller = rememberColorPickerController()
      HsvColorPicker(
        modifier = Modifier.size(SIDE.dp).testTag("picker"),
        controller = controller,
      )
    }

    onNodeWithTag("picker").performTouchInput { click(Offset(150f, 100f)) }

    assertOffsetEquals(Offset(150f, 100f), controller.selectedPoint.value, tolerance = 0.5f)
  }

  @Test
  fun aTapOutsideTheWheelIsSnappedOntoItsEdge() = runColorPickerUiTest {
    lateinit var controller: ColorPickerController
    setContent {
      controller = rememberColorPickerController()
      HsvColorPicker(
        modifier = Modifier.size(SIDE.dp).testTag("picker"),
        controller = controller,
      )
    }

    onNodeWithTag("picker").performTouchInput { click(Offset(199f, 199f)) }

    val distance = controller.selectedPoint.value.distanceTo(Offset(100f, 100f))
    assertTrue(distance <= 100.5f, "the indicator escaped the wheel, it sat $distance away")
  }
}
