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

/**
 * Runs [block] inside a headless Compose scene.
 *
 * The scene runs at a density of 1, so a `Modifier.size(200.dp)` node is exactly 200px and gesture
 * coordinates can be written as plain numbers.
 *
 * A picker reports color changes from the composition's own coroutine scope, so a callback lands on
 * the next idle rather than inside the gesture. Tests that read one call [waitForIdle] first.
 */
internal fun runColorPickerUiTest(block: suspend ComposeUiTest.() -> Unit) {
  runComposeUiTest { block() }
}
