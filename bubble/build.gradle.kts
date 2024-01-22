plugins {
    id("com.soeguet.java-application-conventions")
}

group = "com.soeguet"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":client"))
    implementation(project(":misc"))
    implementation(project(":emoji"))
    implementation(project(":properties"))
}

tasks.test {
    useJUnitPlatform()
}