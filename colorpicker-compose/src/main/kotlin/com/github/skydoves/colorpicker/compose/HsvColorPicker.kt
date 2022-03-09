/*
 * Copyright (C) 2022 skydoves
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
import android.graphics.RectF
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

/**
 * HsvColorPicker allows you to get colors from HSV color palette by tapping on the desired color.
 *
 * @param modifier [Modifier] to decorate the internal Canvas.
 * @param controller Allows you to control and interacts with color pickers and all relevant subcomponents.
 * @param wheelImageBitmap [ImageBitmap] to draw the wheel.
 * @param onColorChanged Color changed listener.
 */
@Composable
public fun HsvColorPicker(
    modifier: Modifier,
    controller: ColorPickerController,
    wheelImageBitmap: ImageBitmap? = null,
    onColorChanged: ((colorEnvelope: ColorEnvelope) -> Unit)? = null
) {
    val context = LocalContext.current
    var hsvBitmapDrawable: HsvBitmapDrawable? = null
    var bitmap: ImageBitmap? = null
    val coroutineScope = rememberCoroutineScope()
    DisposableEffect(key1 = controller) {
        coroutineScope.launch(Dispatchers.Main) {
            controller.isHsvColorPalette = true
            bitmap?.let { controller.setPaletteImageBitmap(it) }
            controller.setWheelImageBitmap(wheelImageBitmap)
            controller.colorChangedTick.mapNotNull { it }.collect {
                onColorChanged?.invoke(it)
            }
        }

        onDispose {
            controller.releaseBitmap()
        }
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged { newSize ->
                val size =
                    newSize.takeIf { it.width != 0 && it.height != 0 } ?: return@onSizeChanged
                controller.canvasSize.value = size
                bitmap
                    ?.asAndroidBitmap()
                    ?.recycle()
                bitmap = ImageBitmap(size.width, size.height, ImageBitmapConfig.Argb8888).also {
                    hsvBitmapDrawable =
                        HsvBitmapDrawable(context.resources, it.asAndroidBitmap()).apply {
                            setBounds(
                                0,
                                0,
                                size.width,
                                size.height
                            )
                        }

                    var dx = 0f
                    var dy = 0f
                    val scale: Float
                    val shaderMatrix = Matrix()
                    val mDrawableRect = RectF(0f, 0f, size.width.toFloat(), size.height.toFloat())
                    val bitmapWidth: Int = it.asAndroidBitmap().width
                    val bitmapHeight: Int = it.asAndroidBitmap().height

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
                        (dy + 0.5f) + mDrawableRect.top
                    )

                    // set the shader matrix to the controller.
                    controller.imageBitmapMatrix.value = shaderMatrix
                }
            }
            .pointerInteropFilter { event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN,
                    MotionEvent.ACTION_MOVE,
                    MotionEvent.ACTION_UP -> {
                        controller.selectByCoordinate(event.x, event.y, true)
                        true
                    }
                    else -> false
                }
            }
    ) {
        drawIntoCanvas { canvas ->
            // draw hsv bitmap on the canvas.
            hsvBitmapDrawable?.draw(canvas.nativeCanvas)

            // draw wheel bitmap on the canvas.
            val point = controller.selectedPoint.value
            val wheelBitmap = controller.wheelBitmap
            if (wheelBitmap == null) {
                canvas.drawCircle(
                    Offset(point.x, point.y),
                    controller.wheelRadius.value,
                    controller.wheelPaint
                )
            } else {
                canvas.drawImage(
                    wheelBitmap,
                    Offset(point.x - wheelBitmap.width / 2, point.y - wheelBitmap.height / 2),
                    Paint()
                )
            }
        }
        controller.reviseTick.value
    }
}
