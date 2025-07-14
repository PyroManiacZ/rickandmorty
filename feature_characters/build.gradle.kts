plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "ru.keckinnd.feature_characters"
    compileSdk = 36

    defaultConfig {
        minSdk = 25
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":core"))

    implementation(libs.androidx.compose.ui)
    implementation(libs.hilt.android)
    implementation(libs.androidx.compose.material)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.coil.compose)
    implementation(libs.accompanist.placeholder.material)
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
    implementation (libs.accompanist.systemuicontroller)
    implementation(libs.androidx.icons.material.core)
    implementation(libs.androidx.icons.material.extended)
    implementation(libs.material3)
}
