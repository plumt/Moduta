plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {

    // 크롤링
    implementation("org.jsoup:jsoup:1.11.3")

    // hilt
    implementation(libs.hilt.core)
    kapt(libs.hilt.compiler)

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.7.1")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.7.1")
    implementation("com.github.akarnokd:rxjava3-retrofit-adapter:3.0.0")

    // Room
    val room_version = "2.7.1"
    implementation("androidx.room:room-runtime:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    implementation(project(":domain"))
}