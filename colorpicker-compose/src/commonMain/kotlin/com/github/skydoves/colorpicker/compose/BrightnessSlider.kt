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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * BrightnessSlider allows you to adjust the brightness value of the selected color from color pickers.
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
 */
@Composable
public fun BrightnessSlider(
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
) {
  SideEffect {
    controller.isAttachedBrightnessSlider = true
  }

  Slider(
    modifier = modifier,
    controller = controller,
    borderRadius = borderRadius,
    borderSize = borderSize,
    borderColor = borderColor,
    wheelImageBitmap = wheelImageBitmap,
    wheelRadius = wheelRadius,
    wheelColor = wheelColor,
    wheelAlpha = wheelAlpha,
    wheelPaint = wheelPaint,
    initialColor = initialColor,
    getValue = { brightness.value },
    setValue = ColorPickerController::setBrightness,
    computeInitial = { maxOf(it.red, it.green, it.blue) },
    getGradientColors = { listOf(Color.Black, pureSelectedColor.value) },
  )
}
