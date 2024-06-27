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

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * AlphaTile allows you to display ARGB colors including transparency with tiles.
 *
 * @param modifier [Modifier] to decorate the internal Canvas.
 * @param controller Allows you to control and interacts with color pickers and all relevant subcomponents.
 * @param selectedColor Color to be displayed over the tiles if the [controller] is not registered.
 * @param tileOddColor Color of the odd tiles.
 * @param tileEvenColor Color of the even tiles.
 * @param tileSize DP size of tiles.
 */
@Composable
public fun AlphaTile(
  modifier: Modifier,
  controller: ColorPickerController? = null,
  selectedColor: Color = Color.Transparent,
  tileOddColor: Color = defaultTileOddColor,
  tileEvenColor: Color = defaultTileEvenColor,
  tileSize: Dp = 12.dp,
) {
  val density = LocalDensity.current
  var background: ImageBitmap? = null
  val colorPaint = Paint().apply {
    color = controller?.selectedColor?.value ?: selectedColor
  }
  val paint = alphaTilePaint(
    with(density) { tileSize.toPx() },
    tileOddColor,
    tileEvenColor,
  )

  Canvas(
    modifier
      .fillMaxSize()
      .onSizeChanged { size ->
        if (size.width != 0 && size.height != 0)
          background = ImageBitmap.fromPaint(paint, size)
      },
  ) {
    drawIntoCanvas { canvas ->
      background?.let {
        canvas.drawImage(it)
        canvas.drawRect(it.size, colorPaint)
      }
    }
  }
}
