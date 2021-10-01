plugins {
    `java-library`
}

repositories {
    jcenter()
}

dependencies {
    implementation( project(":singularity-core"))
    implementation( project(":singularity-security"))

    api("org.apache.commons:commons-math3:3.6.1")
    api("javax.websocket:javax.websocket-api:1.1")

    implementation("io.socket:socket.io-server:3.0.2")
    implementation("io.socket:engine.io-server-jetty:5.0.1")

    implementation("org.slf4j:slf4j-log4j12:1.7.30")
    implementation("org.eclipse.jetty:jetty-server:9.4.40.v20210413")
    implementation("org.eclipse.jetty:jetty-servlet:9.4.40.v20210413")
    implementation("org.eclipse.jetty.websocket:javax-websocket-server-impl:9.4.40.v20210413")
    implementation("com.google.guava:guava:27.0.1-jre")
    implementation("com.google.code.gson:gson:2.8.6")

    testImplementation("junit:junit:4.12")
}
