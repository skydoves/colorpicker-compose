import com.github.skydoves.colorpicker.compose.Configuration
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
  id(libs.plugins.android.library.get().pluginId)
  id(libs.plugins.kotlin.multiplatform.get().pluginId)
  id(libs.plugins.kotlin.serialization.get().pluginId)
  id(libs.plugins.jetbrains.compose.get().pluginId)
  id(libs.plugins.compose.compiler.get().pluginId)
  id(libs.plugins.nexus.plugin.get().pluginId)
  id(libs.plugins.baseline.profile.get().pluginId)
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

kotlin {
  androidTarget { publishLibraryVariants("release") }
  jvm("desktop")
  iosX64()
  iosArm64()
  iosSimulatorArm64()
  macosX64()
  macosArm64()
  js(IR) {
    browser()
    nodejs()
  }
  @OptIn(ExperimentalWasmDsl::class)
  wasmJs {
    binaries.library()
  }

  @Suppress("OPT_IN_USAGE")
  applyHierarchyTemplate {
    common {
      group("jvm") {
        withAndroidTarget()
        withJvm()
      }
      group("skia") {
        withJvm()
        group("darwin") {
          group("apple") {
            group("ios") {
              withIosX64()
              withIosArm64()
              withIosSimulatorArm64()
            }
            group("macos") {
              withMacosX64()
              withMacosArm64()
            }
          }
          withJs()
          withWasmJs()
        }
      }
    }
  }

  task("testClasses")

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(compose.runtime)
        implementation(compose.foundation)
        implementation(compose.ui)
      }
    }
  }

  explicitApi()
}

android {
  compileSdk = Configuration.compileSdk
  namespace = "com.github.skydoves.colorpicker.compose"
  defaultConfig {
    minSdk = Configuration.minSdk
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  lint {
    abortOnError = false
  }
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
    jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
  }
}

tasks.withType<JavaCompile>().configureEach {
  this.targetCompatibility = libs.versions.jvmTarget.get()
  this.sourceCompatibility = libs.versions.jvmTarget.get()
}
