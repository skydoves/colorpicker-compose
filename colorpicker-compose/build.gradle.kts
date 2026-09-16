@file:OptIn(ExperimentalWasmDsl::class)

import com.github.skydoves.colorpicker.compose.Configuration
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

plugins {
  alias(libs.plugins.kmp.android.library)
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.jetbrains.compose)
  alias(libs.plugins.compose.compiler)
  alias(libs.plugins.nexus.plugin)
  alias(libs.plugins.baseline.profile)
}

apply(from = "${rootDir}/scripts/publish-module.gradle.kts")

mavenPublishing {
  val artifactId = "colorpicker-compose"
  coordinates(
    Configuration.artifactGroup,
    artifactId,
    rootProject.extra.get("libVersion").toString()
  )

  pom {
    name.set(artifactId)
    description.set("Jetpack Compose color picker for getting colors from any images by tapping on the desired color.")
  }
}

@OptIn(ExperimentalWasmDsl::class)
kotlin {
  android {
    namespace = "com.github.skydoves.colorpicker.compose"
    compileSdk = Configuration.compileSdk
    minSdk = Configuration.minSdk

    compilations.configureEach {
      compileTaskProvider.configure {
        compilerOptions {
          jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
      }
    }

    lint {
      abortOnError = false
    }
  }
  jvm("desktop")
  iosArm64()
  iosSimulatorArm64()
  macosArm64()
  // The web targets do not run commonTest. ColorPickerController builds a Paint up front, and on
  // Kotlin/JS that reaches for Skiko, whose WebAssembly binary a bare Node or headless browser test
  // never loads. The same sources are covered by desktopTest instead.
  js(IR) {
    browser { testTask { enabled = false } }
    nodejs { testTask { enabled = false } }
  }
  wasmJs {
    browser { testTask { enabled = false } }
    binaries.library()
  }

  @Suppress("OPT_IN_USAGE")
  applyHierarchyTemplate {
    common {
      group("jvm") {
        // The `com.android.kotlin.multiplatform.library` plugin registers its target as
        // platformType=androidJvm, which `withAndroidTarget()` (matches legacy KotlinAndroidTarget)
        // does not pick up. Match by platform type instead.
        withCompilations { it.target.platformType == KotlinPlatformType.androidJvm }
        withJvm()
      }
      group("skia") {
        withJvm()
        group("darwin") {
          group("apple") {
            group("ios") {
              withIosArm64()
              withIosSimulatorArm64()
            }
            group("macos") {
              withMacosArm64()
            }
          }
          withJs()
          withWasmJs()
        }
      }
    }
  }

  tasks.register("testClasses")

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(libs.compose.runtime)
        implementation(libs.compose.foundation)
        implementation(libs.compose.ui)
      }
    }

    val commonTest by getting {
      languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
      dependencies {
        implementation(libs.kotlin.test)
        implementation(libs.kotlinx.coroutines.test)
      }
    }

    // Tests that need a real composition or an ImageBitmap live here. runComposeUiTest wants a
    // window, and compose.desktop.currentOs is what supplies the Skiko backend that draws into it.
    val desktopTest by getting {
      languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
      languageSettings.optIn("androidx.compose.ui.test.ExperimentalTestApi")
      dependencies {
        implementation(libs.compose.ui.test)
        implementation(compose.desktop.currentOs)
      }
    }
  }

  explicitApi()
}

dependencies {
  baselineProfile(project(":benchmark"))
}

baselineProfile {
  baselineProfileOutputDir = "../../src/androidMain"
  filter {
    include("com.github.skydoves.colorpicker.compose.**")
  }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
  compilerOptions {
    jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
  }
}

tasks.withType<JavaCompile>().configureEach {
  this.targetCompatibility = libs.versions.jvmTarget.get()
  this.sourceCompatibility = libs.versions.jvmTarget.get()
}
