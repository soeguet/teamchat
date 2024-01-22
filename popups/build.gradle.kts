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
    implementation(project(":cache"))
}

tasks.test {
    useJUnitPlatform()
}