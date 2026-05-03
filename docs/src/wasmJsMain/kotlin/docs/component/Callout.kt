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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import docs.theme.DocsTheme

enum class CalloutType {
  Info,
  Warning,
  Tip,
}

@Composable
fun Callout(
  text: String,
  type: CalloutType = CalloutType.Info,
  modifier: Modifier = Modifier,
) {
  val backgroundColor = when (type) {
    CalloutType.Info -> DocsTheme.colors.primary.copy(alpha = 0.1f)
    CalloutType.Warning -> DocsTheme.colors.warning.copy(alpha = 0.1f)
    CalloutType.Tip -> DocsTheme.colors.success.copy(alpha = 0.1f)
  }

  val iconColor = when (type) {
    CalloutType.Info -> DocsTheme.colors.primary
    CalloutType.Warning -> DocsTheme.colors.warning
    CalloutType.Tip -> DocsTheme.colors.success
  }

  val borderColor = when (type) {
    CalloutType.Info -> DocsTheme.colors.primary
    CalloutType.Warning -> DocsTheme.colors.warning
    CalloutType.Tip -> DocsTheme.colors.success
  }

  Row(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(8.dp))
      .background(backgroundColor)
      .background(
        color = Color.Transparent,
        shape = RoundedCornerShape(8.dp),
      )
      .padding(16.dp),
    verticalAlignment = Alignment.Top,
  ) {
    Icon(
      imageVector = when (type) {
        CalloutType.Info -> Icons.Default.Info
        CalloutType.Warning -> Icons.Default.Warning
        CalloutType.Tip -> Icons.Default.Info
      },
      contentDescription = null,
      tint = iconColor,
    )
    Spacer(modifier = Modifier.width(12.dp))
    Text(
      text = text,
      style = DocsTheme.typography.body,
      color = DocsTheme.colors.onSurface,
    )
  }
}
