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

import android.graphics.BitmapShader
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.Shader
import android.graphics.drawable.Drawable
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.unit.Dp

/**
 * AlphaTileDrawable displays ARGB colors including transparency with tiles on a canvas.
 *
 * @param tileSize DP size of tiles.
 * @param tileOddColor Color of the odd tiles.
 * @param tileEvenColor Color of the even tiles.
 */
public class AlphaTileDrawable constructor(
    tileSize: Dp,
    tileOddColor: Color,
    tileEvenColor: Color,
) : Drawable() {

    private val androidPaint: android.graphics.Paint = android.graphics.Paint(
        android.graphics.Paint.ANTI_ALIAS_FLAG
    )

    init {
        val sizeF = tileSize.value
        val size = sizeF.toInt()
        val imageBitmap = ImageBitmap(size * 2, size * 2, ImageBitmapConfig.Argb8888)
        val canvas = Canvas(imageBitmap)
        val rect = Rect(0f, 0f, sizeF, sizeF)

        val bitmapPaint = Paint().apply {
            style = PaintingStyle.Fill
            isAntiAlias = true
        }

        bitmapPaint.color = tileOddColor
        drawTile(canvas, rect, bitmapPaint, 0f, 0f)
        drawTile(canvas, rect, bitmapPaint, sizeF, sizeF)

        bitmapPaint.color = tileEvenColor
        drawTile(canvas, rect, bitmapPaint, 0f, sizeF)
        drawTile(canvas, rect, bitmapPaint, sizeF, 0f)

        androidPaint.shader = BitmapShader(
            imageBitmap.asAndroidBitmap(),
            Shader.TileMode.REPEAT,
            Shader.TileMode.REPEAT
        )
    }

    private fun drawTile(canvas: Canvas, rect: Rect, paint: Paint, dx: Float, dy: Float) {
        val translated = rect.translate(dx, dy)
        canvas.drawRect(translated, paint)
    }

    override fun draw(canvas: android.graphics.Canvas) {
        canvas.drawPaint(androidPaint)
    }

    override fun setAlpha(alpha: Int) {
        androidPaint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        androidPaint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int = PixelFormat.OPAQUE
}
