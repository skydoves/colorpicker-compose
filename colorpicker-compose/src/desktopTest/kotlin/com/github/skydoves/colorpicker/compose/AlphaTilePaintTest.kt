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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.IntSize
import kotlin.test.Test
import kotlin.test.assertEquals

class AlphaTilePaintTest {

  @Test
  fun tilesAlternateLikeACheckerboard() {
    val paint = alphaTilePaint(
      tileSize = 4f,
      tileOddColor = Color.White,
      tileEvenColor = Color.Black,
    )
    val bitmap = ImageBitmap.fromPaint(paint, IntSize(8, 8))

    assertEquals(Color.White, bitmap.getPixel(1, 1))
    assertEquals(Color.Black, bitmap.getPixel(5, 1))
    assertEquals(Color.Black, bitmap.getPixel(1, 5))
    assertEquals(Color.White, bitmap.getPixel(5, 5))
  }

  @Test
  fun thePatternRepeatsBeyondTheFirstTwoTiles() {
    val paint = alphaTilePaint(
      tileSize = 4f,
      tileOddColor = Color.White,
      tileEvenColor = Color.Black,
    )
    val bitmap = ImageBitmap.fromPaint(paint, IntSize(16, 16))

    assertEquals(Color.White, bitmap.getPixel(9, 9))
    assertEquals(Color.Black, bitmap.getPixel(13, 9))
  }
}
