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
@file:Suppress("RedundantVisibilityModifier", "unused")

package com.github.skydoves.colorpicker.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull

/** Creates and remembers a [ColorPickerController] on the current composer. */
@Composable
public fun rememberColorPickerController(): ColorPickerController {
  val scope = rememberCoroutineScope()
  return remember { ColorPickerController(scope) }
}

/**
 * [ColorPickerController] allows you to control and interacts with the [ImageColorPicker], [HsvColorPicker],
 * and all relevant subcomponents. You can create and remember [ColorPickerController]
 * with the [rememberColorPickerController] extension.
 */
@Stable
public class ColorPickerController
@OptIn(DelicateCoroutinesApi::class)
constructor(
  internal val coroutineScope: CoroutineScope = GlobalScope,
) {
  internal var canvasSize: Size = Size.Zero
    set(value) {
      if (value == field) { return }
      val cur = _selectedPoint.value
      _selectedPoint.value = Offset( // TODO: check this for aspect ratio preservation
        cur.x * value.width / field.width,
        cur.y * value.height / field.height,
      )
      field = value
    }

  private val _selectedPoint: MutableState<Offset> = mutableStateOf(Offset.Zero)

  /** State of [Offset], which represents the currently selected coordinate. */
  public val selectedPoint: State<Offset> = _selectedPoint

  private val _selectedColor: MutableState<Color> = mutableStateOf(Color.Transparent)

  /** State of [Color], which represents the currently selected color value with alpha and brightness. */
  public val selectedColor: State<Color> = _selectedColor

  /** State of [Color], which represents the currently selected color value without alpha and brightness. */
  internal var pureSelectedColor: MutableState<Color> = mutableStateOf(Color.Transparent)

  /** Alpha value to be applied with the selected color. */
  internal var alpha: MutableState<Float> = mutableFloatStateOf(1.0f)

  /** Brightness value to be applied with the selected color. */
  internal var brightness: MutableState<Float> = mutableFloatStateOf(1.0f)

  /** An [ImageBitmap] to be drawn on the canvas as a wheel. */
  public var wheelBitmap: ImageBitmap? = null

  /** Radius to draw default wheel. */
  public var wheelRadius: Dp = 12.dp
    set(value) {
      field = value
      reviseTick.intValue++
    }

  /** Paint to draw default wheel. */
  public var wheelPaint: Paint = Paint().apply { color = Color.White }
    set(value) {
      field = value
      reviseTick.intValue++
    }

  /** Color of the default wheel. */
  public var wheelColor: Color
    get() = wheelPaint.color
    set(value) {
      wheelPaint.color = value
      reviseTick.intValue++
    }

  /** Color of the default wheel. */
  public var wheelAlpha: Float
    get() = wheelPaint.alpha
    set(value) {
      wheelPaint.alpha = value
      reviseTick.intValue++
    }

  private val _enabled: MutableState<Boolean> = mutableStateOf(true)

  /** Enable or not color selection. */
  public var enabled: Boolean
    get() = _enabled.value
    set(value) { _enabled.value = value }

  /** Indicates if the alpha slider has been attached. */
  internal var isAttachedAlphaSlider: Boolean = false

  /** Indicates if the brightness slider has been attached. */
  internal var isAttachedBrightnessSlider: Boolean = false

  internal var reviseTick = mutableIntStateOf(0)

  private var _colorFlow = MutableStateFlow<ColorEnvelope?>(null)

  @OptIn(FlowPreview::class)
  public fun getColorFlow(debounceDuration: Long = 0): Flow<ColorEnvelope> =
    _colorFlow.filterNotNull().debounce(debounceDuration)

  // Function that takes a coordinate and obtains a color
  // Also returns an adjusted coordinate if appropriate
  private var coordToColor: ((Offset) -> Pair<Color, Offset>)? = null

  /**
   * Setup the controller for use by a picker. The initial position is the
   * initial value selected by the picker. The coordinateToColor function
   * is used to get the color at a given coordinate. The function should
   * return the color at the coordinate and the adjusted coordinate if
   * the coordinate was out of bounds.
   */
  internal fun setup(
    initialPosition: Offset = canvasSize.center,
    coordinateToColor: (Offset) -> Pair<Color, Offset>,
  ) {
    this.coordToColor = coordinateToColor
    selectByCoordinate(initialPosition, fromUser = false)
    reviseTick.intValue++
  }

  /**
   * Select a specific point by coordinates and update a selected color.
   *
   * @param x x-coordinate to extract a pixel color.
   * @param y y-coordinate to extract a pixel color.
   * @param fromUser Represents this event is triggered by user or not.
   */
  public fun selectByCoordinate(x: Float, y: Float, fromUser: Boolean) {
    selectByCoordinate(Offset(x, y), fromUser)
  }

  /**
   * Select a specific point by coordinates and update a selected color.
   *
   * @param point coordinate to extract a pixel color.
   * @param fromUser Represents this event is triggered by user or not.
   */
  public fun selectByCoordinate(point: Offset, fromUser: Boolean) {
    if (selectByCoordinate(point)) {
      // notify color changes to the listeners.
      notifyColorChanged(fromUser)
    }
  }

  /**
   * Select a specific color and update with the selected color.
   *
   * @param color Color to be selected.
   * @param fromUser Represents this event is triggered by user or not.
   */
  public fun selectByColor(color: Color, fromUser: Boolean) {
    val (h, s, v) = color.toHSV()
    selectByHsv(h, s, v, color.alpha, fromUser)
  }

  /**
   * Select a specific color and update with the selected color.
   *
   * @param hsv A float array that represents hsv color code.
   * @param alpha An alpha value that will be composed with the [hsv] color code.
   * @param fromUser Represents this event is triggered by user or not.
   */
  public fun selectByHsv(hsv: FloatArray, alpha: Float, fromUser: Boolean) {
    selectByHsv(hsv[0], hsv[1], hsv[2], alpha, fromUser)
  }

  /**
   * Select a specific color and update with the selected color.
   *
   * @param h The float value for the hue.
   * @param s The float value for the saturation.
   * @param v The float value for the value.
   * @param alpha The float value for the alpha.
   * @param fromUser Represents this event is triggered by user or not.
   */
  public fun selectByHsv(h: Float, s: Float, v: Float, alpha: Float, fromUser: Boolean) {
    var changed = selectByCoordinate(hsvToCoord(h, s, canvasSize.center))
    changed = setAlpha(alpha) || changed
    changed = setBrightness(v) || changed
    if (changed) { notifyColorChanged(fromUser) }
  }

  /** Combine the alpha value to the selected pure color. */
  public fun setAlpha(alpha: Float, fromUser: Boolean) {
    if (setAlpha(alpha)) { notifyColorChanged(fromUser) }
  }

  /** Combine the brightness value to the selected pure color. */
  public fun setBrightness(brightness: Float, fromUser: Boolean) {
    if (setBrightness(brightness)) { notifyColorChanged(fromUser) }
  }

  /** Notify color changes to the color picker and other subcomponents. */
  private fun notifyColorChanged(fromUser: Boolean) {
    val color = _selectedColor.value
    _colorFlow.value = ColorEnvelope(color, color.hexCode, fromUser)
  }

  /**
   * Select a specific point by coordinates and update a selected color.
   * This version does not notify of the change but returns if there was a change.
   *
   * @param point coordinate to extract a pixel color.
   * @return true if the color was changed.
   */
  private fun selectByCoordinate(point: Offset): Boolean {
    val coordToColor = coordToColor
    if (!enabled || coordToColor == null) return false
    val (color, newPoint) = coordToColor(point)
    _selectedPoint.value = newPoint
    if (pureSelectedColor.value == color) return false
    _selectedColor.value = applyHSVFactors(color)
    pureSelectedColor.value = color
    return true
  }

  /** Combine the alpha value to the selected pure color. */
  private fun setAlpha(alpha: Float): Boolean {
    if (!enabled || this.alpha.value == alpha) { return false }
    this.alpha.value = alpha
    _selectedColor.value = selectedColor.value.copy(alpha = alpha)
    return true
  }

  /** Combine the brightness value to the selected pure color. */
  private fun setBrightness(brightness: Float): Boolean {
    if (!enabled || this.brightness.value == brightness) { return false }
    this.brightness.value = brightness
    val (h, s, _) = pureSelectedColor.value.toHSV()
    _selectedColor.value = Color.hsv(h, s, brightness, alpha.value)
    return true
  }

  /** Return a [Color] that is applied with HSV color factors to the [color]. */
  private fun applyHSVFactors(color: Color): Color {
    val (h, s, v) = color.toHSV()
    val actualV = if (isAttachedBrightnessSlider) brightness.value else v
    return Color.hsv(h, s, actualV, if (isAttachedAlphaSlider) alpha.value else 1f)
  }

  internal fun releaseBitmap() {
    wheelBitmap = null
  }
}
