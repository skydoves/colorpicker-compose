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
import kotlinx.coroutines.flow.StateFlow
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
      val previous = field
      if (value == previous) {
        return
      }
      field = value

      // Nothing has been laid out yet, so there is no old point to carry over. Scaling by the zero
      // size used to produce Offset(NaN, NaN), which reads as Offset.Unspecified, and the canvas
      // threw as soon as it tried to draw the wheel there.
      if (previous.width == 0f || previous.height == 0f) {
        _selectedPoint.value = value.center
        return
      }

      val current = _selectedPoint.value
      val scaled = Offset(
        current.x * value.width / previous.width,
        current.y * value.height / previous.height,
      )
      // Scaling each axis on its own distorts anything round, so hand the point back to the picker
      // and let it say where that actually lands on the new canvas.
      _selectedPoint.value = coordToColor?.invoke(scaled)?.second ?: scaled
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

  /** Saturation value to be applied with the selected color. */
  internal var saturation: MutableState<Float> = mutableFloatStateOf(1.0f)

  /** An [ImageBitmap] to be drawn on the canvas as a palette. */
  private var _paletteBitmap: MutableStateFlow<ImageBitmap?> = MutableStateFlow(null)
  public val paletteBitmap: StateFlow<ImageBitmap?> = _paletteBitmap

  private val _wheelBitmap: MutableState<ImageBitmap?> = mutableStateOf(null)

  /** An [ImageBitmap] to be drawn on the canvas as a wheel. */
  public var wheelBitmap: ImageBitmap?
    get() = _wheelBitmap.value
    set(value) {
      _wheelBitmap.value = value
    }

  internal val _debounceDuration: MutableState<Long?> = mutableStateOf(null)

  /** A debounce duration for observing color changes. */
  public var debounceDuration: Long?
    get() = _debounceDuration.value
    set(value) {
      _debounceDuration.value = value
    }

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
    set(value) {
      _enabled.value = value
    }

  /** Indicates if the alpha slider has been attached. */
  internal var isAttachedAlphaSlider: Boolean = false

  /** Indicates if the brightness slider has been attached. */
  internal var isAttachedBrightnessSlider: Boolean = false

  /** Whether a SaturationSlider is attached. */
  internal var isAttachedSaturationSlider: Boolean = false

  internal var reviseTick = mutableIntStateOf(0)

  private var _colorFlow = MutableStateFlow<ColorEnvelope?>(null)

  @OptIn(FlowPreview::class)
  public fun getColorFlow(debounceDuration: Long = 0): Flow<ColorEnvelope> =
    _colorFlow.filterNotNull().debounce(this.debounceDuration ?: debounceDuration)

  // Function that takes a coordinate and obtains a color
  // Also returns an adjusted coordinate if appropriate, or null when the coordinate names nothing
  // selectable, such as the band beside a letterboxed palette or a transparent pixel
  private var coordToColor: ((Offset) -> Pair<Color, Offset>?)? = null

  /** True once a picker has registered itself and the canvas has a size to work with. */
  private val isReady: Boolean
    get() = coordToColor != null && canvasSize != Size.Zero

  /** Set on the first [setup] call, so later ones leave the current selection alone. */
  private var isSetUp: Boolean = false

  /** A color asked for before the picker was ready, replayed once [setup] runs. */
  private var pendingColor: Color? = null

  /**
   * Setup the controller for use by a picker. The initial position is the
   * initial value selected by the picker. The coordinateToColor function
   * is used to get the color at a given coordinate. The function should
   * return the color at the coordinate and the adjusted coordinate if
   * the coordinate was out of bounds, or null if the coordinate has no
   * color to offer.
   *
   * A picker re-runs this whenever its palette changes, and a palette rebuilt inside the
   * composition changes on every recomposition, so only the first call gets to move the selection.
   */
  internal fun setup(
    initialPosition: Offset = canvasSize.center,
    coordinateToColor: (Offset) -> Pair<Color, Offset>?,
  ) {
    this.coordToColor = coordinateToColor
    val position = if (isSetUp) _selectedPoint.value else initialPosition
    isSetUp = true
    selectByCoordinate(position, fromUser = false)
    pendingColor?.let { color ->
      pendingColor = null
      selectByColor(color, fromUser = false)
    }
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
  public fun selectByCoordinate(
    point: Offset,
    fromUser: Boolean,
    source: ColorChangeSource = ColorChangeSource.Programmatic,
  ) {
    if (selectByCoordinate(point)) {
      // notify color changes to the listeners.
      notifyColorChanged(fromUser, source)
    }
  }

  /**
   * Select center point of the palette.
   *
   * @param fromUser Represents this event is triggered by user or not.
   */
  public fun selectCenter(fromUser: Boolean) {
    selectByCoordinate(canvasSize.center, fromUser)
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
    // Callers reach for this from a LaunchedEffect, which runs before the picker has been laid out.
    // Without a canvas there is no coordinate to map the color onto, so hold it until there is one.
    if (!isReady) {
      pendingColor = Color.hsv(h, s, v, alpha)
      return
    }
    var changed = selectByCoordinate(hsvToCoord(h, s, canvasSize.center))
    changed = setAlpha(alpha) || changed
    changed = setBrightness(v) || changed
    if (changed) {
      notifyColorChanged(fromUser)
    }
  }

  /** Combine the alpha value to the selected pure color. */
  public fun setAlpha(
    alpha: Float,
    fromUser: Boolean,
    source: ColorChangeSource = ColorChangeSource.Programmatic,
  ) {
    if (setAlpha(alpha)) {
      notifyColorChanged(fromUser, source)
    }
  }

  /** Combine the hue value to the selected pure color. */
  public fun setHue(
    hue: Float,
    fromUser: Boolean,
    source: ColorChangeSource = ColorChangeSource.Programmatic,
  ) {
    if (setHue(hue)) {
      notifyColorChanged(fromUser, source)
    }
  }

  /** Combine the brightness value to the selected pure color. */
  public fun setBrightness(
    brightness: Float,
    fromUser: Boolean,
    source: ColorChangeSource = ColorChangeSource.Programmatic,
  ) {
    if (setBrightness(brightness)) {
      notifyColorChanged(fromUser, source)
    }
  }

  /** Combine the saturation value to the selected pure color. */
  public fun setSaturation(
    saturation: Float,
    fromUser: Boolean,
    source: ColorChangeSource = ColorChangeSource.Programmatic,
  ) {
    if (setSaturation(saturation)) {
      notifyColorChanged(fromUser, source)
    }
  }

  /** An envelope for the color showing right now, for reporting the end of a gesture. */
  internal fun currentEnvelope(source: ColorChangeSource): ColorEnvelope {
    val color = _selectedColor.value
    return ColorEnvelope(color, color.hexCode, fromUser = true, source = source)
  }

  /** Notify color changes to the color picker and other subcomponents. */
  private fun notifyColorChanged(
    fromUser: Boolean,
    source: ColorChangeSource = ColorChangeSource.Programmatic,
  ) {
    val color = _selectedColor.value
    _colorFlow.value = ColorEnvelope(color, color.hexCode, fromUser, source)
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
    val (color, newPoint) = coordToColor(point) ?: return false
    _selectedPoint.value = newPoint
    if (pureSelectedColor.value == color) return false
    _selectedColor.value = applyHSVFactors(color)
    pureSelectedColor.value = color
    return true
  }

  /** Combine the alpha value to the selected pure color. */
  private fun setAlpha(alpha: Float): Boolean {
    if (!enabled || this.alpha.value == alpha) {
      return false
    }
    this.alpha.value = alpha
    _selectedColor.value = selectedColor.value.copy(alpha = alpha)
    return true
  }

  private fun setHue(hue: Float): Boolean {
    if (!enabled) {
      return false
    }
    val color = Color.hsv(hue * 360f, 1f, 1f)
    _selectedColor.value = applyHSVFactors(color)
    pureSelectedColor.value = color
    return true
  }

  /** Combine the brightness value to the selected pure color. */
  private fun setBrightness(brightness: Float): Boolean {
    if (!enabled || this.brightness.value == brightness) {
      return false
    }
    this.brightness.value = brightness
    val (h, s, _) = pureSelectedColor.value.toHSV()
    val actualS = if (isAttachedSaturationSlider) saturation.value else s
    _selectedColor.value = Color.hsv(h, actualS, brightness, alpha.value)
    return true
  }

  private fun setSaturation(saturation: Float): Boolean {
    if (!enabled || this.saturation.value == saturation) {
      return false
    }
    this.saturation.value = saturation
    val (h, _, v) = pureSelectedColor.value.toHSV()
    val actualV = if (isAttachedBrightnessSlider) brightness.value else v
    _selectedColor.value = Color.hsv(h, saturation, actualV, alpha.value)
    return true
  }

  /** Return a [Color] that is applied with HSV color factors to the [color]. */
  private fun applyHSVFactors(color: Color): Color {
    val (h, s, v) = color.toHSV()
    val actualS = if (isAttachedSaturationSlider) saturation.value else s
    val actualV = if (isAttachedBrightnessSlider) brightness.value else v
    return Color.hsv(h, actualS, actualV, if (isAttachedAlphaSlider) alpha.value else 1f)
  }

  /** Set an [ImageBitmap] to draw on the canvas as a palette. */
  public fun setPaletteImageBitmap(imageBitmap: ImageBitmap) {
    val targetSize = imageBitmap.takeIf { it.width != 0 && it.height != 0 }
      ?: throw RuntimeException(
        "Can't set an ImageBitmap before initializing the canvas",
      )
    canvasSize = Size(targetSize.width.toFloat(), targetSize.height.toFloat())
    _paletteBitmap.value = imageBitmap
    selectCenter(fromUser = false)
    reviseTick.intValue++
  }

  internal fun releaseBitmap() {
    wheelBitmap = null
    _paletteBitmap.value = null
  }
}
