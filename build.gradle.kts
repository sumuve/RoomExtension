// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.7.3" apply false
    id("com.android.library") version "8.7.3" apply false
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false
    kotlin("jvm") version "2.0.0" apply false
    id("com.google.devtools.ksp") version "2.0.0-1.0.23" apply false
//    id("androidx.room") version "2.6.1" apply false
}

ext {
    extra["version"] = "1.0.0"
    extra["localVersion"] = "1.1.1"
}