// Root build script. All dependency versions are declared in
// gradle/libs.versions.toml; subproject build files apply plugins and
// dependencies from that catalog. Configure this project only.

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
}

tasks.register("clean") {
    delete(rootProject.buildDir)
}
