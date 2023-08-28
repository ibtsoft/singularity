plugins {
    id("singularity.library-conventions")
}

dependencies {
    implementation(project(":singularity-core"))

    implementation("com.google.guava:guava:27.0.1-jre")
}
