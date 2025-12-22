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
fun GettingStartedScreen() {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(32.dp),
  ) {
    Text(
      text = "Getting Started",
      style = DocsTheme.typography.h1,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "Learn how to use ColorPicker Compose in your project.",
      style = DocsTheme.typography.body,
      color = DocsTheme.colors.onSurfaceVariant,
    )

    Spacer(modifier = Modifier.height(32.dp))

    // Step 1: Create Controller
    Text(
      text = "1. Create a ColorPickerController",
      style = DocsTheme.typography.h2,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "First, create a ColorPickerController using rememberColorPickerController(). " +
        "This controller manages the state and coordinates all color picker components.",
      style = DocsTheme.typography.body,
      color = DocsTheme.colors.onSurface,
    )

    Spacer(modifier = Modifier.height(16.dp))

    CodeBlock(
      code = """val controller = rememberColorPickerController()""",
    )

    Spacer(modifier = Modifier.height(32.dp))

    // Step 2: Add Color Picker
    Text(
      text = "2. Add a Color Picker",
      style = DocsTheme.typography.h2,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "Add either HsvColorPicker or ImageColorPicker to your composable. " +
        "Pass the controller and handle color changes via the onColorChanged callback.",
      style = DocsTheme.typography.body,
      color = DocsTheme.colors.onSurface,
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
        val color: Color = colorEnvelope.color
        val hexCode: String = colorEnvelope.hexCode
        val fromUser: Boolean = colorEnvelope.fromUser
    }
)""",
    )

    Spacer(modifier = Modifier.height(32.dp))

    // Step 3: Add Sliders (Optional)
    Text(
      text = "3. Add Sliders (Optional)",
      style = DocsTheme.typography.h2,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "Optionally add AlphaSlider and BrightnessSlider to allow users to adjust " +
        "transparency and brightness. These automatically sync with the controller.",
      style = DocsTheme.typography.body,
      color = DocsTheme.colors.onSurface,
    )

    Spacer(modifier = Modifier.height(16.dp))

    CodeBlock(
      code = """AlphaSlider(
    modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
        .height(35.dp),
    controller = controller,
)

BrightnessSlider(
    modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
        .height(35.dp),
    controller = controller,
)""",
    )

    Spacer(modifier = Modifier.height(32.dp))

    // Complete Example
    Text(
      text = "Complete Example",
      style = DocsTheme.typography.h2,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(16.dp))

    CodeBlock(
      code = """@Composable
fun ColorPickerExample() {
    val controller = rememberColorPickerController()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        HsvColorPicker(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(10.dp),
            controller = controller,
            onColorChanged = { envelope ->
                // Handle color change
            }
        )

        AlphaSlider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .height(35.dp),
            controller = controller,
        )

        BrightnessSlider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .height(35.dp),
            controller = controller,
        )

        AlphaTile(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(6.dp)),
            controller = controller
        )
    }
}""",
    )

    Spacer(modifier = Modifier.height(32.dp))

    Callout(
      text = "Check the Playground section to interactively explore all components and see " +
        "how they work together.",
      type = CalloutType.Tip,
    )

    Spacer(modifier = Modifier.height(32.dp))
  }
}
