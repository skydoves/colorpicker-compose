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
import docs.component.CodeLanguage
import docs.config.BuildConfig
import docs.theme.DocsTheme

@Composable
fun InstallationScreen() {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(32.dp),
  ) {
    Text(
      text = "Installation",
      style = DocsTheme.typography.h1,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "Add ColorPicker Compose to your project.",
      style = DocsTheme.typography.body,
      color = DocsTheme.colors.onSurfaceVariant,
    )

    Spacer(modifier = Modifier.height(32.dp))

    // Version Catalog
    Text(
      text = "Version Catalog",
      style = DocsTheme.typography.h2,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "If you're using Version Catalog, add the dependency to your libs.versions.toml:",
      style = DocsTheme.typography.body,
      color = DocsTheme.colors.onSurface,
    )

    Spacer(modifier = Modifier.height(16.dp))

    val versionCatalogCode = buildString {
      append("[versions]\n")
      append("colorpicker = \"${BuildConfig.VERSION}\"\n\n")
      append("[libraries]\n")
      append("compose-colorpicker = { ")
      append("module = \"com.github.skydoves:colorpicker-compose\", ")
      append("version.ref = \"colorpicker\" }")
    }

    CodeBlock(code = versionCatalogCode, language = CodeLanguage.TOML)

    Spacer(modifier = Modifier.height(32.dp))

    // Gradle (Android)
    Text(
      text = "Gradle (Android)",
      style = DocsTheme.typography.h2,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "Add the dependency to your module's build.gradle.kts:",
      style = DocsTheme.typography.body,
      color = DocsTheme.colors.onSurface,
    )

    Spacer(modifier = Modifier.height(16.dp))

    val gradleCode = buildString {
      append("dependencies {\n")
      append("    implementation(\"com.github.skydoves:")
      append("colorpicker-compose:${BuildConfig.VERSION}\")\n\n")
      append("    // or if using Version Catalog\n")
      append("    implementation(libs.compose.colorpicker)\n")
      append("}")
    }

    CodeBlock(code = gradleCode)

    Spacer(modifier = Modifier.height(32.dp))

    // Kotlin Multiplatform
    Text(
      text = "Kotlin Multiplatform",
      style = DocsTheme.typography.h2,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "For Kotlin Multiplatform projects, add the dependency to your commonMain source set:",
      style = DocsTheme.typography.body,
      color = DocsTheme.colors.onSurface,
    )

    Spacer(modifier = Modifier.height(16.dp))

    val kmpCode = buildString {
      append("kotlin {\n")
      append("    sourceSets {\n")
      append("        commonMain.dependencies {\n")
      append("            implementation(\"com.github.skydoves:")
      append("colorpicker-compose:${BuildConfig.VERSION}\")\n")
      append("        }\n")
      append("    }\n")
      append("}")
    }

    CodeBlock(code = kmpCode)

    Spacer(modifier = Modifier.height(32.dp))

    // Requirements
    Text(
      text = "Requirements",
      style = DocsTheme.typography.h2,
      color = DocsTheme.colors.onBackground,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "• Android: minSdk 21+\n" +
        "• Kotlin: 2.0+\n" +
        "• Compose Multiplatform: 1.6+",
      style = DocsTheme.typography.body,
      color = DocsTheme.colors.onSurface,
    )

    Spacer(modifier = Modifier.height(32.dp))
  }
}
