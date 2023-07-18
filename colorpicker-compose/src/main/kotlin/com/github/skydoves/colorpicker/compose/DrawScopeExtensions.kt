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

import android.graphics.PointF
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

private const val SELECTOR_RADIUS: Float = 50f
private const val BORDER_WIDTH: Float = 10f

public fun DrawScope.drawColorIndicator(pos: PointF, color: Color) {
    drawCircle(color, SELECTOR_RADIUS, Offset(pos.x, pos.y))
    drawCircle(
        Color.White,
        SELECTOR_RADIUS - (BORDER_WIDTH / 2),
        Offset(pos.x, pos.y),
        style = Stroke(width = BORDER_WIDTH),
    )
    drawCircle(
        Color.LightGray,
        SELECTOR_RADIUS,
        Offset(pos.x, pos.y),
        style = Stroke(width = 1f),
    )
}
