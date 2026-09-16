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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import kotlin.test.Test

class AlphaTileTest {

  @Test
  fun theTileRendersWithoutAController() = runColorPickerUiTest {
    setContent {
      AlphaTile(
        modifier = Modifier.size(60.dp).testTag("tile"),
        selectedColor = Color.Red.copy(alpha = 0.5f),
      )
    }

    onNodeWithTag("tile").assertExists()
  }

  @Test
  fun theTileRendersWithAController() = runColorPickerUiTest {
    setContent {
      AlphaTile(
        modifier = Modifier.size(60.dp).testTag("tile"),
        controller = rememberColorPickerController(),
      )
    }

    onNodeWithTag("tile").assertExists()
  }
}
