import com.github.skydoves.colorpicker.compose.Configuration

plugins {
  id(libs.plugins.android.application.get().pluginId)
  id(libs.plugins.kotlin.multiplatform.get().pluginId)
  id(libs.plugins.jetbrains.compose.get().pluginId)
  id(libs.plugins.compose.compiler.get().pluginId)
  id(libs.plugins.baseline.profile.get().pluginId)
}

kotlin {
  applyDefaultHierarchyTemplate()
  androidTarget()

  listOf(
    iosX64(),
    iosArm64(),
    iosSimulatorArm64()
  ).forEach {
    it.binaries.framework {
      baseName = "shared"
      isStatic = true
    }
  }

  task("testClasses")

  sourceSets {
    commonMain.dependencies {
      implementation(compose.runtime)
      implementation(compose.foundation)
      implementation(compose.ui)
      implementation(compose.components.uiToolingPreview)
      implementation(compose.material)
      implementation(compose.components.resources)

      implementation(libs.androidx.compose.navigation)
      implementation(libs.image.picker)

      implementation(project(":colorpicker-compose"))
    }

    androidMain.dependencies {
      implementation(libs.androidx.activity.compose)
    }
  }
}

android {
  compileSdk = Configuration.compileSdk
  namespace = "com.github.skydoves.colorpickercomposedemo"

  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
  sourceSets["main"].res.srcDirs("src/androidMain/res")

  defaultConfig {
    applicationId = "com.github.skydoves.colorpickercomposedemo"
    minSdk = Configuration.demoMinSdk
    targetSdk = Configuration.targetSdk
    versionCode = Configuration.versionCode
    versionName = Configuration.versionName
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }

  buildFeatures {
    compose = true
    buildConfig = true
  }

  lint {
    abortOnError = false
  }

  buildTypes {
    create("benchmark") {
      initWith(buildTypes.getByName("release"))
      signingConfig = signingConfigs.getByName("debug")
      matchingFallbacks += listOf("release")
      isDebuggable = false
    }
  }

  dependencies {
    baselineProfile(project(":benchmark"))
  }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
  compilerOptions {
    jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
  }
}
