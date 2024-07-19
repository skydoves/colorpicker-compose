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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
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
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@Composable
fun PickColour() {
  val controller = rememberColorPickerController()
  var currentColorEnvelope by remember {
    mutableStateOf(
      ColorEnvelope(
        Color.Green,
        "ff00ff00",
        false,
      ),
    )
  }

  Column(
    modifier = Modifier.fillMaxSize().background(Color.Black),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    HsvColorPicker(
      initialColor = currentColorEnvelope.color,
      modifier = Modifier.padding(10.dp).size(300.dp),
      controller = controller,
      onColorChanged = { colorEnvelope: ColorEnvelope ->
        currentColorEnvelope = colorEnvelope
      },
    )

    Spacer(Modifier.height(8.dp))

    AlphaSlider(
      modifier = Modifier
        .height(35.dp)
        .width(400.dp),
      controller = controller,
    )

    Spacer(Modifier.height(8.dp))

    BrightnessSlider(
      modifier = Modifier
        .height(35.dp)
        .width(400.dp),
      controller = controller,
    )

    Spacer(Modifier.height(8.dp))

    SelectionContainer {
      Text(
        currentColorEnvelope.hexCode,
        color = currentColorEnvelope.color,
      )
    }

    AlphaTile(
      modifier = Modifier
        .size(80.dp)
        .clip(RoundedCornerShape(6.dp)),
      controller = controller,
    )
  }
}
