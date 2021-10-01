
repositories {
    jcenter()
}

dependencies {
    //api("org.apache.commons:commons-math3:3.6.1")
    implementation(project(":singularity-core"))

    implementation("com.google.guava:guava:27.0.1-jre")

    testImplementation("junit:junit:4.12")
}
