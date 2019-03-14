plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("io.fabric")
    id("com.google.gms.google-services")
}

android {
    signingConfigs {
        getByName("debug") {
            keyAlias = "androiddebugkey"
            keyPassword = "android"
            storeFile = file("$projectDir/debug.keystore")
            storePassword = "android"
        }
    }

    compileSdkVersion(Deps.compileSdkVersion)

    defaultConfig {
        applicationId = "com.stazis.subwaystations"
        minSdkVersion(Deps.minSdkVersion)
        targetSdkVersion(Deps.targetSdkVersion)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    // Anko
    implementation("org.jetbrains.anko:anko-commons:0.10.8")

    // Core
    implementation("com.google.android.material:material:1.0.0")
    implementation("androidx.core:core-ktx:1.0.1")
    implementation("androidx.fragment:fragment-ktx:1.0.0")

    // Crashlytics
    implementation("com.crashlytics.sdk.android:crashlytics:2.9.9")

    // Dagger
    implementation("com.google.dagger:dagger:${Deps.daggerVersion}")
    implementation("com.google.dagger:dagger-android:${Deps.daggerVersion}")
    implementation("com.google.dagger:dagger-android-support:${Deps.daggerVersion}")
    kapt("com.google.dagger:dagger-android-processor:${Deps.daggerVersion}")
    kapt("com.google.dagger:dagger-compiler:${Deps.daggerVersion}")

    // Firebase
    implementation("com.google.firebase:firebase-core:16.0.7")
    implementation("com.google.firebase:firebase-firestore:18.1.0")

    // Gson
    implementation("com.google.code.gson:gson:2.8.5")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Deps.kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${Deps.kotlinVersion}")

    // Location
    implementation("com.google.android.gms:play-services-location:16.0.0")

    // Maps
    implementation("com.google.android.gms:play-services-maps:16.1.0")
    implementation("com.google.maps.android:android-maps-utils:0.5")

    // Moxy
    implementation("com.arello-mobile:moxy:${Deps.moxyVersion}")
    kapt("com.arello-mobile:moxy-compiler:${Deps.moxyVersion}")

    // Multidex
    implementation("androidx.multidex:multidex:2.0.1")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:${Deps.retrofitVersion}")
    implementation("com.squareup.retrofit2:converter-gson:${Deps.retrofitVersion}")
    implementation("com.squareup.retrofit2:adapter-rxjava2:${Deps.retrofitVersion}")

    // Room
    implementation("androidx.room:room-runtime:${Deps.roomVersion}")
    implementation("androidx.room:room-rxjava2:${Deps.roomVersion}")
    kapt("androidx.room:room-compiler:${Deps.roomVersion}")

    // RxJava
    implementation("io.reactivex.rxjava2:rxjava:2.2.2")

    // RxAndroid
    implementation("io.reactivex.rxjava2:rxandroid:2.1.0")

    // Testing
    testImplementation("junit:junit:4.12")
    testImplementation("androidx.test:core:1.1.0")
    kaptAndroidTest("com.google.dagger:dagger-compiler:${Deps.daggerVersion}")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.0.0-RC3")
    androidTestImplementation("org.mockito:mockito-android:2.24.0")
}
