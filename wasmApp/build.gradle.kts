import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
  id(libs.plugins.kotlin.multiplatform.get().pluginId)
  id(libs.plugins.jetbrains.compose.get().pluginId)
  id(libs.plugins.compose.compiler.get().pluginId)
}

kotlin {
  wasmJs {
    browser {
      commonWebpackConfig {
        outputFileName = "composeApp.js"
        devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
          static = (static ?: mutableListOf()).apply {
            // Serve sources to debug inside browser
            add(project.projectDir.path)
          }
        }
      }
    }
    binaries.executable()
  }

  sourceSets {
    commonMain.dependencies {
      implementation(compose.runtime)
      implementation(compose.foundation)
      implementation(compose.ui)
      implementation(compose.components.uiToolingPreview)
      implementation(compose.material)
      implementation(compose.components.resources)

      implementation(project(":colorpicker-compose"))
    }
  }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
  compilerOptions {
    jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
  }
}
