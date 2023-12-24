plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.fishingmanager"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.fishingmanager"
        minSdk = 24
        targetSdk = 33
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
    dataBinding {
        enable = true
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

    // Basic
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Scalars (Converter)
    implementation("com.squareup.retrofit2:converter-scalars:2.6.4")

    // Gson (Converter)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Gmail Sender
    implementation(files("libs/activation.jar"))
    implementation(files("libs/additionnal.jar"))
    implementation(files("libs/mail.jar"))

    // Tensorflow
    implementation("org.tensorflow:tensorflow-lite:2.13.0")
    implementation("org.tensorflow:tensorflow-lite-support:0.0.0-nightly-SNAPSHOT")
    implementation("org.tensorflow:tensorflow-lite-support:0.4.3")
    implementation("org.tensorflow:tensorflow-lite:0.0.0-nightly-SNAPSHOT")



}