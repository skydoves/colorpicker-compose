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
import docs.component.Callout
import docs.component.CalloutType
import docs.component.CodeBlock
import docs.theme.DocsTheme

@Composable
fun ApiHsvColorPickerScreen() {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(32.dp),
  ) {
    Text(
      text = "HsvColorPicker",
      style = DocsTheme.typography.h1,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "A circular HSV (Hue-Saturation-Value) color wheel that allows users to " +
        "select colors by tapping or dragging on the wheel.",
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
      code = """HsvColorPicker(
    modifier = Modifier
        .fillMaxWidth()
        .height(450.dp)
        .padding(10.dp),
    controller = controller,
    onColorChanged = { colorEnvelope: ColorEnvelope ->
        // Handle color change
    }
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
      description = "Modifier to be applied to the color picker",
    )
    ParameterItem(
      name = "controller",
      type = "ColorPickerController",
      description = "Controller that manages the color picker state",
    )
    ParameterItem(
      name = "initialColor",
      type = "Color?",
      description = "Color selected on the first layout pass, or the center of the wheel when null",
    )
    ParameterItem(
      name = "wheelImageBitmap",
      type = "ImageBitmap?",
      description = "Custom image drawn in place of the default indicator",
    )
    ParameterItem(
      name = "drawOnPosSelected",
      type = "(DrawScope.() -> Unit)?",
      description = "Draws anything you like at the selected position",
    )
    ParameterItem(
      name = "drawDefaultWheelIndicator",
      type = "Boolean",
      description = "Whether to draw the built in indicator, off by default once you " +
        "pass a wheel image or a draw block",
    )
    ParameterItem(
      name = "onColorChanged",
      type = "(ColorEnvelope) -> Unit",
      description = "Invoked for every step of a gesture",
    )
    ParameterItem(
      name = "onColorPickingFinished",
      type = "(ColorEnvelope) -> Unit",
      description = "Invoked once when a tap lands or a drag ends",
    )
    ParameterItem(
      name = "onStart",
      type = "() -> Unit",
      description = "Invoked when the user starts interacting with the picker",
    )
    ParameterItem(
      name = "onFinish",
      type = "() -> Unit",
      description = "Invoked when the user stops interacting with the picker",
    )

    Spacer(modifier = Modifier.height(32.dp))

    // Initial Color
    Text(
      text = "Setting Initial Color",
      style = DocsTheme.typography.h2,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "You can set the initial color using the initialColor parameter:",
      style = DocsTheme.typography.body,
      color = DocsTheme.colors.onSurface,
    )

    Spacer(modifier = Modifier.height(16.dp))

    CodeBlock(
      code = """HsvColorPicker(
    modifier = Modifier.size(300.dp),
    controller = controller,
    initialColor = Color.Red, // Start with red selected
    onColorChanged = { /* ... */ }
)""",
    )

    Spacer(modifier = Modifier.height(32.dp))

    Text(
      text = "Knowing when a pick is finished",
      style = DocsTheme.typography.h2,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "onColorChanged fires for every step of a drag, which is more often than you want " +
        "for saving a choice or logging one. onColorPickingFinished fires once, when the " +
        "gesture is over.",
      style = DocsTheme.typography.body,
      color = DocsTheme.colors.onSurface,
    )

    Spacer(modifier = Modifier.height(16.dp))

    CodeBlock(
      code = """HsvColorPicker(
    modifier = Modifier.fillMaxWidth().height(450.dp),
    controller = controller,
    onColorChanged = { colorEnvelope: ColorEnvelope ->
        preview(colorEnvelope.color) // every step of the drag
    },
    onColorPickingFinished = { colorEnvelope: ColorEnvelope ->
        save(colorEnvelope.color) // once, when the finger lifts
    },
)""",
    )

    Spacer(modifier = Modifier.height(32.dp))

    Callout(
      text = "Note: HsvColorPicker draws its own palette, so setPaletteImageBitmap() on the " +
        "controller and the paletteContentScale parameter are for ImageColorPicker only.",
      type = CalloutType.Warning,
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
