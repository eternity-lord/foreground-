plugins {
    id("com.android.library") version "8.5.0"
    id("org.jetbrains.kotlin.android") version "2.0.20"
    id("org.jetbrains.kotlin.plugin.compose") version "1.5.8"
}

android {
    namespace = "com.forgebound.ui"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        targetSdk = 34
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":i18n"))
    implementation("androidx.compose.ui:ui:1.7.0")
    implementation("androidx.compose.ui:ui-graphics:1.7.0")
    implementation("androidx.compose.foundation:foundation:1.7.0")
    implementation("androidx.compose.material3:material3:1.7.0")
    implementation("androidx.compose.runtime:runtime:1.7.0")
    implementation("androidx.activity:activity-compose:1.9.0")
}
