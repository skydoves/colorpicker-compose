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
package docs.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import docs.component.CodeBlock
import docs.theme.DocsTheme

@Composable
fun ApiAlphaSliderScreen() {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(32.dp),
  ) {
    Text(
      text = "AlphaSlider",
      style = DocsTheme.typography.h1,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "A slider component that allows users to adjust the alpha (transparency) " +
        "value of the selected color. The slider displays a gradient from transparent " +
        "to the current color.",
      style = DocsTheme.typography.body,
      color = DocsTheme.colors.onSurfaceVariant,
    )

    Spacer(modifier = Modifier.height(32.dp))

    // Basic Usage
    Text(
      text = "Basic Usage",
      style = DocsTheme.typography.h2,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(16.dp))

    CodeBlock(
      code = """AlphaSlider(
    modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
        .height(35.dp),
    controller = controller,
)""",
    )

    Spacer(modifier = Modifier.height(32.dp))

    // Parameters
    Text(
      text = "Parameters",
      style = DocsTheme.typography.h2,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(16.dp))

    ParameterItem(
      name = "modifier",
      type = "Modifier",
      description = "Modifier to be applied to the slider",
    )
    ParameterItem(
      name = "controller",
      type = "ColorPickerController",
      description = "Controller that manages the color picker state",
    )
    ParameterItem(
      name = "borderRadius",
      type = "Dp",
      description = "Corner radius of the slider border",
    )
    ParameterItem(
      name = "borderSize",
      type = "Dp",
      description = "Thickness of the slider border",
    )
    ParameterItem(
      name = "borderColor",
      type = "Color",
      description = "Color of the slider border",
    )
    ParameterItem(
      name = "wheelRadius",
      type = "Dp",
      description = "Radius of the slider wheel indicator",
    )
    ParameterItem(
      name = "wheelColor",
      type = "Color",
      description = "Color of the wheel indicator",
    )
    ParameterItem(
      name = "wheelImageBitmap",
      type = "ImageBitmap?",
      description = "Custom image for the wheel indicator",
    )
    ParameterItem(
      name = "tileOddColor",
      type = "Color",
      description = "Color of odd tiles in the transparency pattern",
    )
    ParameterItem(
      name = "tileEvenColor",
      type = "Color",
      description = "Color of even tiles in the transparency pattern",
    )
    ParameterItem(
      name = "tileSize",
      type = "Dp",
      description = "Size of each tile in the transparency pattern",
    )

    Spacer(modifier = Modifier.height(32.dp))

    // Customization
    Text(
      text = "Customization",
      style = DocsTheme.typography.h2,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "You can customize the appearance of the slider:",
      style = DocsTheme.typography.body,
      color = DocsTheme.colors.onSurface,
    )

    Spacer(modifier = Modifier.height(16.dp))

    CodeBlock(
      code = """AlphaSlider(
    modifier = Modifier
        .fillMaxWidth()
        .height(35.dp),
    controller = controller,
    borderRadius = 6.dp,
    borderSize = 5.dp,
    borderColor = Color.LightGray,
    wheelRadius = 30.dp,
    wheelColor = Color.White,
    tileOddColor = Color.White,
    tileEvenColor = Color.LightGray,
    tileSize = 30.dp,
)""",
    )

    Spacer(modifier = Modifier.height(32.dp))
  }
}

@Composable
private fun ParameterItem(
  name: String,
  type: String,
  description: String,
) {
  Column(
    modifier = Modifier.padding(vertical = 8.dp),
  ) {
    Text(
      text = "$name: $type",
      style = DocsTheme.typography.body,
      color = DocsTheme.colors.primary,
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = description,
      style = DocsTheme.typography.bodySmall,
      color = DocsTheme.colors.onSurfaceVariant,
    )
  }
}
