// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    dependencies {
        classpath (libs.kotlin.gradle.plugin)
        classpath (libs.hilt.android.gradle.plugin)
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("com.android.library") version "7.3.1" apply false
    kotlin("kapt") version "1.9.21"
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    id("com.google.devtools.ksp") version "2.0.0-1.0.24" apply false
}