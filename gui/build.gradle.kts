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
    implementation(project(":bubble"))
    implementation(project(":client"))
    implementation(project(":cache"))
    implementation(project(":notification"))
    implementation(project(":emoji"))
    implementation(project(":properties"))
}

tasks.test {
    useJUnitPlatform()
}