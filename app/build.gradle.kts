plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    id("kotlin-kapt")
//    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.onthetime"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.onthetime"
        minSdk = 24
        targetSdk = 34
        versionCode = 2
        versionName = "1.1"

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
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

}

kapt{
    correctErrorTypes = true
}

dependencies {
//    2.28.3-alpha
//    implementation ("com.google.dagger:hilt-android:2.44")
//    kapt ("com.google.dagger:hilt-android-compiler:2.44")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.firebase.storage)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)



    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    implementation("com.google.android.material:material:1.9.0")

//Splash Screen
    implementation("androidx.core:core-splashscreen:1.0.0")


    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.0")

//Firebase
    implementation("com.google.firebase:firebase-auth:21.0.1")
    implementation("com.google.firebase:firebase-firestore:25.1.0")
    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))

    implementation("com.google.firebase:firebase-analytics")


//    implementation(platform("com.google.firebase:firebase-bom:32.0.0"))


//    Dagger
//    implementation(libs.hilt.android)
//    kapt(libs.hilt.android.compiler)
//    implementation(libs.androidx.hilt.navigation.compose)
//    implementation(libs.androidx.hilt.lifecycle.viewmodel)
//    kapt(libs.androidx.hilt.compiler)
//    kapt (libs.androidx.hilt.compiler.v100)
//    implementation("com.google.dagger:hilt-android:2.44")
//    kapt("com.google.dagger:hilt-android-compiler:2.44")



    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")

    implementation ("com.squareup.picasso:picasso:2.8")



    implementation ("de.hdodenhof:circleimageview:3.1.0")


    implementation ("com.google.android.material:material:1.9.0")

    val room_version = "2.5.2" // En güncel sürümü kontrol edin

    implementation ("androidx.room:room-runtime:$room_version")
    annotationProcessor ("androidx.room:room-compiler:$room_version") // Java kullanıyorsanız
    kapt ("androidx.room:room-compiler:$room_version") // Kotlin kullanıyorsanız


}