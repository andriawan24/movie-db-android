import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.kover)
}

val localProperties = File(rootProject.projectDir, "local.properties")
var movieApiKey = ""
if (localProperties.exists()) {
    val properties = Properties()
    localProperties.inputStream().use { properties.load(it) }

    movieApiKey = properties.getProperty("MOVIE_API_KEY") ?: ""
}

android {
    namespace = "com.andriawan.moviedb"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.andriawan.moviedb"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isDebuggable = true
            isShrinkResources = false

            buildConfigField("String", "MOVIE_API_KEY", "\"$movieApiKey\"")
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false

            buildConfigField("String", "MOVIE_API_KEY", "\"$movieApiKey\"")

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
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    // Core libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.logging.interceptor)

    // Dagger hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // Logging
    implementation(libs.timber)

    // Glide image loading
    implementation(libs.glide)

    // Kotlinx Serialization
    implementation(libs.kotlinx.serialization.json)

    // Viewmodel coroutines
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Paging
    implementation(libs.androidx.paging.runtime.ktx)

    // Additional testing dependencies
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.androidx.paging.testing)
    testImplementation(libs.mockwebserver)
    testImplementation(libs.turbine)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

kover {
    reports {
        filters {
            includes {
                classes(
                    "com.andriawan.moviedb.data.network.*",
                    "com.andriawan.moviedb.data.repository.*",
                    "com.andriawan.moviedb.domain.usecases.*",
                    "com.andriawan.moviedb.ui.*",
                )
            }
        }
    }
}