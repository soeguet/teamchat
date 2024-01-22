plugins {
    id("com.soeguet.java-application-conventions")
}

group = "com.soeguet"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":misc"))
    implementation(project(":popups"))
}

tasks.test {
    useJUnitPlatform()
}