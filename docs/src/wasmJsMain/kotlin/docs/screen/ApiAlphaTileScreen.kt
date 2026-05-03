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
fun ApiAlphaTileScreen() {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(32.dp),
  ) {
    Text(
      text = "AlphaTile",
      style = DocsTheme.typography.h1,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "A component that displays ARGB colors including transparency with a " +
        "checkered tile background pattern, making it easy to visualize alpha values.",
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
      code = """AlphaTile(
    modifier = Modifier
        .size(80.dp)
        .clip(RoundedCornerShape(6.dp)),
    controller = controller
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
      description = "Modifier to be applied to the tile",
    )
    ParameterItem(
      name = "controller",
      type = "ColorPickerController",
      description = "Controller that provides the current color",
    )
    ParameterItem(
      name = "tileOddColor",
      type = "Color",
      description = "Color of odd tiles in the checkered pattern (default: White)",
    )
    ParameterItem(
      name = "tileEvenColor",
      type = "Color",
      description = "Color of even tiles in the checkered pattern (default: LightGray)",
    )
    ParameterItem(
      name = "tileSize",
      type = "Dp",
      description = "Size of each tile in the checkered pattern",
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
      text = "Customize the checkered background pattern:",
      style = DocsTheme.typography.body,
      color = DocsTheme.colors.onSurface,
    )

    Spacer(modifier = Modifier.height(16.dp))

    CodeBlock(
      code = """AlphaTile(
    modifier = Modifier
        .size(80.dp)
        .clip(RoundedCornerShape(6.dp)),
    controller = controller,
    tileOddColor = Color.White,
    tileEvenColor = Color.LightGray,
    tileSize = 30.dp,
)""",
    )

    Spacer(modifier = Modifier.height(32.dp))

    // Use Case
    Text(
      text = "Common Use Cases",
      style = DocsTheme.typography.h2,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "• Display the currently selected color with alpha transparency\n" +
        "• Preview colors before applying them\n" +
        "• Show color swatches in a color palette UI",
      style = DocsTheme.typography.body,
      color = DocsTheme.colors.onSurface,
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
