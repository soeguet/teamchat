import org.gradle.internal.impldep.org.eclipse.jgit.util.RawCharUtil.trimTrailingWhitespace

plugins {
    // Apply the java Plugin to add support for Java.
    java
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}
project.version = "1.0.7"

dependencies {
    implementation("com.miglayout:miglayout-swing:11.3")
    implementation("com.fasterxml.jackson.core:jackson-core:2.16.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    implementation("org.java-websocket:Java-WebSocket:1.5.5")
    implementation("org.jsoup:jsoup:1.17.2")
    implementation("com.dorkbox:Notify:4.5")
    implementation("com.formdev:flatlaf:3.3")

    // Use JUnit Jupiter for testing.
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}