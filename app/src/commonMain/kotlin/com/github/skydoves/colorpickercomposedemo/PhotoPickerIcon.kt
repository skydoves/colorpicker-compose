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
package com.github.skydoves.colorpickercomposedemo

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import colorpickercomposedemo.app.generated.resources.Res
import colorpickercomposedemo.app.generated.resources.ic_gallery
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import com.preat.peekaboo.image.picker.toImageBitmap
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ColumnScope.PhotoPickerIcon(
  onImageSelected: (ImageBitmap?) -> Unit,
) {
  val scope = rememberCoroutineScope()

  val singleImagePicker = rememberImagePickerLauncher(
    selectionMode = SelectionMode.Single,
    scope = scope,
    onResult = { byteArrays ->
      byteArrays.firstOrNull()?.let {
        val imageBitmap = it.toImageBitmap()
        onImageSelected.invoke(imageBitmap)
      }
    },
  )

  Box(
    modifier = Modifier
      .padding(horizontal = 16.dp)
      .align(Alignment.End),
  ) {
    Image(
      modifier = Modifier
        .size(42.dp)
        .clickable { singleImagePicker.launch() },
      imageVector = vectorResource(Res.drawable.ic_gallery),
      contentDescription = null,
    )
  }
}
