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

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.v2.runComposeUiTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

/**
 * Runs [block] inside a headless Compose scene.
 *
 * Two things need arranging before a color picker can be composed in a test:
 *
 * - [ColorPicker] collects the controller's color flow on `Dispatchers.Main`, and the Skiko test
 *   environment never installs one. Pinning it to an unconfined test dispatcher also makes
 *   `onColorChanged` land synchronously, so a test can assert right after a gesture.
 * - The v2 test scene runs at a density of 1, so a `Modifier.size(200.dp)` node is exactly 200px
 *   and gesture coordinates can be written as plain numbers.
 */
internal fun runColorPickerUiTest(block: suspend ComposeUiTest.() -> Unit) {
  Dispatchers.setMain(UnconfinedTestDispatcher())
  try {
    runComposeUiTest { block() }
  } finally {
    Dispatchers.resetMain()
  }
}
