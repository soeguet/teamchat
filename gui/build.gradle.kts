plugins {
    id("java")
}

group = "com.soeguet"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-core:2.16.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    implementation("com.miglayout:miglayout-swing:11.3")
    implementation("org.jsoup:jsoup:1.17.2")

    implementation(project(":misc"))
    implementation(project(":popups"))
    implementation(project(":bubble"))
    implementation(project(":cache"))
    implementation(project(":notification"))
    implementation(project(":emoji"))
    implementation(project(":properties"))

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}