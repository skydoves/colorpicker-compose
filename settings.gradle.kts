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
include(":demo:androidApp")
include(":demo:shared")
include(":demo:wasmApp")
include(":demo:desktopApp")
include(":colorpicker-compose")
include(":benchmark")
include("docs")
