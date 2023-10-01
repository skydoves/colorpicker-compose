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
package com.github.skydoves.colorpicker.compose.benchmark

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test

@RequiresApi(Build.VERSION_CODES.P)
class BaselineProfileGenerator {
  @get:Rule
  val baselineProfileRule = BaselineProfileRule()

  @Test
  fun startup() =
    baselineProfileRule.collect(
      packageName = packageName,
      stableIterations = 2,
      maxIterations = 8,
    ) {
      pressHome()
      // This block defines the app's critical user journey. Here we are interested in
      // optimizing for app startup. But you can also navigate and scroll
      // through your most important UI.
      startActivityAndWait()
      device.waitForIdle()

      device.testSnitcherScenario() || return@collect
    }
}

private fun UiDevice.testSnitcherScenario(): Boolean {
  waitForObject(By.res("Image")).click()

  waitForIdle()

  return wait(Until.hasObject(By.res("HSV")), 5_000)
}

private fun UiDevice.waitForObject(selector: BySelector, timeout: Long = 5_000): UiObject2 {
  if (wait(Until.hasObject(selector), timeout)) {
    return findObject(selector)
  }

  error("Object with selector [$selector] not found")
}

private const val packageName = "com.github.skydoves.colorpickercomposedemo"
