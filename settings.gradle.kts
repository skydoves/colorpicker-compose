pluginManagement {
  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
  }
}
dependencyResolutionManagement {
  // This causes WASM builds to fail with `Could not determine the dependencies of task ':kotlinNodeJsSetup'.`
  // repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
  }
}
rootProject.name = "ColorPickerComposeDemo"
include(":app")
include(":colorpicker-compose")
include(":benchmark")
include("wasmApp")
