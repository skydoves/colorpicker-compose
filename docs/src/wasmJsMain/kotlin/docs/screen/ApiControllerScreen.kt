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
fun ApiControllerScreen() {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(32.dp),
  ) {
    Text(
      text = "ColorPickerController",
      style = DocsTheme.typography.h1,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "The central controller that manages state and coordinates all color picker " +
        "components. Create it using rememberColorPickerController() and pass it to " +
        "all related components.",
      style = DocsTheme.typography.body,
      color = DocsTheme.colors.onSurfaceVariant,
    )

    Spacer(modifier = Modifier.height(32.dp))

    // Creating Controller
    Text(
      text = "Creating a Controller",
      style = DocsTheme.typography.h2,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(16.dp))

    CodeBlock(
      code = """val controller = rememberColorPickerController()""",
    )

    Spacer(modifier = Modifier.height(32.dp))

    // Properties
    Text(
      text = "Properties",
      style = DocsTheme.typography.h2,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(16.dp))

    ParameterItem(
      name = "selectedColor",
      type = "State<Color>",
      description = "The currently selected color with alpha and brightness applied",
    )
    ParameterItem(
      name = "selectedPoint",
      type = "State<Offset>",
      description = "The currently selected coordinate on the picker",
    )
    ParameterItem(
      name = "enabled",
      type = "Boolean",
      description = "Enable or disable color selection",
    )
    ParameterItem(
      name = "debounceDuration",
      type = "Long?",
      description = "Debounce duration for color change events (ms)",
    )
    ParameterItem(
      name = "wheelRadius",
      type = "Dp",
      description = "Radius of the wheel indicator",
    )
    ParameterItem(
      name = "wheelColor",
      type = "Color",
      description = "Color of the wheel indicator",
    )
    ParameterItem(
      name = "wheelAlpha",
      type = "Float",
      description = "Alpha value of the wheel indicator",
    )

    Spacer(modifier = Modifier.height(32.dp))

    // Wheel Customization
    Text(
      text = "Wheel Customization",
      style = DocsTheme.typography.h2,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(16.dp))

    CodeBlock(
      code = """controller.setWheelRadius(40.dp)
controller.setWheelColor(Color.Blue)
controller.setWheelAlpha(0.5f)
controller.setWheelImageBitmap(imageBitmap)""",
    )

    Spacer(modifier = Modifier.height(32.dp))

    // Point Selection
    Text(
      text = "Programmatic Selection",
      style = DocsTheme.typography.h2,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "Select colors programmatically using these methods:",
      style = DocsTheme.typography.body,
      color = DocsTheme.colors.onSurface,
    )

    Spacer(modifier = Modifier.height(16.dp))

    CodeBlock(
      code = """// Select by coordinate
controller.selectByCoordinate(x = 100f, y = 100f, fromUser = false)

// Select center of the palette
controller.selectCenter(fromUser = false)

// Select by color
controller.selectByColor(Color.Red, fromUser = false)

// Select by HSV values
controller.selectByHsv(h = 180f, s = 0.8f, v = 1f, alpha = 1f, fromUser = false)""",
    )

    Spacer(modifier = Modifier.height(32.dp))

    // Color Flow
    Text(
      text = "Observing Color Changes",
      style = DocsTheme.typography.h2,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "Use getColorFlow() to observe color changes with optional debouncing:",
      style = DocsTheme.typography.body,
      color = DocsTheme.colors.onSurface,
    )

    Spacer(modifier = Modifier.height(16.dp))

    CodeBlock(
      code = """// Collect color changes
LaunchedEffect(controller) {
    controller.getColorFlow(debounceDuration = 200L)
        .collect { colorEnvelope ->
            // Handle color update
        }
}""",
    )

    Spacer(modifier = Modifier.height(32.dp))

    // Debounce
    Text(
      text = "Debouncing",
      style = DocsTheme.typography.h2,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "Set debounce duration to reduce overhead when communicating with " +
        "external devices or performing heavy operations:",
      style = DocsTheme.typography.body,
      color = DocsTheme.colors.onSurface,
    )

    Spacer(modifier = Modifier.height(16.dp))

    CodeBlock(
      code = """controller.debounceDuration = 200L // 200ms debounce""",
    )

    Spacer(modifier = Modifier.height(32.dp))

    // Enable/Disable
    Text(
      text = "Enable/Disable",
      style = DocsTheme.typography.h2,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(16.dp))

    CodeBlock(
      code = """// Disable color selection
controller.enabled = false

// Re-enable
controller.enabled = true""",
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
