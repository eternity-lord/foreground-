plugins {
    id("com.android.library") version "8.5.0"
    id("org.jetbrains.kotlin.android") version "2.0.20"
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.20"
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
    implementation("androidx.compose.ui:ui:1.3.0")
    implementation("androidx.compose.ui:ui-graphics:1.3.0")
    implementation("androidx.compose.runtime:runtime:1.3.0")
}
