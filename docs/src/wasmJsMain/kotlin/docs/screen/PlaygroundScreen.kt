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

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import docs.component.CodeBlock
import docs.theme.DocsTheme

@Composable
fun PlaygroundScreen() {
  val controller = rememberColorPickerController()
  var hexCode by remember { mutableStateOf("FFFFFFFF") }
  var selectedColor by remember { mutableStateOf(Color.White) }
  var showAlphaSlider by remember { mutableStateOf(true) }
  var showBrightnessSlider by remember { mutableStateOf(true) }
  var wheelRadius by remember { mutableFloatStateOf(12f) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(32.dp),
  ) {
    Text(
      text = "Interactive Playground",
      style = DocsTheme.typography.h1,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "Experiment with the color picker components in real-time. " +
        "Adjust settings and see the generated code update automatically.",
      style = DocsTheme.typography.body,
      color = DocsTheme.colors.onSurfaceVariant,
    )

    Spacer(modifier = Modifier.height(32.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(24.dp),
    ) {
      // Left: Color Picker Demo
      Card(
        modifier = Modifier.weight(1f),
        colors = CardDefaults.cardColors(containerColor = DocsTheme.colors.surface),
        shape = RoundedCornerShape(16.dp),
      ) {
        Column(
          modifier = Modifier.padding(24.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          Text(
            text = "Color Picker",
            style = DocsTheme.typography.h2,
            color = DocsTheme.colors.onSurface,
          )

          Spacer(modifier = Modifier.height(24.dp))

          // HSV Color Picker
          HsvColorPicker(
            modifier = Modifier
              .size(280.dp)
              .padding(10.dp),
            controller = controller,
            onColorChanged = { colorEnvelope: ColorEnvelope ->
              hexCode = colorEnvelope.hexCode
              selectedColor = colorEnvelope.color
            },
          )

          Spacer(modifier = Modifier.height(16.dp))

          // Alpha Slider
          if (showAlphaSlider) {
            Text(
              text = "Alpha",
              style = DocsTheme.typography.bodySmall,
              color = DocsTheme.colors.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(8.dp))
            AlphaSlider(
              modifier = Modifier
                .fillMaxWidth()
                .height(35.dp)
                .clip(RoundedCornerShape(8.dp)),
              controller = controller,
            )
            Spacer(modifier = Modifier.height(16.dp))
          }

          // Brightness Slider
          if (showBrightnessSlider) {
            Text(
              text = "Brightness",
              style = DocsTheme.typography.bodySmall,
              color = DocsTheme.colors.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(8.dp))
            BrightnessSlider(
              modifier = Modifier
                .fillMaxWidth()
                .height(35.dp)
                .clip(RoundedCornerShape(8.dp)),
              controller = controller,
            )
            Spacer(modifier = Modifier.height(16.dp))
          }

          // Color Preview
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
          ) {
            AlphaTile(
              modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, DocsTheme.colors.divider, RoundedCornerShape(8.dp)),
              controller = controller,
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
              Text(
                text = "Selected Color",
                style = DocsTheme.typography.bodySmall,
                color = DocsTheme.colors.onSurfaceVariant,
              )
              Spacer(modifier = Modifier.height(4.dp))
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(4.dp))
                  .background(DocsTheme.colors.codeBackground)
                  .padding(horizontal = 12.dp, vertical = 6.dp),
              ) {
                Text(
                  text = "#$hexCode",
                  style = DocsTheme.typography.code,
                  color = DocsTheme.colors.primary,
                  fontWeight = FontWeight.Medium,
                )
              }
            }
          }
        }
      }

      // Right: Controls & Generated Code
      Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(24.dp),
      ) {
        // Live Preview with Icons
        Card(
          colors = CardDefaults.cardColors(containerColor = DocsTheme.colors.surface),
          shape = RoundedCornerShape(16.dp),
        ) {
          Column(
            modifier = Modifier.padding(24.dp),
          ) {
            Text(
              text = "Live Preview",
              style = DocsTheme.typography.h2,
              color = DocsTheme.colors.onSurface,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = "See your selected color applied to icons in real-time",
              style = DocsTheme.typography.bodySmall,
              color = DocsTheme.colors.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
              // Heart Icon
              Canvas(modifier = Modifier.size(60.dp)) {
                val heartPath = Path().apply {
                  val width = size.width
                  val height = size.height
                  moveTo(width / 2, height * 0.25f)
                  cubicTo(
                    width * 0.15f,
                    height * -0.1f,
                    -width * 0.1f,
                    height * 0.45f,
                    width / 2,
                    height * 0.9f,
                  )
                  moveTo(width / 2, height * 0.25f)
                  cubicTo(
                    width * 0.85f,
                    height * -0.1f,
                    width * 1.1f,
                    height * 0.45f,
                    width / 2,
                    height * 0.9f,
                  )
                }
                drawPath(heartPath, color = selectedColor, style = Fill)
              }

              // Star Icon
              Canvas(modifier = Modifier.size(60.dp)) {
                val starPath = Path().apply {
                  val cx = size.width / 2
                  val cy = size.height / 2
                  val outerRadius = size.minDimension / 2 * 0.9f
                  val innerRadius = outerRadius * 0.4f
                  val points = 5
                  var angle = -kotlin.math.PI.toFloat() / 2

                  moveTo(
                    cx + outerRadius * kotlin.math.cos(angle),
                    cy + outerRadius * kotlin.math.sin(angle),
                  )
                  for (i in 0 until points) {
                    angle += kotlin.math.PI.toFloat() / points
                    lineTo(
                      cx + innerRadius * kotlin.math.cos(angle),
                      cy + innerRadius * kotlin.math.sin(angle),
                    )
                    angle += kotlin.math.PI.toFloat() / points
                    lineTo(
                      cx + outerRadius * kotlin.math.cos(angle),
                      cy + outerRadius * kotlin.math.sin(angle),
                    )
                  }
                  close()
                }
                drawPath(starPath, color = selectedColor, style = Fill)
              }

              // Circle Icon
              Canvas(modifier = Modifier.size(60.dp)) {
                drawCircle(color = selectedColor, radius = size.minDimension / 2 * 0.8f)
              }

              // Diamond Icon
              Canvas(modifier = Modifier.size(60.dp)) {
                val diamondPath = Path().apply {
                  val cx = size.width / 2
                  val cy = size.height / 2
                  val radius = size.minDimension / 2 * 0.8f
                  moveTo(cx, cy - radius)
                  lineTo(cx + radius, cy)
                  lineTo(cx, cy + radius)
                  lineTo(cx - radius, cy)
                  close()
                }
                drawPath(diamondPath, color = selectedColor, style = Fill)
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Outlined versions
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
              // Outlined Heart
              Canvas(modifier = Modifier.size(60.dp)) {
                val heartPath = Path().apply {
                  val width = size.width
                  val height = size.height
                  moveTo(width / 2, height * 0.25f)
                  cubicTo(
                    width * 0.15f,
                    height * -0.1f,
                    -width * 0.1f,
                    height * 0.45f,
                    width / 2,
                    height * 0.9f,
                  )
                  moveTo(width / 2, height * 0.25f)
                  cubicTo(
                    width * 0.85f,
                    height * -0.1f,
                    width * 1.1f,
                    height * 0.45f,
                    width / 2,
                    height * 0.9f,
                  )
                }
                drawPath(heartPath, color = selectedColor, style = Stroke(width = 4f))
              }

              // Outlined Star
              Canvas(modifier = Modifier.size(60.dp)) {
                val starPath = Path().apply {
                  val cx = size.width / 2
                  val cy = size.height / 2
                  val outerRadius = size.minDimension / 2 * 0.9f
                  val innerRadius = outerRadius * 0.4f
                  val points = 5
                  var angle = -kotlin.math.PI.toFloat() / 2

                  moveTo(
                    cx + outerRadius * kotlin.math.cos(angle),
                    cy + outerRadius * kotlin.math.sin(angle),
                  )
                  for (i in 0 until points) {
                    angle += kotlin.math.PI.toFloat() / points
                    lineTo(
                      cx + innerRadius * kotlin.math.cos(angle),
                      cy + innerRadius * kotlin.math.sin(angle),
                    )
                    angle += kotlin.math.PI.toFloat() / points
                    lineTo(
                      cx + outerRadius * kotlin.math.cos(angle),
                      cy + outerRadius * kotlin.math.sin(angle),
                    )
                  }
                  close()
                }
                drawPath(starPath, color = selectedColor, style = Stroke(width = 4f))
              }

              // Outlined Circle
              Canvas(modifier = Modifier.size(60.dp)) {
                drawCircle(
                  color = selectedColor,
                  radius = size.minDimension / 2 * 0.8f,
                  style = Stroke(width = 4f),
                )
              }

              // Outlined Diamond
              Canvas(modifier = Modifier.size(60.dp)) {
                val diamondPath = Path().apply {
                  val cx = size.width / 2
                  val cy = size.height / 2
                  val radius = size.minDimension / 2 * 0.8f
                  moveTo(cx, cy - radius)
                  lineTo(cx + radius, cy)
                  lineTo(cx, cy + radius)
                  lineTo(cx - radius, cy)
                  close()
                }
                drawPath(diamondPath, color = selectedColor, style = Stroke(width = 4f))
              }
            }
          }
        }

        // Controls
        Card(
          colors = CardDefaults.cardColors(containerColor = DocsTheme.colors.surface),
          shape = RoundedCornerShape(16.dp),
        ) {
          Column(
            modifier = Modifier.padding(24.dp),
          ) {
            Text(
              text = "Settings",
              style = DocsTheme.typography.h2,
              color = DocsTheme.colors.onSurface,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Show Alpha Slider
            Row(
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Checkbox(
                checked = showAlphaSlider,
                onCheckedChange = { showAlphaSlider = it },
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Show AlphaSlider",
                style = DocsTheme.typography.body,
                color = DocsTheme.colors.onSurface,
              )
            }

            // Show Brightness Slider
            Row(
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Checkbox(
                checked = showBrightnessSlider,
                onCheckedChange = { showBrightnessSlider = it },
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Show BrightnessSlider",
                style = DocsTheme.typography.body,
                color = DocsTheme.colors.onSurface,
              )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Wheel Radius
            Text(
              text = "Wheel Radius: ${wheelRadius.toInt()}dp",
              style = DocsTheme.typography.bodySmall,
              color = DocsTheme.colors.onSurface,
            )
            Slider(
              value = wheelRadius,
              onValueChange = {
                wheelRadius = it
                controller.wheelRadius = it.dp
              },
              valueRange = 6f..30f,
              colors = SliderDefaults.colors(
                thumbColor = DocsTheme.colors.primary,
                activeTrackColor = DocsTheme.colors.primary,
              ),
            )
          }
        }

        // Generated Code
        Card(
          colors = CardDefaults.cardColors(containerColor = DocsTheme.colors.surface),
          shape = RoundedCornerShape(16.dp),
        ) {
          Column(
            modifier = Modifier.padding(24.dp),
          ) {
            Text(
              text = "Generated Code",
              style = DocsTheme.typography.h2,
              color = DocsTheme.colors.onSurface,
            )

            Spacer(modifier = Modifier.height(16.dp))

            val generatedCode = buildString {
              append("val controller = rememberColorPickerController()\n\n")
              append("HsvColorPicker(\n")
              append("    modifier = Modifier.size(280.dp),\n")
              append("    controller = controller,\n")
              append("    onColorChanged = { envelope ->\n")
              append("        // hexCode = envelope.hexCode\n")
              append("    }\n")
              append(")\n")

              if (showAlphaSlider) {
                append("\nAlphaSlider(\n")
                append("    modifier = Modifier\n")
                append("        .fillMaxWidth()\n")
                append("        .height(35.dp),\n")
                append("    controller = controller,\n")
                append(")\n")
              }

              if (showBrightnessSlider) {
                append("\nBrightnessSlider(\n")
                append("    modifier = Modifier\n")
                append("        .fillMaxWidth()\n")
                append("        .height(35.dp),\n")
                append("    controller = controller,\n")
                append(")\n")
              }

              append("\nAlphaTile(\n")
              append("    modifier = Modifier\n")
              append("        .size(60.dp)\n")
              append("        .clip(RoundedCornerShape(8.dp)),\n")
              append("    controller = controller,\n")
              append(")")
            }

            CodeBlock(code = generatedCode)
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(32.dp))
  }
}
