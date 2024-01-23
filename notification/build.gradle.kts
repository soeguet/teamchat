plugins {
    id("com.soeguet.java-application-conventions")
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
    implementation(project(":misc"))
    implementation(project(":emoji"))
    implementation(project(":cache"))
    implementation(project(":client"))
}

tasks.test {
    useJUnitPlatform()
}