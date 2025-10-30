plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.crmapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.crmapp"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    dependencies {
        implementation(platform("androidx.compose:compose-bom:2024.05.00"))
        implementation("androidx.compose.material3:material3")
        implementation("androidx.compose.ui:ui")
        implementation("androidx.compose.ui:ui-tooling-preview")
        implementation("androidx.activity:activity-compose")
        implementation("androidx.lifecycle:lifecycle-runtime-compose")

        // ✅ CORREÇÃO APLICADA AQUI
        implementation("androidx.navigation:navigation-compose:2.7.7")

        implementation("androidx.compose.material:material-icons-extended")
        implementation("androidx.compose.material3:material3-window-size-class")
        debugImplementation("androidx.compose.ui:ui-tooling")
        debugImplementation("androidx.compose.ui:ui-test-manifest")
    }
}
