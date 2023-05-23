@file:Suppress("UnstableApiUsage")

import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val versionMajor = 0 // 0..Infinity
val versionMinor = 1 // 0..9
val versionPatch = 2 // 0..9

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

android {
    namespace = "com.neo.blind_user_interface"
    compileSdk = 33
    buildToolsVersion = "33.0.2"

    signingConfigs {
        create("release") {
            loadProperties("keystore.properties") { properties ->
                storeFile = rootProject.file(properties["storeFile"] as String)
                storePassword = properties["storePassword"] as String
                keyAlias = properties["keyAlias"] as String
                keyPassword = properties["keyPassword"] as String
            }
        }
    }

    defaultConfig {
        applicationId = "com.neo.blind_user_interface"

        minSdk = 22
        targetSdk = 33

        versionCode = getVersionCode()
        versionName = getVersionName()
        resourceConfigurations.addAll(
            listOf(
                "en",
                "pl",
                "pt"
            )
        )

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            signingConfig = signingConfigs.getByName("release")
        }

        getByName("debug") {
            isMinifyEnabled = false

            versionNameSuffix = "-dev"
            applicationIdSuffix = ".dev"

            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.10.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Unit test
    testImplementation("junit:junit:4.13.2")

    // Instrumented test
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

// tasks
tasks.register("versionCode") {
    doLast {
        println("versionCode is ${getVersionCode()} from v${getVersionName()}")
    }
}

// functions
fun loadProperties(
    fileName: String,
    postResult: (Properties) -> Unit
) = postResult(
    Properties().apply {
        load(FileInputStream(rootProject.file(fileName)))
    }
)

fun getVersionCode() = versionMajor * 100 + versionMinor * 10 + versionPatch
fun getVersionName() = "$versionMajor.$versionMinor.$versionPatch"
