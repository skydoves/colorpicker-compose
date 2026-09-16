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
fun ApiSaturationSliderScreen() {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(32.dp),
  ) {
    Text(
      text = "SaturationSlider",
      style = DocsTheme.typography.h1,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "A slider component that allows users to adjust the saturation component of " +
        "the selected color. The slider displays a gradient of the currently selected hue, " +
        "running from fully washed out on one end to fully saturated on the other. When a " +
        "BrightnessSlider is attached to the same controller, the gradient is drawn at the " +
        "current brightness as well.",
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
      code = """SaturationSlider(
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
      description = "Modifier to be applied to the slider (default: Modifier)",
    )
    ParameterItem(
      name = "controller",
      type = "ColorPickerController",
      description = "Controller that manages the color picker state (required)",
    )
    ParameterItem(
      name = "borderRadius",
      type = "Dp",
      description = "Corner radius of the slider border (default: 6.dp)",
    )
    ParameterItem(
      name = "borderSize",
      type = "Dp",
      description = "Thickness of the slider border (default: 5.dp)",
    )
    ParameterItem(
      name = "borderColor",
      type = "Color",
      description = "Color of the slider border (default: Color.LightGray)",
    )
    ParameterItem(
      name = "wheelImageBitmap",
      type = "ImageBitmap?",
      description = "Custom image drawn instead of the default circular wheel (default: null)",
    )
    ParameterItem(
      name = "wheelRadius",
      type = "Dp",
      description = "Radius of the slider wheel indicator (default: 12.dp)",
    )
    ParameterItem(
      name = "wheelColor",
      type = "Color",
      description = "Color of the wheel indicator (default: Color.White)",
    )
    ParameterItem(
      name = "wheelAlpha",
      type = "Float",
      description = "Alpha value applied to the wheel indicator (default: 1.0f)",
    )
    ParameterItem(
      name = "wheelPaint",
      type = "Paint",
      description = "Paint used to draw the wheel indicator " +
        "(default: a Paint built from wheelColor and wheelAlpha)",
    )
    ParameterItem(
      name = "initialColor",
      type = "Color?",
      description = "Color the slider starts on. When null, the slider follows the " +
        "controller's current color (default: null)",
    )
    ParameterItem(
      name = "orientation",
      type = "SliderOrientation",
      description = "Whether the slider runs left to right (Horizontal) or bottom to top " +
        "(Vertical). A horizontal slider fills the width it is given, a vertical one fills " +
        "the height (default: SliderOrientation.Horizontal)",
    )
    ParameterItem(
      name = "onColorChanged",
      type = "(ColorEnvelope) -> Unit",
      description = "Callback invoked with the ColorEnvelope the controller ends up on, " +
        "carrying the color, its hex code, whether the change came from the user, and the " +
        "ColorChangeSource. This is what makes the slider usable on its own, without a " +
        "picker beside it (default: no-op)",
    )
    ParameterItem(
      name = "onStart",
      type = "() -> Unit",
      description = "Callback invoked when user interaction with the slider starts " +
        "(default: no-op)",
    )
    ParameterItem(
      name = "onFinish",
      type = "() -> Unit",
      description = "Callback invoked when user interaction with the slider ends " +
        "(default: no-op)",
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
      code = """SaturationSlider(
    modifier = Modifier
        .fillMaxWidth()
        .height(35.dp),
    controller = controller,
    borderRadius = 6.dp,
    borderSize = 5.dp,
    borderColor = Color.LightGray,
    wheelRadius = 30.dp,
    wheelColor = Color.White,
    wheelAlpha = 1.0f,
)""",
    )

    Spacer(modifier = Modifier.height(24.dp))

    Text(
      text = "Set orientation to SliderOrientation.Vertical to run the slider bottom to " +
        "top instead. A vertical slider fills the height it is given, so give it a height " +
        "and a width:",
      style = DocsTheme.typography.body,
      color = DocsTheme.colors.onSurface,
    )

    Spacer(modifier = Modifier.height(16.dp))

    CodeBlock(
      code = """SaturationSlider(
    modifier = Modifier
        .height(250.dp)
        .width(35.dp),
    controller = controller,
    orientation = SliderOrientation.Vertical,
)""",
    )

    Spacer(modifier = Modifier.height(24.dp))

    Text(
      text = "Because onColorChanged hands you a full ColorEnvelope, the slider can stand " +
        "alone as the only color control on the screen:",
      style = DocsTheme.typography.body,
      color = DocsTheme.colors.onSurface,
    )

    Spacer(modifier = Modifier.height(16.dp))

    CodeBlock(
      code = """SaturationSlider(
    modifier = Modifier
        .fillMaxWidth()
        .height(35.dp),
    controller = controller,
    onColorChanged = { colorEnvelope: ColorEnvelope ->
        selectedColor = colorEnvelope.color
        hexCode = colorEnvelope.hexCode
    },
    onStart = { isDragging = true },
    onFinish = { isDragging = false },
)""",
    )

    Spacer(modifier = Modifier.height(32.dp))
  }
}

@Composable
private fun ParameterItem(name: String, type: String, description: String) {
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
