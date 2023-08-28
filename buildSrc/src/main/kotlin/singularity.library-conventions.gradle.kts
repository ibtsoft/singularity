plugins {
    `java-library`
    `maven-publish`
    id("singularity.java-conventions")
}

group = "com.singularity"
version = "0.0.1-SNAPSHOT"

configure<PublishingExtension> {
    publications {
        create<MavenPublication>("singularity") {
            from(components["java"])
        }
    }
}
