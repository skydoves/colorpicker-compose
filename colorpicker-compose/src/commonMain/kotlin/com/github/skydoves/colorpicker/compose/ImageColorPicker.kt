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

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.IntSize

/**
 * ImageColorPicker allows you to get colors from any images by tapping on the desired color.
 *
 * @param modifier [Modifier] to decorate the internal Canvas.
 * @param controller Allows you to control and interacts with color pickers and all relevant subcomponents.
 * @param paletteImageBitmap [ImageBitmap] to draw the palette.
 * @param wheelImageBitmap [ImageBitmap] to draw the wheel.
 * @param drawOnPosSelected to draw anything on the canvas when [ColorPickerController.selectedPoint] changes
 * @param drawDefaultWheelIndicator should the indicator be drawn on the canvas. Defaults to false if either [wheelImageBitmap] or [drawOnPosSelected] are not null.
 * @param paletteContentScale Represents a rule to apply to scale a source rectangle to be inscribed into a destination.
 * @param previewImagePainter Display an image instead of the palette on the inspection preview mode on Android Studio.
 * @param onColorChanged Color changed listener.
 */
@Composable
public fun ImageColorPicker(
  modifier: Modifier,
  controller: ColorPickerController,
  paletteImageBitmap: ImageBitmap,
  wheelImageBitmap: ImageBitmap? = null,
  drawOnPosSelected: (DrawScope.() -> Unit)? = null,
  drawDefaultWheelIndicator: Boolean = wheelImageBitmap == null && drawOnPosSelected == null,
  paletteContentScale: PaletteContentScale = PaletteContentScale.FIT,
  previewImagePainter: Painter? = null,
  onColorChanged: (colorEnvelope: ColorEnvelope) -> Unit = {},
) {
  if (LocalInspectionMode.current && previewImagePainter != null) {
    Image(
      modifier = modifier.fillMaxSize(),
      painter = previewImagePainter,
      contentDescription = null,
    )
    return
  }

  val controllerBitmap: ImageBitmap? by controller.paletteBitmap.collectAsState(
    initial = paletteImageBitmap,
  )
  val imageBitmap = controllerBitmap ?: paletteImageBitmap

  val width = remember(imageBitmap) { imageBitmap.width }
  val height = remember(imageBitmap) { imageBitmap.height }
  var offset by remember { mutableStateOf(Offset.Zero) }
  var scale by remember { mutableStateOf(1f) }

  LaunchedEffect(key1 = imageBitmap) {
    controller.setup { point ->
      val origPoint = (point - offset) / scale
      val imPoint = Offset(
        origPoint.x.coerceIn(0f, width - 1f),
        origPoint.y.coerceIn(0f, height - 1f),
      )
      // TODO: transparent pixel handling
      val px = imageBitmap.getPixel(imPoint.roundToInt())
      val newPoint = imPoint * scale + offset
      px to newPoint
    }
  }

  ColorPicker(
    modifier = modifier,
    controller = controller,
    wheelImageBitmap = wheelImageBitmap,
    drawOnPosSelected = drawOnPosSelected,
    drawDefaultWheelIndicator = drawDefaultWheelIndicator,
    onColorChanged = onColorChanged,
    sizeChanged = { size ->
      val metrics = when (paletteContentScale) {
        PaletteContentScale.FIT -> {
          getMetricsForFit(imageBitmap.size, size)
        }

        PaletteContentScale.CROP -> {
          getMetricsForCrop(imageBitmap.size, size)
        }
      }
      scale = metrics.first
      offset = metrics.second
    },
    setup = {
      controller.setup { point ->
        val origPoint = (point - offset) / scale
        val imPoint = Offset(
          origPoint.x.coerceIn(0f, width - 1f),
          origPoint.y.coerceIn(0f, height - 1f),
        )
        // TODO: transparent pixel handling
        val px = imageBitmap.getPixel(imPoint.roundToInt())
        val newPoint = imPoint * scale + offset
        px to newPoint
      }
    },
    draw = {
      drawImageRect(
        image = imageBitmap,
        dstOffset = offset.roundToInt(),
        dstSize = imageBitmap.size * scale,
        paint = emptyPaint,
      )
    },
  )
}

private fun getMetricsForFit(imSize: IntSize, targetSize: IntSize) =
  if (imSize.width * targetSize.height > targetSize.width * imSize.height) {
    scaleToWidth(imSize, targetSize)
  } else {
    scaleToHeight(imSize, targetSize)
  }

private fun getMetricsForCrop(imSize: IntSize, targetSize: IntSize) =
  if (imSize.width * targetSize.height > targetSize.width * imSize.height) {
    scaleToHeight(imSize, targetSize)
  } else {
    scaleToWidth(imSize, targetSize)
  }

private fun scaleToWidth(imSize: IntSize, targetSize: IntSize): Pair<Float, Offset> {
  val scale = targetSize.width.toFloat() / imSize.width
  val offset = Offset(0f, (targetSize.height - imSize.height * scale) * 0.5f)
  return scale to offset
}

private fun scaleToHeight(imSize: IntSize, targetSize: IntSize): Pair<Float, Offset> {
  val scale = targetSize.height.toFloat() / imSize.height
  val offset = Offset((targetSize.width - imSize.width * scale) * 0.5f, 0f)
  return scale to offset
}
