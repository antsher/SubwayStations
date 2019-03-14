buildscript {
    repositories {
        google()
        jcenter()
        maven { url = uri("https://maven.fabric.io/public") }
    }

    dependencies {
        classpath("com.android.tools.build:gradle:3.3.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Deps.kotlinVersion}")
        classpath("com.google.gms:google-services:4.2.0")
        classpath("io.fabric.tools:gradle:1.26.1")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { url = uri("https://maven.google.com/") }
    }
}
