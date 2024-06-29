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

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.IntSize
import kotlin.math.min

/**
 * HsvColorPicker allows you to get colors from HSV color palette by tapping on the desired color.
 *
 * @param modifier [Modifier] to decorate the internal Canvas.
 * @param controller Allows you to control and interacts with color pickers and all relevant subcomponents.
 * @param wheelImageBitmap [ImageBitmap] to draw the wheel.
 * @param drawOnPosSelected to draw anything on the canvas when [ColorPickerController.selectedPoint] changes
 * @param drawDefaultWheelIndicator should the indicator be drawn on the canvas. Defaults to false if either [wheelImageBitmap] or [drawOnPosSelected] are not null.
 * @param onColorChanged Color changed listener.
 * @param initialColor [Color] of the initial state. This property works for [HsvColorPicker] and
 * it will be selected on center if you give null value.
 */
@Composable
public fun HsvColorPicker(
  modifier: Modifier,
  controller: ColorPickerController,
  wheelImageBitmap: ImageBitmap? = null,
  drawOnPosSelected: (DrawScope.() -> Unit)? = null,
  drawDefaultWheelIndicator: Boolean = wheelImageBitmap == null && drawOnPosSelected == null,
  onColorChanged: (colorEnvelope: ColorEnvelope) -> Unit = {},
  initialColor: Color? = null,
) {
  var size by remember { mutableStateOf(IntSize.Zero) }
  var radius by remember { mutableStateOf(size.radius) }
  var center by remember { mutableStateOf(size.center) }

  ColorPicker(
    modifier = modifier.fillMaxSize(),
    controller = controller,
    wheelImageBitmap = wheelImageBitmap,
    drawOnPosSelected = drawOnPosSelected,
    drawDefaultWheelIndicator = drawDefaultWheelIndicator,
    onColorChanged = onColorChanged,
    sizeChanged = {
      size = it
      radius = size.radius
      center = size.center
    },
    setup = {
      val initialPosition = initialColor?.let {
        val (h, s, _) = it.toHSV()
        hsvToCoord(h, s, center)
      } ?: center
      controller.setup(initialPosition) { point ->
        val vector = point - center
        val angle = vector.angle()
        val hue = angleToHue(angle) // hue: 0 to 360
        val sat = min(vector.length() / radius, 1f) // saturation: 0 to 1
        // adjust point in case saturation was out of bounds
        val newPoint = Offset.fromAngle(angle, sat * radius) + center
        try {
          Color.hsv(hue, sat, 1f) to newPoint
        } catch (e: IllegalArgumentException) {
          // Just in case something goes wrong...
          Color.hsv(0f, 0f, 0f) to newPoint
        }
      }
    },
    draw = { drawHsvColorGradient(controller.canvasSize) },
  )
}
