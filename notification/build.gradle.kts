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
    implementation(project(":emoji"))
    implementation(project(":cache"))
    implementation(project(":client"))
}

tasks.test {
    useJUnitPlatform()
}