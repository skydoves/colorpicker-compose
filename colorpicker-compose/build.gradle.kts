import com.github.skydoves.colorpicker.compose.Configuration

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  id(libs.plugins.android.library.get().pluginId)
  id(libs.plugins.kotlin.multiplatform.get().pluginId)
  id(libs.plugins.kotlin.serialization.get().pluginId)
  id(libs.plugins.jetbrains.compose.get().pluginId)
  id(libs.plugins.compose.compiler.get().pluginId)
  id(libs.plugins.nexus.plugin.get().pluginId)
  // TODO: id(libs.plugins.baseline.profile.get().pluginId)
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
  applyDefaultHierarchyTemplate()

  androidTarget {
    publishLibraryVariants("release")
  }
  iosX64()
  iosArm64()
  iosSimulatorArm64()

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

// TODO:
//  dependencies {
//    baselineProfile(project(":benchmark"))
//  }
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

// TODO:
//baselineProfile {
//  filter {
//    include("com.github.skydoves.colorpicker.compose.**")
//  }
//}
