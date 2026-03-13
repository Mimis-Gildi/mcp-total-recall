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
    // MCP
    implementation(libs.mcp.server)
    implementation(libs.coroutines.core)

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

val generateBuildInfo by tasks.registering {
    val outputDir = layout.buildDirectory.dir("generated/source/buildinfo")
    inputs.property("version", project.version)
    outputs.dir(outputDir)

    doLast {
        outputDir.get().asFile.resolve("mimis/gildi/memory").apply {
            mkdirs()
            resolve("BuildInfo.kt").writeText(
                """
                |package mimis.gildi.memory
                |
                |object BuildInfo {
                |    const val VERSION = "${project.version}"
                |}
                """.trimMargin()
            )
        }
    }
}

kotlin.sourceSets.main {
    kotlin.srcDir(generateBuildInfo)
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}
