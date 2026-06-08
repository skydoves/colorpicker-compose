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
@file:OptIn(ExperimentalWasmDsl::class)

import com.github.skydoves.colorpicker.compose.Configuration
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
  alias(libs.plugins.kmp.android.library)
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.jetbrains.compose)
  alias(libs.plugins.compose.compiler)
}

kotlin {
  android {
    namespace = "com.github.skydoves.colorpickercomposedemo.shared"
    compileSdk = Configuration.compileSdk
    minSdk = Configuration.demoMinSdk
    androidResources {
      enable = true
    }
  }

  listOf(
    iosArm64(),
    iosSimulatorArm64(),
  ).forEach {
    it.binaries.framework {
      baseName = "shared"
      isStatic = true
    }
  }

  wasmJs {
    browser()
  }

  jvm()

  sourceSets {
    commonMain.dependencies {
      implementation(libs.compose.runtime)
      implementation(libs.compose.foundation)
      implementation(libs.compose.ui)
      implementation(libs.compose.ui.tooling.preview)
      implementation(libs.compose.material)
      implementation(libs.compose.components.resources)

      implementation(libs.androidx.compose.navigation)
      implementation(libs.filekit.dialogs.compose)

      api(project(":colorpicker-compose"))
    }
  }
}

compose.resources {
  packageOfResClass = "com.github.skydoves.colorpickercomposedemo.shared"
}

dependencies {
  // The new com.android.kotlin.multiplatform.library plugin has no build variants,
  // so debugImplementation isn't available — use the unified android runtime classpath.
  androidRuntimeClasspath(libs.compose.ui.tooling)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
  compilerOptions {
    jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
  }
}
