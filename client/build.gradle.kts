plugins {
    id("java")
}

group = "com.soeguet"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":misc"))
    implementation(project(":popups"))
    implementation("com.miglayout:miglayout-swing:11.3")
    implementation("org.java-websocket:Java-WebSocket:1.5.5")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}