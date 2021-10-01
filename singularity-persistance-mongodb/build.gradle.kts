plugins {
    `java-library`
}

repositories {
    jcenter()
}

dependencies {
    implementation(project(":singularity-core"))

    implementation("org.mongodb:mongo-java-driver:2.12.3")

    implementation("com.google.guava:guava:27.0.1-jre")

    testImplementation("junit:junit:4.12")
}
