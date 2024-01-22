plugins {
    id("com.soeguet.java-application-conventions")
}

group = "com.soeguet"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
}

tasks.test {
    useJUnitPlatform()
}