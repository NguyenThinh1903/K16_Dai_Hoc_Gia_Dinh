plugins {
    id("com.android.application")
    id("kotlin-android")
    // The Flutter Gradle Plugin must be applied after the Android and Kotlin Gradle plugins.
    id("dev.flutter.flutter-gradle-plugin")
}

android {
    namespace = "com.example.memorymatch"
    compileSdk = 34 // Sử dụng giá trị cố định thay vì flutter.compileSdkVersion để đảm bảo tương thích
    ndkVersion = "27.0.12077973" // Cố định NDK phiên bản yêu cầu bởi Firebase

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    defaultConfig {
        // TODO: Specify your own unique Application ID (https://developer.android.com/studio/build/application-id.html).
        applicationId = "com.example.memorymatch"
        // You can update the following values to match your application needs.
        // For more information, see: https://flutter.dev/to/review-gradle-config.
        minSdk = 21 // Hoặc flutter.minSdkVersion nếu muốn động
        targetSdk = 34 // Hoặc flutter.targetSdkVersion nếu muốn động
        versionCode = 1 // Hoặc flutter.versionCode nếu muốn động
        versionName = "1.0" // Hoặc flutter.versionName nếu muốn động
    }

    buildTypes {
        release {
            // TODO: Add your own signing config for the release build.
            // Signing with the debug keys for now, so `flutter run --release` works.
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

flutter {
    source = "../.."
}