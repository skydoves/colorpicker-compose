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

import androidx.compose.ui.graphics.Color
import java.util.Locale

internal val Color.hexCode: String
  inline get() {
    val a: Int = (alpha * 255).toInt()
    val r: Int = (red * 255).toInt()
    val g: Int = (green * 255).toInt()
    val b: Int = (blue * 255).toInt()
    return java.lang.String.format(Locale.getDefault(), "%02X%02X%02X%02X", a, r, g, b)
  }
