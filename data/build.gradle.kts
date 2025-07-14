plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "ru.keckinnd.data"
    compileSdk = 36

    defaultConfig {
        minSdk = 25
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
    implementation(project(":core"))
    implementation(project(":domain"))


    implementation(libs.androidx.core.ktx)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    ksp(libs.hilt.compiler)
    implementation(libs.hilt.android)

    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.converter.kotlinx.serialization)

    implementation(libs.retrofit)

    implementation(libs.javax.inject)
}