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
  js(IR) {
    browser()
    nodejs()
  }
  wasmJs {
    browser()
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

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
  compilerOptions {
    freeCompilerArgs.add("-Xexplicit-api=strict")
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
