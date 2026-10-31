import org.gradle.api.JavaVersion

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.forgebound.i18n"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
        targetSdk = 34
    }

    compileOptions {
        sourceCompatibility = JavaVersion.toVersion("17")
        targetCompatibility = JavaVersion.toVersion("17")
    }
}

dependencies {
    implementation(project(":core"))
    testImplementation(kotlin("test-junit"))
    testImplementation(libs.junit)
    testImplementation(project(":core"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}
