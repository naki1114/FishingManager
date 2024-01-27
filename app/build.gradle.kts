plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.fishingmanager"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.fishingmanager"
        minSdk = 26
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
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp:okhttp:2.7.5")

    // Scalars (Converter)
    implementation("com.squareup.retrofit2:converter-scalars:2.6.4")

    // Gson (Converter)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Gmail Sender
    implementation(files("libs/activation.jar"))
    implementation(files("libs/additionnal.jar"))
    implementation(files("libs/mail.jar"))

    // ProgressView
    implementation("com.github.skydoves:progressview:1.1.3")

    // Tensorflow
//    implementation("org.tensorflow:tensorflow-lite:2.13.0")
//    implementation("org.tensorflow:tensorflow-lite-support:0.0.0-nightly-SNAPSHOT")
//    implementation("org.tensorflow:tensorflow-lite-support:0.4.3")
//    implementation("org.tensorflow:tensorflow-lite:0.0.0-nightly-SNAPSHOT")

    // Camera2
//    implementation("androidx.camera:camera-core:1.3.1")
//    implementation("androidx.camera:camera-camera2:1.3.1")
//    implementation("androidx.camera:camera-lifecycle:1.3.1")
//    implementation("androidx.camera:camera-video:1.3.1")
//    implementation("androidx.camera:camera-view:1.3.1")
//    implementation("androidx.camera:camera-extensions:1.3.1")

    // TedPermission
    implementation("io.github.ParkSangGwon:tedpermission-normal:3.3.0")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.13.0")

    // PullRefresh
//    implementation("com.baoyz.pullrefreshlayout:library:1.2.0")

    // CircleImage
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // KakaoLogin
//    implementation("com.kakao.sdk:v2-user:2.10.0")

    // GoogleLogin
//    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // NaverLogin
//    implementation("com.navercorp.nid:oauth:5.5.0")

    // CalendarView
    implementation("com.github.prolificinteractive:material-calendarview:2.0.1")
    implementation("com.jakewharton.threetenabp:threetenabp:1.2.1")

    // Navigation
    implementation("androidx.navigation:navigation-fragment:2.3.2")
    implementation("androidx.navigation:navigation-ui:2.3.2")

    // PhotoView
    implementation("com.github.chrisbanes:PhotoView:2.3.0")

}