buildscript {
    repositories {
        google()
        mavenCentral()

        // other repositories...
    }
    dependencies {

        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.7")
        classpath("com.google.gms:google-services:4.4.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.22") // Use the latest Kotlin version
        // Include KSP plugin classpath if you are going to use KSP
        classpath("com.google.devtools.ksp:symbol-processing-gradle-plugin:<ksp_version>")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.44")


    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.4.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.23" apply false
    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false

}