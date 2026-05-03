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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import docs.theme.DocsTheme

@Composable
fun PlatformSupportScreen() {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(32.dp),
  ) {
    Text(
      text = "Platform Support",
      style = DocsTheme.typography.h1,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "ColorPicker Compose is built with Kotlin Multiplatform and supports " +
        "a wide range of platforms.",
      style = DocsTheme.typography.body,
      color = DocsTheme.colors.onSurfaceVariant,
    )

    Spacer(modifier = Modifier.height(32.dp))

    // Platforms Grid
    Text(
      text = "Supported Platforms",
      style = DocsTheme.typography.h2,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(24.dp))

    Column(
      verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      PlatformRow(platform = "Android", targets = "API 21+", supported = true)
      PlatformRow(platform = "Desktop (JVM)", targets = "Windows, macOS, Linux", supported = true)
      PlatformRow(platform = "iOS", targets = "x64, arm64, simulatorArm64", supported = true)
      PlatformRow(platform = "macOS", targets = "x64, arm64", supported = true)
      PlatformRow(platform = "JavaScript", targets = "Browser, Node.js", supported = true)
      PlatformRow(platform = "WebAssembly", targets = "WasmJS", supported = true)
    }

    Spacer(modifier = Modifier.height(48.dp))

    // Source Set Structure
    Text(
      text = "Source Set Structure",
      style = DocsTheme.typography.h2,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "The library uses a hierarchical source set structure for maximum code sharing:",
      style = DocsTheme.typography.body,
      color = DocsTheme.colors.onSurface,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(containerColor = DocsTheme.colors.codeBackground),
      shape = RoundedCornerShape(8.dp),
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
      ) {
        SourceSetNode(name = "commonMain", level = 0)
        SourceSetNode(name = "jvmMain", level = 1)
        SourceSetNode(name = "androidMain", level = 2)
        SourceSetNode(name = "desktopMain", level = 2)
        SourceSetNode(name = "skiaMain", level = 1)
        SourceSetNode(name = "darwinMain", level = 2)
        SourceSetNode(name = "iosMain", level = 3)
        SourceSetNode(name = "macosMain", level = 3)
        SourceSetNode(name = "jsMain", level = 2)
        SourceSetNode(name = "wasmJsMain", level = 2)
      }
    }

    Spacer(modifier = Modifier.height(48.dp))

    // Notes
    Text(
      text = "Notes",
      style = DocsTheme.typography.h2,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "• All platforms share the same API and behavior\n" +
        "• Platform-specific implementations are handled internally\n" +
        "• No additional configuration needed for multiplatform projects",
      style = DocsTheme.typography.body,
      color = DocsTheme.colors.onSurface,
    )

    Spacer(modifier = Modifier.height(32.dp))
  }
}

@Composable
private fun SourceSetNode(
  name: String,
  level: Int,
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Spacer(modifier = Modifier.width((level * 24).dp))

    Box(
      modifier = Modifier
        .size(8.dp)
        .clip(RoundedCornerShape(2.dp))
        .background(DocsTheme.colors.primary),
    )

    Spacer(modifier = Modifier.width(8.dp))

    Text(
      text = name,
      style = DocsTheme.typography.code,
      color = DocsTheme.colors.onSurface,
    )
  }
}

@Composable
private fun PlatformRow(
  platform: String,
  targets: String,
  supported: Boolean,
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = DocsTheme.colors.surface),
    shape = RoundedCornerShape(8.dp),
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      if (supported) {
        Icon(
          imageVector = Icons.Default.Check,
          contentDescription = "Supported",
          tint = DocsTheme.colors.success,
          modifier = Modifier
            .size(24.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(DocsTheme.colors.success.copy(alpha = 0.1f))
            .padding(4.dp),
        )
      }

      Spacer(modifier = Modifier.width(16.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = platform,
          style = DocsTheme.typography.body,
          color = DocsTheme.colors.onSurface,
          fontWeight = FontWeight.Medium,
        )
        Text(
          text = targets,
          style = DocsTheme.typography.caption,
          color = DocsTheme.colors.onSurfaceVariant,
        )
      }
    }
  }
}
