plugins {
    alias(libs.plugins.kotest)
    kotlin("jvm") version libs.versions.kotlin
    application
}

val useJavaVersion: String by project

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(useJavaVersion.toInt())
}

dependencies {
    // Logging
    implementation(libs.slf4j.api)
    implementation(libs.kotlin.logging)
    implementation(libs.logback.classic)

    // Testing
    testImplementation(libs.kotest.framework)
    testImplementation(libs.kotest.runner)
    testImplementation(libs.kotest.assertions)
}

application {
    mainClass.set("mimis.gildi.memory.TotalRecallKt")
}

tasks.test {
    useJUnitPlatform()
}
