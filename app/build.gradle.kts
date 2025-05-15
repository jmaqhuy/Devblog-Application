plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.devblogapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.devblogapplication"
        minSdk = 28
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

    buildFeatures {
        viewBinding = true
        dataBinding = true
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

    // Room
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    // ViewModel
    implementation(libs.lifecycle.viewmodel)
    // LiveData
    implementation(libs.lifecycle.livedata)

    // Retrofit
    implementation (libs.retrofit)
    implementation(libs.converter.gson)

    // Lombok
    compileOnly (libs.lombok)
    annotationProcessor (libs.lombok)

    // security for access token
    implementation (libs.security.crypto)

    implementation (libs.lottie)

    implementation(libs.logging.interceptor)

    implementation (libs.roundedimageview)

    // Load Image from Internet
    implementation (libs.glide)

    // Select favorite tag
    implementation(libs.flexbox)


    // markdown
    implementation (libs.core)
    implementation (libs.ext.strikethrough)
    implementation (libs.ext.tables)
    implementation (libs.image)
    implementation (libs.linkify)

}