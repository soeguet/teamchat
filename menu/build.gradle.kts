plugins {
    id("java")
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