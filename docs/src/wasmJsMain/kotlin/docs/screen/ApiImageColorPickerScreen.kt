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
fun ApiImageColorPickerScreen() {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(32.dp),
  ) {
    Text(
      text = "ImageColorPicker",
      style = DocsTheme.typography.h1,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "Allows you to pick colors from any image by tapping on the desired color. " +
        "Works with drawable resources or dynamically loaded images.",
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
      code = """ImageColorPicker(
    modifier = Modifier
        .fillMaxWidth()
        .height(450.dp)
        .padding(10.dp),
    controller = controller,
    paletteImageBitmap = ImageBitmap.imageResource(R.drawable.palette),
    paletteContentScale = PaletteContentScale.FIT,
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
      name = "paletteImageBitmap",
      type = "ImageBitmap",
      description = "The image to use as the color palette",
    )
    ParameterItem(
      name = "paletteContentScale",
      type = "PaletteContentScale",
      description = "How to scale the palette image (FIT or CROP)",
    )
    ParameterItem(
      name = "wheelRadius",
      type = "Dp",
      description = "Radius of the selector wheel indicator",
    )
    ParameterItem(
      name = "wheelColor",
      type = "Color",
      description = "Color of the wheel indicator",
    )
    ParameterItem(
      name = "onColorChanged",
      type = "(ColorEnvelope) -> Unit",
      description = "Callback invoked when color changes",
    )

    Spacer(modifier = Modifier.height(32.dp))

    // PaletteContentScale
    Text(
      text = "PaletteContentScale",
      style = DocsTheme.typography.h2,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "Control how the palette image is scaled within the picker:",
      style = DocsTheme.typography.body,
      color = DocsTheme.colors.onSurface,
    )

    Spacer(modifier = Modifier.height(16.dp))

    CodeBlock(
      code = """// Scale to fit within bounds
controller.setPaletteContentScale(PaletteContentScale.FIT)

// Center crop the image
controller.setPaletteContentScale(PaletteContentScale.CROP)""",
    )

    Spacer(modifier = Modifier.height(32.dp))

    // Dynamic Palette
    Text(
      text = "Dynamic Palette",
      style = DocsTheme.typography.h2,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "You can change the palette image at runtime using the controller:",
      style = DocsTheme.typography.body,
      color = DocsTheme.colors.onSurface,
    )

    Spacer(modifier = Modifier.height(16.dp))

    CodeBlock(
      code = """// Load image from photo picker
val photoPicker = rememberLauncherForActivityResult(PhotoPicker()) { uris ->
    val uri = uris.firstOrNull() ?: return@rememberLauncherForActivityResult

    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        ImageDecoder.decodeBitmap(
            ImageDecoder.createSource(context.contentResolver, uri)
        )
    } else {
        MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    }

    controller.setPaletteImageBitmap(bitmap.asImageBitmap())
}""",
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
