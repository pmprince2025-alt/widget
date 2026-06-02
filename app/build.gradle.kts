plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.widgetkit.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.widgetkit.app"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":feature:gallery"))
    implementation(project(":feature:config"))
    implementation(project(":feature:mywidgets"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":core:ui"))
    implementation(project(":widget:clock"))
    implementation(project(":widget:note"))
    implementation(project(":widget:countdown"))

    implementation(libs.activity.compose)
    implementation(libs.navigation.compose)
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.lifecycle.runtime.compose)
}
