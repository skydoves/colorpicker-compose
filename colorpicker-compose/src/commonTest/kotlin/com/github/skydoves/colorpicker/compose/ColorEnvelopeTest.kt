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
import kotlin.test.Test
import kotlin.test.assertEquals

class ColorEnvelopeTest {

  @Test
  fun anEnvelopeDefaultsToAProgrammaticSource() {
    val envelope = ColorEnvelope(Color.Red, Color.Red.hexCode, fromUser = false)

    assertEquals(ColorChangeSource.Programmatic, envelope.source)
  }

  @Test
  fun envelopesWithTheSameContentAreEqual() {
    val first = ColorEnvelope(Color.Red, "ffff0000", true, ColorChangeSource.Tap)
    val second = ColorEnvelope(Color.Red, "ffff0000", true, ColorChangeSource.Tap)

    assertEquals(first, second)
    assertEquals(first.hashCode(), second.hashCode())
  }

  @Test
  fun theSourceIsPartOfTheIdentity() {
    val tapped = ColorEnvelope(Color.Red, "ffff0000", true, ColorChangeSource.Tap)
    val dragged = tapped.copy(source = ColorChangeSource.Drag)

    assertEquals(ColorChangeSource.Drag, dragged.source)
    assertEquals(tapped.color, dragged.color)
  }
}
