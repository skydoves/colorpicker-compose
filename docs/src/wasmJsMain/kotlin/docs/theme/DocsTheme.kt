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
package docs.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Immutable
data class DocsColors(
  val primary: Color,
  val onPrimary: Color,
  val background: Color,
  val onBackground: Color,
  val surface: Color,
  val onSurface: Color,
  val surfaceVariant: Color,
  val onSurfaceVariant: Color,
  val sidebarBackground: Color,
  val sidebarActive: Color,
  val divider: Color,
  val codeBackground: Color,
  val success: Color,
  val warning: Color,
  val error: Color,
)

@Immutable
data class DocsTypography(
  val h1: TextStyle,
  val h2: TextStyle,
  val h3: TextStyle,
  val body: TextStyle,
  val bodySmall: TextStyle,
  val caption: TextStyle,
  val code: TextStyle,
)

private fun darkColors(primary: Color) = DocsColors(
  primary = primary,
  onPrimary = Color.White,
  background = Color(0xFF121212),
  onBackground = Color(0xFFE1E1E1),
  surface = Color(0xFF1E1E1E),
  onSurface = Color(0xFFE1E1E1),
  surfaceVariant = Color(0xFF2D2D2D),
  onSurfaceVariant = Color(0xFFB0B0B0),
  sidebarBackground = Color(0xFF181818),
  sidebarActive = primary.copy(alpha = 0.15f),
  divider = Color(0xFF333333),
  codeBackground = Color(0xFF1A1A2E),
  success = Color(0xFF4CAF50),
  warning = Color(0xFFFFC107),
  error = Color(0xFFEF5350),
)

private fun lightColors(primary: Color) = DocsColors(
  primary = primary,
  onPrimary = Color.White,
  background = Color(0xFFFAFAFA),
  onBackground = Color(0xFF1A1A1A),
  surface = Color.White,
  onSurface = Color(0xFF1A1A1A),
  surfaceVariant = Color(0xFFF0F0F0),
  onSurfaceVariant = Color(0xFF666666),
  sidebarBackground = Color(0xFFF5F5F5),
  sidebarActive = primary.copy(alpha = 0.15f),
  divider = Color(0xFFE0E0E0),
  codeBackground = Color(0xFFF5F5F5),
  success = Color(0xFF4CAF50),
  warning = Color(0xFFFF9800),
  error = Color(0xFFF44336),
)

val DefaultPrimaryColor = Color(0xFF7C4DFF)

private val Typography = DocsTypography(
  h1 = TextStyle(
    fontSize = 32.sp,
    fontWeight = FontWeight.Bold,
    lineHeight = 40.sp,
  ),
  h2 = TextStyle(
    fontSize = 24.sp,
    fontWeight = FontWeight.SemiBold,
    lineHeight = 32.sp,
  ),
  h3 = TextStyle(
    fontSize = 18.sp,
    fontWeight = FontWeight.Medium,
    lineHeight = 24.sp,
  ),
  body = TextStyle(
    fontSize = 16.sp,
    fontWeight = FontWeight.Normal,
    lineHeight = 24.sp,
  ),
  bodySmall = TextStyle(
    fontSize = 14.sp,
    fontWeight = FontWeight.Normal,
    lineHeight = 20.sp,
  ),
  caption = TextStyle(
    fontSize = 12.sp,
    fontWeight = FontWeight.Normal,
    lineHeight = 16.sp,
  ),
  code = TextStyle(
    fontSize = 14.sp,
    fontWeight = FontWeight.Normal,
    lineHeight = 20.sp,
  ),
)

val LocalDocsColors = staticCompositionLocalOf { darkColors(DefaultPrimaryColor) }
val LocalDocsTypography = staticCompositionLocalOf { Typography }

object DocsTheme {
  val colors: DocsColors
    @Composable get() = LocalDocsColors.current

  val typography: DocsTypography
    @Composable get() = LocalDocsTypography.current
}

@Composable
fun DocsTheme(
  darkTheme: Boolean = true,
  primaryColor: Color = DefaultPrimaryColor,
  content: @Composable () -> Unit,
) {
  val colors = if (darkTheme) darkColors(primaryColor) else lightColors(primaryColor)

  CompositionLocalProvider(
    LocalDocsColors provides colors,
    LocalDocsTypography provides Typography,
  ) {
    content()
  }
}
