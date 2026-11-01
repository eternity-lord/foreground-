plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(kotlin("test-junit"))  // kotlin.test -> JUnit 4 runner bridge
    testImplementation(libs.junit)            // JUnit 4 engine
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

tasks.named("test", Test::class) {
    useJUnit()
}
