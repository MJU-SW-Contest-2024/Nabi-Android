import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
}

val properties = gradleLocalProperties(rootDir, providers)

android {
    namespace = "com.nabi.nabi"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nabi.nabi"
        minSdk = 29
        targetSdk = 34
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        versionName = "0.0.1"
        versionCode = if (project.hasProperty("versionCode")) {
            project.property("versionCode").toString().toInt()
        } else {
            1
        }

        buildConfigField(
            "String",
            "KAKAO_NATIVE_KEY",
            "\"${properties.getProperty("KAKAO_NATIVE_KEY")}\""
        )
        manifestPlaceholders["KAKAO_NATIVE_KEY"] = properties.getProperty("KAKAO_NATIVE_KEY")

        buildConfigField("String", "BASE_URL", "\"${properties.getProperty("BASE_URL")}\"")

        buildConfigField("String", "NAVER_CLIENT_ID", "\"${properties.getProperty("NAVER_CLIENT_ID")}\"")
        buildConfigField("String", "NAVER_CLIENT_SECRET", "\"${properties.getProperty("NAVER_CLIENT_SECRET")}\"")
    }

    applicationVariants.all {
        outputs.all {
            val outputImpl = this as com.android.build.gradle.internal.api.ApkVariantOutputImpl
            val newFileName = "Nabi-${name}.apk"
            outputImpl.outputFileName = newFileName
        }
    }

    signingConfigs {
        create("release") {
            keyAlias = properties["SIGNED_KEY_ALIAS"] as String?
            keyPassword = properties["SIGNED_KEY_PASSWORD"] as String?
            storeFile = properties["SIGNED_STORE_FILE"]?.let { file(it) }
            storePassword = properties["SIGNED_STORE_PASSWORD"] as String?
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
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
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.dagger.hilt.compiler)

    // Glide
    implementation(libs.glide)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // LoggingInterceptor
    implementation(libs.logging.interceptor)
    implementation(libs.okhttp)

    // ViewModel
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // Kakao Login
    implementation(libs.v2.user)

    // FCM
    implementation(libs.firebase.messaging)

    // WorkManager
    implementation(libs.androidx.work.runtime.ktx)

    // ViewPager2
    implementation(libs.androidx.viewpager2)

    // Tooltip - Balloon
    implementation("com.github.skydoves:balloon:1.4.6")

    // LoggerUtils
    implementation(libs.logger)

    // ReadMoreView
    implementation("com.github.jeonjungin:ReadMoreView:v1.0.4")

    // RoomDB
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // naver Login
    implementation(libs.oauth)
}