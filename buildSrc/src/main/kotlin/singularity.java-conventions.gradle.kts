plugins {
    java
    checkstyle
    idea
    id("com.github.spotbugs")
    id("org.owasp.dependencycheck")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.reactivex.rxjava3:rxjava:3.1.3")
    implementation("org.slf4j:slf4j-api:1.7.30")
    implementation("org.slf4j:slf4j-log4j12:1.7.30")
    implementation("org.apache.logging.log4j:log4j:2.14.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testImplementation("org.assertj:assertj-core:3.22.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

checkstyle {
    toolVersion = "10.11.0"
    maxWarnings = 0
}

spotbugs {
    ignoreFailures.set(true)
    showStackTraces.set(false)
}
