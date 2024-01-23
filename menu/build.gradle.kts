plugins {
    id("java")
    id("com.diffplug.spotless") version "6.7.2"
}

spotless {
    java {
        importOrder()
        removeUnusedImports()
//    cleanthat()
        googleJavaFormat()
    }
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