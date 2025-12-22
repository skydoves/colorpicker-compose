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
package docs.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.window.Dialog
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import docs.theme.DefaultPrimaryColor
import docs.theme.DocsTheme

private val PresetColors = listOf(
  Color(0xFF7C4DFF), // Purple (default)
  Color(0xFF2196F3), // Blue
  Color(0xFF00BCD4), // Cyan
  Color(0xFF4CAF50), // Green
  Color(0xFFFF9800), // Orange
  Color(0xFFE91E63), // Pink
  Color(0xFFF44336), // Red
  Color(0xFF9C27B0), // Deep Purple
)

@Composable
fun ThemeColorPickerDialog(
  currentColor: Color,
  onColorChanged: (Color) -> Unit,
  onDismiss: (Color) -> Unit,
) {
  val controller = rememberColorPickerController()
  val initialColor = remember { currentColor }
  var selectedColor by remember { mutableStateOf(currentColor) }

  Dialog(onDismissRequest = { onDismiss(initialColor) }) {
    Card(
      modifier = Modifier.width(340.dp),
      colors = CardDefaults.cardColors(containerColor = DocsTheme.colors.surface),
      shape = RoundedCornerShape(16.dp),
    ) {
      Column(
        modifier = Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Text(
          text = "Theme Color",
          style = DocsTheme.typography.h2,
          color = DocsTheme.colors.onSurface,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "Pick a color for the documentation theme",
          style = DocsTheme.typography.bodySmall,
          color = DocsTheme.colors.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Preset Colors
        Text(
          text = "Presets",
          style = DocsTheme.typography.bodySmall,
          color = DocsTheme.colors.onSurfaceVariant,
          modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
          PresetColors.forEach { color ->
            Box(
              modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(color)
                .then(
                  if (selectedColor == color) {
                    Modifier.border(2.dp, DocsTheme.colors.onSurface, CircleShape)
                  } else {
                    Modifier
                  },
                )
                .clickable {
                  selectedColor = color
                  onColorChanged(color)
                  controller.selectByColor(color, false)
                },
            )
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Custom Color Picker
        Text(
          text = "Custom",
          style = DocsTheme.typography.bodySmall,
          color = DocsTheme.colors.onSurfaceVariant,
          modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(8.dp))

        HsvColorPicker(
          modifier = Modifier.size(200.dp),
          controller = controller,
          onColorChanged = { envelope ->
            selectedColor = envelope.color
            onColorChanged(envelope.color)
          },
        )

        Spacer(modifier = Modifier.height(12.dp))

        BrightnessSlider(
          modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .clip(RoundedCornerShape(8.dp)),
          controller = controller,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Preview
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(
            text = "Preview:",
            style = DocsTheme.typography.bodySmall,
            color = DocsTheme.colors.onSurfaceVariant,
          )
          Spacer(modifier = Modifier.width(12.dp))
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(selectedColor)
              .border(1.dp, DocsTheme.colors.divider, RoundedCornerShape(8.dp)),
          )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Buttons
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
          TextButton(
            onClick = {
              selectedColor = DefaultPrimaryColor
              onColorChanged(DefaultPrimaryColor)
              controller.selectByColor(DefaultPrimaryColor, false)
            },
            modifier = Modifier.weight(1f),
          ) {
            Text(
              text = "Reset",
              color = DocsTheme.colors.onSurfaceVariant,
            )
          }
          TextButton(
            onClick = { onDismiss(initialColor) },
            modifier = Modifier.weight(1f),
          ) {
            Text(
              text = "Cancel",
              color = DocsTheme.colors.onSurfaceVariant,
            )
          }
          Button(
            onClick = { onDismiss(selectedColor) },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
              containerColor = selectedColor,
            ),
          ) {
            Text(
              text = "Apply",
              color = Color.White,
            )
          }
        }
      }
    }
  }
}
