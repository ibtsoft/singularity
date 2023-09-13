plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.8.10" //1.8.21 conflicts with the kotlin-dsl version above
}

repositories {
    gradlePluginPortal() // so that external plugins can be resolved in dependencies section
}

dependencies {
    implementation("gradle.plugin.com.github.spotbugs.snom:spotbugs-gradle-plugin:4.7.2")
    implementation("org.owasp:dependency-check-gradle:6.5.1")
}

kotlin {
    jvmToolchain(17)
}
