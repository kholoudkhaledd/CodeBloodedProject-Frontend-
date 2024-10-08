plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation ("androidx.compose.ui:ui:1.5.0")
    implementation ("com.github.bumptech.glide:glide:4.15.1")
    implementation ("androidx.compose.ui:ui-tooling:1.5.0")

    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    implementation(libs.landscapist.glide)
    implementation (libs.coil.compose.v240)
    implementation (libs.compose)

    implementation(libs.androidx.activity)
    implementation(libs.firebase.common.ktx)
    implementation(libs.firebase.database.ktx)
    implementation(libs.play.services.cast)
    implementation(libs.firebase.messaging.ktx)
    implementation(libs.androidx.core.splashscreen)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.google.code.gson:gson:2.8.9")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.3")


    implementation ("com.squareup.okhttp3:okhttp:4.9.3")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging")


        implementation ("androidx.compose.ui:ui:1.4.3")
        implementation ("androidx.compose.material3:material3:1.0.1")
        implementation ("androidx.compose.ui:ui-tooling-preview:1.4.3")
        implementation (libs.androidx.compose.ui.ui.tooling)
        implementation (libs.androidx.foundation)



        implementation ("androidx.compose.ui:ui:1.0.0")
        implementation ("androidx.compose.material:material:1.0.0")
        implementation ("androidx.compose.ui:ui-tooling:1.0.0")
        implementation ("androidx.compose.runtime:runtime-livedata:1.0.0")
        implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0")

    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0") // Use the latest version







}