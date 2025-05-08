plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.yun.seoul.moduta"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.yun.seoul.moduta"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String",  "WEATHER_URL", "${project.property("weather_url")}")
        buildConfigField("String",  "WEATHER_IMAGE_URL", "${project.property("weather_image_url")}")
        buildConfigField("String",  "KAKAO_MAP", "${project.property("kakao_map")}")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.activity)
    implementation("androidx.activity:activity-ktx:1.9.3")

    implementation(libs.material)
    implementation(libs.androidx.appcompat)

    // hilt
    implementation ("com.google.dagger:hilt-android:2.49")
    kapt("com.google.dagger:hilt-compiler:2.49")
//
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation (libs.androidx.navigation.ui.ktx)

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    // Glide
    implementation(libs.glide)
    implementation("com.github.2coffees1team:GlideToVectorYou:v2.0.0")

    //okhttp
    implementation("com.squareup.okhttp3:okhttp:4.3.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.3.1")

    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.7.1")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.7.1")
    implementation("com.github.akarnokd:rxjava3-retrofit-adapter:3.0.0")

    // 카카오맵
    implementation("com.kakao.maps.open:android:2.11.9")

    implementation(project(":domain"))
    implementation(project(":data"))
}