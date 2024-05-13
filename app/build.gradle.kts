plugins {

    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.farmin"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.farmin"
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation ("androidx.core:core-ktx:1.13.1")
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-storage")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.journeyapps:zxing-android-embedded:4.3.0") // qr code
    implementation("com.github.bumptech.glide:glide:4.14.2") // image url loader
    annotationProcessor ("com.github.bumptech.glide:compiler:4.14.2") // image url loader
    implementation("androidx.core:core:1.13.0") // for utility functionality
    implementation("org.apache.poi:poi:5.2.5") // excel
    implementation("org.apache.poi:poi-ooxml:5.2.5") //excel
    implementation("com.squareup.retrofit2:retrofit:2.9.0") // network requests to web API for the weather
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // converter of what has been passed
    implementation("com.google.code.gson:gson:2.8.8") // gson
    implementation("com.google.android.gms:play-services-location:21.2.0") // location
}