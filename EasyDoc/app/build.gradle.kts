import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
    kotlin("plugin.serialization")
}

kotlin{
    compilerOptions{
        jvmTarget.set(JvmTarget.JVM_23)
    }
}

android {
    namespace = "com.n1ck120.easydoc"
    compileSdk = 36

    packaging {
        resources.excludes.add("META-INF/**")
    }

    defaultConfig {
        applicationId = "com.n1ck120.easydoc"
        minSdk = 29
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_23
        targetCompatibility = JavaVersion.VERSION_23
    }

    buildFeatures {
        viewBinding = false
    }
    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }

    //Libsodium
    sourceSets["main"].jniLibs.srcDirs("src/main/jniLibs")

    defaultConfig {
        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.ui.text.android)
    implementation(libs.androidx.preference)
    implementation(files("libs/poishadow-all.jar"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation (libs.kernel)
    implementation(libs.layout)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlin.reflect)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    //noinspection UseTomlInstead
    implementation("net.java.dev.jna:jna:5.17.0@aar")
    //noinspection UseTomlInstead
    implementation("com.goterl:lazysodium-android:5.2.0@aar")
    //Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    //annotationProcessor("androidx.room:room-compiler:$room_version")
    implementation(libs.androidx.room.ktx)
    //Token JWT
    api(libs.jjwt.api)
    runtimeOnly(libs.jjwt.impl)
    runtimeOnly("io.jsonwebtoken:jjwt-orgjson:0.12.6") {
        exclude("org.json", "json") //provided by Android natively
    }
}