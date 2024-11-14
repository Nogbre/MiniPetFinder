plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.example.minipetfinder"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.minipetfinder"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.room:room-runtime:2.6.1")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("com.google.maps.android:android-maps-utils:2.2.5")
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation(libs.recyclerview)
    implementation ("androidx.recyclerview:recyclerview:1.2.1")

    annotationProcessor ("androidx.room:room-compiler:2.6.1")
    implementation ("com.google.android.material:material:1.4.0")


    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}