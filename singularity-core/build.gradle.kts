plugins {
    id("singularity.library-conventions")
}

dependencies {
    api("org.apache.commons:commons-math3:3.6.1")

    implementation("org.javassist:javassist:3.27.0-GA")
    implementation("com.google.guava:guava:27.0.1-jre")
}

