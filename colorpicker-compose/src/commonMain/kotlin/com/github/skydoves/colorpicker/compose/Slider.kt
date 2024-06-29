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

import androidx.annotation.FloatRange
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

/**
 * Slider allows you to adjust the a value of the selected color from color pickers.
 * See [AlphaSlider] and [BrightnessSlider] for concrete versions.
 *
 * @param modifier [Modifier] to decorate the internal Canvas.
 * @param controller Allows you to control and interacts with color pickers and all relevant subcomponents.
 * @param borderRadius Radius of the border.
 * @param borderSize [Dp] size of the border.
 * @param borderColor [Color] of the border.
 * @param wheelImageBitmap [ImageBitmap] to draw the wheel.
 * @param wheelRadius Radius of the wheel.
 * @param wheelColor [Color] of th wheel.
 * @param wheelPaint [Paint] to draw the wheel.
 * @param initialColor [Color] of the initial state. This property works for [HsvColorPicker] and
 * it will be selected on rightmost of slider if you give null value.
 *
 * @param drawBackground optional function to draw anything on the canvas
 * @param getValue function to get the current value from the controller
 * @param setValue function to set the current value on the controller
 * @param computeInitial function to compute the initial value from the initial color
 * @param getGradientColors function to get the gradient colors from the controller
 */
@Composable
internal fun Slider(
  modifier: Modifier = Modifier,
  controller: ColorPickerController,
  borderRadius: Dp = 6.dp,
  borderSize: Dp = 5.dp,
  borderColor: Color = Color.LightGray,
  wheelImageBitmap: ImageBitmap? = null,
  wheelRadius: Dp = 12.dp,
  wheelColor: Color = Color.White,
  @FloatRange(from = 0.0, to = 1.0) wheelAlpha: Float = 1.0f,
  wheelPaint: Paint = Paint().apply {
    color = wheelColor
    alpha = wheelAlpha
  },
  initialColor: Color? = null,
  drawBackground: Canvas.(IntSize) -> Unit = {},
  getValue: ColorPickerController.() -> Float,
  setValue: ColorPickerController.(Float, fromUser: Boolean) -> Unit,
  computeInitial: (Color) -> Float,
  getGradientColors: ColorPickerController.() -> List<Color>,
) {
  val density = LocalDensity.current
  var background: ImageBitmap? = null
  val borderPaint = Paint().apply {
    style = PaintingStyle.Stroke
    strokeWidth = with(density) { borderSize.toPx() }
    color = borderColor
  }
  val colorPaint = Paint().apply {
    color = controller.pureSelectedColor.value
  }
  val wheelRadiusPx = with(density) { wheelRadius.toPx() }

  var isInitialized by remember { mutableStateOf(false) }

  fun setValue(wheelPoint: Float) {
    val position = background?.width?.toFloat()?.let { wheelPoint / it } ?: 0f
    controller.setValue(position.coerceIn(0f, 1f), true)
  }

  Canvas(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(borderRadius))
      .onSizeChanged { size ->
        if (size.width != 0 && size.height != 0) {
          background = ImageBitmap.fromDrawing(size) {
            drawBackground(size)
            drawRoundRect(size, borderRadius.value, borderPaint)
          }
        }
      }
      .pointerInput(Unit) {
        detectHorizontalDragGestures { change, _ -> setValue(change.position.x) }
      }
      .pointerInput(Unit) { detectTapGestures { offset -> setValue(offset.x) } },
  ) {
    drawIntoCanvas { canvas ->
      background?.let {
        val (width, height) = it.size

        // draw background bitmap.
        canvas.drawImage(it)

        // draw a linear gradient color shader.
        val halfHeight = height * 0.5f
        colorPaint.shader = LinearGradientShader(
          colors = controller.getGradientColors(),
          from = Offset(0f, halfHeight),
          to = Offset(width.toFloat(), halfHeight),
          tileMode = TileMode.Clamp,
        )
        canvas.drawRoundRect(it.size, borderRadius.value, colorPaint)

        // draw wheel bitmap on the canvas.
        canvas.drawWheel(
          position = controller.getValue(),
          width = width,
          height = height,
          wheelImageBitmap = wheelImageBitmap,
          wheelRadius = wheelRadiusPx,
          wheelColor = wheelColor,
          wheelAlpha = wheelAlpha,
          wheelPaint = wheelPaint,
        )
      }

      if (initialColor != null && !isInitialized) {
        isInitialized = true
        controller.setValue(computeInitial(initialColor), false)
      }
    }
  }
}

private fun Canvas.drawWheel(
  position: Float,
  width: Int,
  height: Int,
  wheelImageBitmap: ImageBitmap?,
  wheelRadius: Float,
  wheelColor: Color = Color.White,
  @FloatRange(from = 0.0, to = 1.0) wheelAlpha: Float = 1.0f,
  wheelPaint: Paint = Paint().apply {
    color = wheelColor
    alpha = wheelAlpha
  },
) {
  val center = Offset(position.coerceIn(0f, 1f) * width, height * 0.5f)
  if (wheelImageBitmap == null) {
    drawCircle(center, wheelRadius, wheelPaint)
  } else {
    drawImageCenterAt(wheelImageBitmap, center)
  }
}
