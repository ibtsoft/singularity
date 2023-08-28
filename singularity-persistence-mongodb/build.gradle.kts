plugins {
    id("singularity.library-conventions")
}

dependencies {
    implementation(project(":singularity-core"))

    implementation(libs.mongodb.java.driver)

    implementation("com.google.guava:guava:27.0.1-jre")

    testImplementation("org.testcontainers:mongodb:1.16.2")
}
