plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.tinyhometasksapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tinyhometasksapp"
        minSdk = 26
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.runtime.livedata)

    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.converter.moshi)
    implementation(libs.logging.interceptor)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation("io.github.vanpra.compose-material-dialogs:datetime:0.8.1-rc")

}


//kotlin {
//    sourceSets {
//        commonMain.dependencies {
//            implementation("com.squareup.retrofit2:retrofit:2.9.0")
//            implementation("com.squareup.retrofit2:converter-gson:2.9.0")
//            implementation("com.squareup.moshi:moshi:1.14.0")
//            implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
//            implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
//            implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
//            implementation("androidx.compose.runtime:runtime-livedata:1.7.3")
//            implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
//            implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
//            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
//            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
//            implementation("io.github.cdimascio:dotenv-kotlin:6.4.2")
//        }
//    }
//}