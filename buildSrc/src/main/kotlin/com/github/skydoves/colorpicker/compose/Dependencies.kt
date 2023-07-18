package com.github.skydoves.colorpicker.compose

object Versions {
    internal const val ANDROID_GRADLE_PLUGIN = "7.4.2"
    internal const val ANDROID_GRADLE_SPOTLESS = "6.19.0"
    internal const val GRADLE_NEXUS_PUBLISH_PLUGIN = "1.3.0"
    internal const val KOTLIN = "1.8.22"
    internal const val KOTLIN_GRADLE_DOKKA = "1.8.20"
    internal const val KOTLIN_BINARY_VALIDATOR = "0.13.1"
    internal const val KOTLIN_COROUTINE = "1.7.2"

    internal const val MATERIAL = "1.6.0"
    internal const val ANDROIDX_CORE_KTX = "1.8.0"

    internal const val COMPOSE_BOM = "2023.06.01"
    internal const val COMPOSE_COMPILER = "1.4.8"
    internal const val COMPOSE_ACTIVITY = "1.7.2"
    internal const val COMPOSE_NAVIGATION = "2.6.0"
    internal const val PHOTO_PICKER = "1.0.0-alpha06"
}

object Dependencies {
    const val androidGradlePlugin =
        "com.android.tools.build:gradle:${Versions.ANDROID_GRADLE_PLUGIN}"
    const val gradleNexusPublishPlugin =
        "io.github.gradle-nexus:publish-plugin:${Versions.GRADLE_NEXUS_PUBLISH_PLUGIN}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"
    const val spotlessGradlePlugin =
        "com.diffplug.spotless:spotless-plugin-gradle:${Versions.ANDROID_GRADLE_SPOTLESS}"
    const val dokka = "org.jetbrains.dokka:dokka-gradle-plugin:${Versions.KOTLIN_GRADLE_DOKKA}"
    const val kotlinBinaryValidator =
        "org.jetbrains.kotlinx:binary-compatibility-validator:${Versions.KOTLIN_BINARY_VALIDATOR}"
    const val coroutines =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.KOTLIN_COROUTINE}"

    const val material = "com.google.android.material:material:${Versions.MATERIAL}"
    const val androidxCoreKtx = "androidx.core:core-ktx:${Versions.ANDROIDX_CORE_KTX}"

    const val composeBom = "androidx.compose:compose-bom:${Versions.COMPOSE_BOM}"
    const val composeUI = "androidx.compose.ui:ui"
    const val composeRuntime = "androidx.compose.runtime:runtime"
    const val composeMaterial = "androidx.compose.material:material"
    const val composeFoundation = "androidx.compose.foundation:foundation"
    const val composeTooling = "androidx.compose.ui:ui-tooling"
    const val composeActivity = "androidx.activity:activity-compose:${Versions.COMPOSE_ACTIVITY}"
    const val composeNavigation = "androidx.navigation:navigation-compose:${Versions.COMPOSE_NAVIGATION}"
    const val photoPicker =
        "com.google.modernstorage:modernstorage-photopicker:${Versions.PHOTO_PICKER}"
}
