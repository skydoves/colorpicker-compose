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
package com.github.skydoves.colorpickercomposedemo.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import colorpickercomposedemo.app.generated.resources.Res
import colorpickercomposedemo.app.generated.resources.palettebar
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.ImageColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.github.skydoves.colorpickercomposedemo.PhotoPickerIcon
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun ImageColorPickerScreen() {
  val controller = rememberColorPickerController()
  var hexCode by remember { mutableStateOf("") }
  var textColor by remember { mutableStateOf(Color.Transparent) }

  Column {
    Spacer(modifier = Modifier.weight(1f))

    PhotoPickerIcon(controller)

    ImageColorPicker(
      modifier = Modifier
        .testTag("ImageColorPicker")
        .fillMaxWidth()
        .height(200.dp)
        .padding(10.dp),
      controller = controller,
      paletteImageBitmap = imageResource(Res.drawable.palettebar),
      onColorChanged = { colorEnvelope: ColorEnvelope ->
        hexCode = colorEnvelope.hexCode
        textColor = colorEnvelope.color
      },
      previewImagePainter = painterResource(Res.drawable.palettebar),
    )

    Spacer(modifier = Modifier.weight(5f))

    AlphaSlider(
      modifier = Modifier
        .testTag("ImageColorPicker_AlphaSlider")
        .fillMaxWidth()
        .padding(10.dp)
        .height(35.dp)
        .align(Alignment.CenterHorizontally),
      controller = controller,
    )

    BrightnessSlider(
      modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
        .height(35.dp)
        .align(Alignment.CenterHorizontally),
      controller = controller,
    )

    Spacer(modifier = Modifier.weight(3f))

    Text(
      text = "#$hexCode",
      color = textColor,
      fontSize = 16.sp,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.align(Alignment.CenterHorizontally),
    )

    AlphaTile(
      modifier = Modifier
        .size(80.dp)
        .clip(RoundedCornerShape(6.dp))
        .align(Alignment.CenterHorizontally),
      controller = controller,
    )

    Spacer(modifier = Modifier.height(50.dp))
  }
}
