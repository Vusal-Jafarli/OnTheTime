buildscript {
//    ext{
//         compose_version = "1.1.0"
//    }

//
//    dependencies {
////        id("com.google.dagger.hilt.android") version "2.44" apply false
//        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.44") // Hilt Gradle Plugin" // Kotlin Gradle Plugin
//
//
//    }


}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.google.gms.google.services) apply false
//    id("com.google.dagger.hilt.android") version "2.44" apply false
//    id("kotlin-kapt")
//    id("dagger.hilt.android.plugin")

//    id("com.google.firebase.crashlytics") version "2.9.1"
//    id("com.google.firebase.analytics") version "21.1.0"
}
