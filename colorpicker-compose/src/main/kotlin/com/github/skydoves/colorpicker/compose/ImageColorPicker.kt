/*
 * Copyright (C) 2022 skydoves (Jaewoong Eum)
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

import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.RectF
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.toSize
import androidx.core.util.Pools
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

/**
 * ImageColorPicker allows you to get colors from any images by tapping on the desired color.
 *
 * @param modifier [Modifier] to decorate the internal Canvas.
 * @param controller Allows you to control and interacts with color pickers and all relevant subcomponents.
 * @param paletteImageBitmap [ImageBitmap] to draw the palette.
 * @param wheelImageBitmap [ImageBitmap] to draw the wheel.
 * @param drawOnPosSelected to draw anything on the canvas when [ColorPickerController.selectedPoint] changes
 * @param drawThumbIndicator should the indicator be drawn on the canvas. Defaults to false if either [wheelImageBitmap] or [drawOnPosSelected] are not null.
 * @param paletteContentScale Represents a rule to apply to scale a source rectangle to be inscribed into a destination.
 * @param onColorChanged Color changed listener.
 */
@Composable
public fun ImageColorPicker(
    modifier: Modifier,
    controller: ColorPickerController,
    paletteImageBitmap: ImageBitmap,
    wheelImageBitmap: ImageBitmap? = null,
    drawOnPosSelected: (DrawScope.(selectedPos: PointF) -> Unit)? = null,
    drawThumbIndicator: Boolean = wheelImageBitmap == null && drawOnPosSelected == null,
    paletteContentScale: PaletteContentScale = PaletteContentScale.FIT,
    onColorChanged: ((colorEnvelope: ColorEnvelope) -> Unit)? = null,
) {
    val coroutineScope = rememberCoroutineScope()
    DisposableEffect(key1 = controller) {
        coroutineScope.launch(Dispatchers.Main) {
            with(controller) {
                setPaletteContentScale(paletteContentScale)
                setPaletteImageBitmap(paletteImageBitmap)
                setWheelImageBitmap(wheelImageBitmap)
                colorChangedTick.mapNotNull { it }.collect {
                    onColorChanged?.invoke(it)
                }
            }
        }

        onDispose {
            controller.releaseBitmap()
        }
    }

    Canvas(
        modifier = modifier
            .onSizeChanged { size ->
                if (size.width != 0 && size.height != 0) {
                    controller.canvasSize.value = size
                }
            }
            .pointerInteropFilter { event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN,
                    MotionEvent.ACTION_MOVE,
                    MotionEvent.ACTION_UP,
                    -> {
                        controller.selectByCoordinate(event.x, event.y, true)
                        true
                    }
                    else -> false
                }
            },
    ) {
        drawIntoCanvas { canvas ->
            // draw image bitmap on the canvas.
            controller.paletteBitmap?.let { imageBitmap ->
                var dx = 0f
                var dy = 0f
                val scale: Float
                val shaderMatrix = Matrix()
                val shader = ImageShader(imageBitmap, TileMode.Clamp)
                val brush = ShaderBrush(shader)
                val paint = paintPool.acquire() ?: Paint()
                paint.asFrameworkPaint().apply {
                    isAntiAlias = true
                    isDither = true
                    isFilterBitmap = true
                }

                // cache the paint in the internal stack.
                canvas.saveLayer(size.toRect(), paint)

                val mDrawableRect = RectF(0f, 0f, size.width, size.height)
                val bitmapWidth: Int = imageBitmap.asAndroidBitmap().width
                val bitmapHeight: Int = imageBitmap.asAndroidBitmap().height

                if (bitmapWidth * mDrawableRect.height() > mDrawableRect.width() * bitmapHeight) {
                    scale = mDrawableRect.height() / bitmapHeight.toFloat()
                    dx = (mDrawableRect.width() - bitmapWidth * scale) * 0.5f
                } else {
                    scale = mDrawableRect.width() / bitmapWidth.toFloat()
                    dy = (mDrawableRect.height() - bitmapHeight * scale) * 0.5f
                }

                // resize the matrix to scale by sx and sy.
                shaderMatrix.setScale(scale, scale)

                // post translate the matrix with the specified translation.
                shaderMatrix.postTranslate(
                    (dx + 0.5f) + mDrawableRect.left,
                    (dy + 0.5f) + mDrawableRect.top,
                )
                // apply the scaled matrix to the shader.
                shader.setLocalMatrix(shaderMatrix)
                // Set the shader matrix to the controller.
                controller.imageBitmapMatrix.value = shaderMatrix
                // draw an image bitmap as a rect.
                drawRect(brush = brush, size = controller.canvasSize.value.toSize())
                // restore canvas.
                canvas.restore()
                // resets the paint and release to the pool.
                paint.asFrameworkPaint().reset()
                paintPool.release(paint)
            }

            // draw wheel bitmap on the canvas.
            val point = controller.selectedPoint.value
            val wheelBitmap = controller.wheelBitmap
            if (wheelBitmap != null) {
                canvas.drawImage(
                    wheelBitmap,
                    Offset(point.x - wheelBitmap.width / 2, point.y - wheelBitmap.height / 2),
                    Paint(),
                )
            }

            if (drawThumbIndicator) {
                canvas.drawCircle(
                    Offset(point.x, point.y),
                    controller.wheelRadius.toPx(),
                    controller.wheelPaint,
                )
            }

            if (drawOnPosSelected != null) {
                this.drawOnPosSelected(controller.selectedPoint.value)
            }
        }
        controller.reviseTick.value
    }
}

/** paint pool which caching and reusing [Paint] instances. */
private val paintPool = Pools.SimplePool<Paint>(2)
