import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.jetbrains.compose)
  alias(libs.plugins.compose.compiler)
}

kotlin {
  jvm()

  sourceSets {
    jvmMain.dependencies {
      implementation(compose.desktop.currentOs)
      implementation(libs.filekit.dialogs.compose)

      implementation(project(":demo:shared"))
    }
  }
}

compose.desktop {
  application {
    mainClass = "MainKt"
    nativeDistributions {
      targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
      packageName = "ColorPickerComposeDemo"
      packageVersion = "1.0.0"
    }
  }
}

tasks.withType<KotlinJvmCompile>().configureEach {
  compilerOptions {
    jvmTarget.set(JvmTarget.JVM_17)
  }
}
