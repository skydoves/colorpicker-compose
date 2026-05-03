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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import docs.config.BuildConfig
import docs.navigation.DocsRoute
import docs.theme.DocsTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(onNavigate: (DocsRoute) -> Unit) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(48.dp),
  ) {
    // Hero Section
    Column(
      modifier = Modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Icon(
        imageVector = Icons.Default.Palette,
        contentDescription = null,
        tint = DocsTheme.colors.primary,
        modifier = Modifier.size(80.dp),
      )

      Spacer(modifier = Modifier.height(24.dp))

      Text(
        text = "ColorPicker Compose",
        style = DocsTheme.typography.h1,
        color = DocsTheme.colors.onBackground,
      )

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "Kotlin Multiplatform color picker library for Jetpack Compose",
        style = DocsTheme.typography.body,
        color = DocsTheme.colors.onSurfaceVariant,
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "v${BuildConfig.VERSION}",
        style = DocsTheme.typography.caption,
        color = DocsTheme.colors.primary,
        fontWeight = FontWeight.Medium,
      )
    }

    Spacer(modifier = Modifier.height(48.dp))

    // Quick Start Button
    Box(
      modifier = Modifier
        .align(Alignment.CenterHorizontally)
        .clip(RoundedCornerShape(12.dp))
        .background(DocsTheme.colors.primary)
        .clickable { onNavigate(DocsRoute.GettingStarted) }
        .padding(horizontal = 32.dp, vertical = 16.dp),
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = "Get Started",
          style = DocsTheme.typography.body,
          color = DocsTheme.colors.onPrimary,
          fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowForward,
          contentDescription = null,
          tint = DocsTheme.colors.onPrimary,
        )
      }
    }

    Spacer(modifier = Modifier.height(64.dp))

    // Features Section
    Text(
      text = "Features",
      style = DocsTheme.typography.h2,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(24.dp))

    FlowRow(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      FeatureCard(
        icon = Icons.Default.ColorLens,
        title = "HSV Color Picker",
        description = "Pick colors from HSV color wheel with intuitive touch interaction",
        modifier = Modifier.weight(1f),
      )
      FeatureCard(
        icon = Icons.Default.Brush,
        title = "Image Color Picker",
        description = "Extract colors from any image by tapping on the desired color",
        modifier = Modifier.weight(1f),
      )
      FeatureCard(
        icon = Icons.Default.Tune,
        title = "Alpha & Brightness",
        description = "Fine-tune colors with alpha and brightness sliders",
        modifier = Modifier.weight(1f),
      )
      FeatureCard(
        icon = Icons.Default.Devices,
        title = "Multiplatform",
        description = "Works on Android, iOS, Desktop, Web (JS/Wasm), and macOS",
        modifier = Modifier.weight(1f),
      )
    }

    Spacer(modifier = Modifier.height(64.dp))

    // Components Section
    Text(
      text = "Components",
      style = DocsTheme.typography.h2,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(24.dp))

    Column(
      verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      ComponentItem(
        name = "HsvColorPicker",
        description = "HSV color wheel for color selection",
        onClick = { onNavigate(DocsRoute.ApiHsvColorPicker) },
      )
      ComponentItem(
        name = "ImageColorPicker",
        description = "Pick colors from any image",
        onClick = { onNavigate(DocsRoute.ApiImageColorPicker) },
      )
      ComponentItem(
        name = "AlphaSlider",
        description = "Adjust alpha/transparency value",
        onClick = { onNavigate(DocsRoute.ApiAlphaSlider) },
      )
      ComponentItem(
        name = "BrightnessSlider",
        description = "Adjust brightness value",
        onClick = { onNavigate(DocsRoute.ApiBrightnessSlider) },
      )
      ComponentItem(
        name = "AlphaTile",
        description = "Display colors with transparency",
        onClick = { onNavigate(DocsRoute.ApiAlphaTile) },
      )
      ComponentItem(
        name = "ColorPickerController",
        description = "Central controller for all components",
        onClick = { onNavigate(DocsRoute.ApiController) },
      )
    }
  }
}

@Composable
private fun FeatureCard(
  icon: ImageVector,
  title: String,
  description: String,
  modifier: Modifier = Modifier,
) {
  Card(
    modifier = modifier,
    colors = CardDefaults.cardColors(containerColor = DocsTheme.colors.surface),
    shape = RoundedCornerShape(12.dp),
  ) {
    Column(
      modifier = Modifier.padding(20.dp),
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = DocsTheme.colors.primary,
        modifier = Modifier.size(32.dp),
      )
      Spacer(modifier = Modifier.height(12.dp))
      Text(
        text = title,
        style = DocsTheme.typography.h3,
        color = DocsTheme.colors.onSurface,
      )
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = description,
        style = DocsTheme.typography.bodySmall,
        color = DocsTheme.colors.onSurfaceVariant,
      )
    }
  }
}

@Composable
private fun ComponentItem(
  name: String,
  description: String,
  onClick: () -> Unit,
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick),
    colors = CardDefaults.cardColors(containerColor = DocsTheme.colors.surface),
    shape = RoundedCornerShape(8.dp),
  ) {
    Row(
      modifier = Modifier.padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = name,
          style = DocsTheme.typography.body,
          color = DocsTheme.colors.primary,
          fontWeight = FontWeight.Medium,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = description,
          style = DocsTheme.typography.bodySmall,
          color = DocsTheme.colors.onSurfaceVariant,
        )
      }
      Icon(
        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
        contentDescription = null,
        tint = DocsTheme.colors.onSurfaceVariant,
      )
    }
  }
}
