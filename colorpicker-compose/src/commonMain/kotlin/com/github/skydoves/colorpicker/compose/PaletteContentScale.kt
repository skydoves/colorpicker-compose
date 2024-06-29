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
package com.github.skydoves.colorpicker.compose

/** Represents a rule to apply to scale a source rectangle to be inscribed into a destination. */
public enum class PaletteContentScale {
  /**
   * Scale the source with maintaining the source's aspect ratio
   * so that both dimensions (width and height) of the source will be equal to or less than the
   * corresponding dimension of the target size.
   */
  FIT,

  /**
   * Crop ths source the corresponding dimension of the target size.
   * so that if the dimensions (width and height) source is bigger than the target size,
   * it will be cut off from the center.
   */
  CROP,

  /**
   * Scale the source with maintaining the source's aspect ratio
   * so that if both dimensions (width and height) of the source is smaller than the target size,
   * it will not be scaled.
   */
//    INSIDE,
}
