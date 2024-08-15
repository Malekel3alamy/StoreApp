


buildscript {

    repositories {
        google()
        gradlePluginPortal()

    }
    dependencies {
        val navVersion = "2.7.7"
        classpath(libs.androidx.navigation.safe.args.gradle.plugin)
        classpath ("com.google.gms:google-services:4.4.2")
    }
}


plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    alias(libs.plugins.google.gms.google.services) apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false

}

