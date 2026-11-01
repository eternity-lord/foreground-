plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(kotlin("test"))        // kotlin.test annotations (JUnit-engine agnostic)
    testRuntimeOnly(libs.junit.jupiter.engine) // JUnit 5 engine for useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

tasks.named("test", Test::class) {
    useJUnitPlatform()
}
