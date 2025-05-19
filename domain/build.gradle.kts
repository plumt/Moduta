plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
//    id("com.google.devtools.ksp")
    id("kotlin-kapt")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(libs.hilt.core)
    kapt(libs.hilt.compiler)

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
}