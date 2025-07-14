

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "ru.keckinnd.rickandmorty"
    compileSdk = 36

    defaultConfig {
        applicationId = "ru.keckinnd.rickandmorty"
        minSdk = 25
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
        }
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":feature_characters"))
    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(libs.hilt.android)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.ui.tooling.preview.android)
    implementation(libs.androidx.compose.navigation)
    ksp(libs.hilt.compiler)
    implementation(libs.navigation.compose)
    implementation(libs.accompanist.placeholder.material)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.coil.compose)

    implementation (libs.androidx.core.ktx)
    implementation (libs.androidx.lifecycle.runtime.ktx)
    implementation (libs.androidx.activity.compose)

    implementation (platform(libs.androidx.compose.bom))
    implementation (libs.androidx.ui)
    implementation (libs.androidx.ui.graphics)
    implementation (libs.androidx.ui.tooling.preview)
    implementation (libs.androidx.material3)
    implementation (libs.androidx.foundation)
    implementation (libs.androidx.navigation.compose.v274)

    implementation (libs.androidx.paging.compose)
    implementation (libs.androidx.paging.runtime.ktx)

    implementation (libs.retrofit)
    implementation (libs.okhttp)
    implementation (libs.converter.kotlinx.serialization)
    implementation (libs.kotlinx.serialization.json)

    implementation (libs.androidx.room.runtime.v260)
    implementation (libs.androidx.room.ktx.v260)
    implementation (libs.androidx.room.paging)

    implementation (libs.androidx.hilt.navigation.compose.v110)

    implementation (libs.coil.compose.v250)

    implementation (libs.accompanist.swiperefresh)
    implementation (libs.accompanist.systemuicontroller)
}