import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
}

android {
    signingConfigs {
        getByName("debug") {
            storeFile = file("./ksp.jks")
            storePassword = "123456789"
            keyAlias = "ksp"
            keyPassword = "123456789"
        }
        create("release") {
            storeFile = file("./ksp.jks")
            storePassword = "123456789"
            keyAlias = "ksp"
            keyPassword = "123456789"
        }
    }
    namespace = "com.cjj.roomextension"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.cjj.roomextension"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        signingConfig = signingConfigs.getByName("release")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

        }
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
}

//room {
//    schemaDirectory("$projectDir/schemas")
//}

dependencies {

    "ksp"(project(":re-compiler"))
    implementation(project(":re"))
//    implementation("io.github.sumuve:room-extension:${rootProject.extra["version"].toString()}")
//    ksp("io.github.sumuve:room-extension-compiler:${rootProject.extra["version"].toString()}")
    val room_version = "2.6.1"

    implementation("androidx.room:room-runtime:$room_version")
    // To use Kotlin Symbol Processing (KSP)
    ksp("androidx.room:room-compiler:$room_version")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")
    implementation("androidx.lifecycle:lifecycle-runtime:2.8.2")
    implementation("com.jakewharton:butterknife:10.2.3")
    ksp("com.jakewharton:butterknife-compiler:10.2.3")

    implementation("com.google.code.gson:gson:2.10.1")

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}