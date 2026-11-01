plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.forgebound.render"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        targetSdk = 34
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":ui"))
    implementation(libs.compose.ui)
    implementation(libs.compose.uiGraphics)
    implementation(libs.compose.runtime)
}
