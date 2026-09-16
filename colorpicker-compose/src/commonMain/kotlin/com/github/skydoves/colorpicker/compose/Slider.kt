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
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.fillMaxHeight
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
 * Slider allows you to adjust the value of the selected color from color pickers.
 * See [AlphaSlider] and [BrightnessSlider] for concrete versions.
 *
 * @param modifier [Modifier] to decorate the internal Canvas.
 * @param controller Allows you to control and interacts with color pickers and all relevant subcomponents.
 * @param borderRadius Radius of the border.
 * @param borderSize [Dp] size of the border.
 * @param borderColor [Color] of the border.
 * @param wheelImageBitmap [ImageBitmap] to draw the wheel.
 * @param wheelRadius Radius of the wheel.
 * @param wheelColor [Color] of the wheel.
 * @param wheelPaint [Paint] to draw the wheel.
 * @param initialColor [Color] of the initial state. This property works for [HsvColorPicker] and
 * it will be selected on rightmost of slider if you give null value.
 * @param orientation Whether the slider runs left to right or bottom to top.
 *
 * @param drawBackground optional function to draw anything on the canvas
 * @param getValue function to get the current value from the controller
 * @param setValue function to set the current value on the controller
 * @param onValueChanged Callback invoked when value changes, with the new value in `0f..1f`.
 * Provides [ColorChangeSource] the update trigger source.
 * @param onColorChanged Callback invoked with the color the controller ends up on.
 * @param onStart Callback invoked when user interaction with the slider starts.
 * @param onFinish Callback invoked when user interaction with the slider ends.
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
  wheelAlpha: Float = 1.0f,
  wheelPaint: Paint = Paint().apply {
    color = wheelColor
    alpha = wheelAlpha
  },
  initialColor: Color? = null,
  orientation: SliderOrientation = SliderOrientation.Horizontal,
  drawBackground: Canvas.(IntSize) -> Unit = {},
  getValue: ColorPickerController.() -> Float,
  setValue: ColorPickerController.(Float, fromUser: Boolean, source: ColorChangeSource) -> Unit,
  onValueChanged: (ColorChangeSource, Float) -> Unit = { _, _ -> },
  onColorChanged: ((ColorEnvelope) -> Unit)? = null,
  onStart: () -> Unit = {},
  onFinish: () -> Unit = {},
  computeInitial: (Color) -> Float,
  getGradientColors: ColorPickerController.() -> List<Color>,
) {
  val density = LocalDensity.current
  val isVertical = orientation == SliderOrientation.Vertical
  val debounceDuration = controller.debounceDuration

  var canvasSize by remember { mutableStateOf(IntSize.Zero) }
  var isInitialized by remember { mutableStateOf(false) }

  val borderPaint = Paint().apply {
    style = PaintingStyle.Stroke
    strokeWidth = with(density) { borderSize.toPx() }
    color = borderColor
  }
  // The shader below supplies every color this paint draws. Taking one from the controller here
  // also took its alpha, and a controller with nothing selected yet is fully transparent, which
  // left a standalone slider drawing nothing at all.
  val colorPaint = Paint()
  val wheelRadiusPx = with(density) { wheelRadius.toPx() }
  // Half a thumb at each end, so it sits inside the track instead of being clipped by it.
  val thumbInset = wheelImageBitmap
    ?.let { (if (isVertical) it.height else it.width) * 0.5f }
    ?: wheelRadiusPx

  val background = canvasSize.takeIf { it.width != 0 && it.height != 0 }?.let { size ->
    ImageBitmap.fromDrawing(size) {
      drawBackground(size)
      drawRoundRect(size, borderRadius.value, borderPaint)
    }
  }

  controller.ObserveColorChanges(onColorChanged)

  fun setValue(point: Float, source: ColorChangeSource) {
    val size = canvasSize.takeIf { it.width != 0 && it.height != 0 } ?: return
    val travel = travelOf(size, isVertical, thumbInset)
    val fraction = (point - thumbInset) / travel
    // Vertical sliders run bottom to top, the way anyone expects a fader to.
    val position = (if (isVertical) 1f - fraction else fraction).coerceIn(0f, 1f)
    controller.setValue(position, true, source)
    onValueChanged(source, position)
  }

  Canvas(
    modifier = modifier
      .then(if (isVertical) Modifier.fillMaxHeight() else Modifier.fillMaxWidth())
      .clip(RoundedCornerShape(borderRadius))
      .onSizeChanged { size ->
        if (size.width != 0 && size.height != 0) {
          canvasSize = size
          if (initialColor != null && !isInitialized) {
            isInitialized = true
            controller.setValue(computeInitial(initialColor), false, ColorChangeSource.Programmatic)
          }
        }
      }
      .pointerInput(key1 = controller, key2 = orientation) {
        detectTapGestures(
          onTap = { offset ->
            setValue(if (isVertical) offset.y else offset.x, ColorChangeSource.Tap)
          },
        )
      }
      .pointerInput(controller, debounceDuration, orientation) {
        if (isVertical) {
          detectVerticalDragGestures(
            onDragStart = { onStart() },
            onDragEnd = { onFinish() },
            onDragCancel = { onFinish() },
          ) { change, _ -> setValue(change.position.y, ColorChangeSource.Drag) }
        } else {
          detectHorizontalDragGestures(
            onDragStart = { onStart() },
            onDragEnd = { onFinish() },
            onDragCancel = { onFinish() },
          ) { change, _ -> setValue(change.position.x, ColorChangeSource.Drag) }
        }
      },
  ) {
    drawIntoCanvas { canvas ->
      background?.let {
        val size = it.size

        // draw background bitmap.
        canvas.drawImage(it)

        // draw a linear gradient color shader.
        colorPaint.shader = LinearGradientShader(
          colors = controller.getGradientColors(),
          from = gradientStart(size, isVertical),
          to = gradientEnd(size, isVertical),
          tileMode = TileMode.Clamp,
        )
        canvas.drawRoundRect(size, borderRadius.value, colorPaint)

        // draw wheel bitmap on the canvas.
        canvas.drawWheel(
          center = thumbCenter(size, isVertical, thumbInset, controller.getValue()),
          wheelImageBitmap = wheelImageBitmap,
          wheelRadius = wheelRadiusPx,
          wheelPaint = wheelPaint,
        )
      }
    }
  }
}

/** How far the thumb center can travel, once a radius is reserved at each end. */
private fun travelOf(size: IntSize, isVertical: Boolean, thumbInset: Float): Float {
  val length = (if (isVertical) size.height else size.width).toFloat()
  return (length - 2f * thumbInset).coerceAtLeast(1f)
}

private fun thumbCenter(
  size: IntSize,
  isVertical: Boolean,
  thumbInset: Float,
  position: Float,
): Offset {
  val fraction = position.coerceIn(0f, 1f)
  val along = thumbInset + (if (isVertical) 1f - fraction else fraction) *
    travelOf(size, isVertical, thumbInset)
  return if (isVertical) {
    Offset(size.width * 0.5f, along)
  } else {
    Offset(along, size.height * 0.5f)
  }
}

private fun gradientStart(size: IntSize, isVertical: Boolean): Offset = if (isVertical) {
  Offset(size.width * 0.5f, size.height.toFloat())
} else {
  Offset(0f, size.height * 0.5f)
}

private fun gradientEnd(size: IntSize, isVertical: Boolean): Offset = if (isVertical) {
  Offset(size.width * 0.5f, 0f)
} else {
  Offset(size.width.toFloat(), size.height * 0.5f)
}

private fun Canvas.drawWheel(
  center: Offset,
  wheelImageBitmap: ImageBitmap?,
  wheelRadius: Float,
  wheelPaint: Paint,
) {
  if (wheelImageBitmap == null) {
    drawCircle(center, wheelRadius, wheelPaint)
  } else {
    drawImageCenterAt(wheelImageBitmap, center)
  }
}
