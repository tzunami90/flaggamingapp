plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.beone.flagggaming"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.beone.flagggaming"
        minSdk = 29
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
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.interceptor)
    implementation(libs.jtds)
    implementation(libs.glide)
    implementation(libs.jsoup)
    implementation(libs.bcrypt)
    implementation(libs.maps)
    implementation(libs.mapsUtils)
    implementation(libs.admob)
    implementation(libs.hikariCP)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}