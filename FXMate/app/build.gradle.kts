plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "org.me.gcu.fxmate"
    compileSdk = 36

    defaultConfig {
        applicationId = "org.me.gcu.fxmate"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            // Enable code and resource shrinking for release builds
            // Removes unused code and resources (e.g., unused flag images) from APK
            // Reduces APK size by ~0.37 MB (only includes ~75 used flags from 260 total)
            isMinifyEnabled = true
            isShrinkResources = true
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}