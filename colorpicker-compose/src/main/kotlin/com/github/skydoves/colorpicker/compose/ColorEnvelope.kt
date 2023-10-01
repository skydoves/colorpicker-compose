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

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Data transfer object that includes updated color factors.
 *
 * @param color ARGB color value.
 * @param hexCode Color hex code, which represents [color] value.
 * @param fromUser Represents this event is triggered by user or not.
 */
@Immutable
public data class ColorEnvelope(
  val color: Color,
  val hexCode: String,
  val fromUser: Boolean,
)
