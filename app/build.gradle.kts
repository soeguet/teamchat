/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    id("com.soeguet.java-application-conventions")
}

dependencies {
    implementation("org.apache.commons:commons-text")
    implementation("com.miglayout:miglayout-swing:11.3")
    implementation("com.fasterxml.jackson.core:jackson-core:2.16.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    implementation("org.java-websocket:Java-WebSocket:1.5.5")
    implementation("org.jsoup:jsoup:1.17.2")
    implementation("com.dorkbox:Notify:4.5")
    implementation("com.formdev:flatlaf:3.3")

    implementation(project(":bubble"))
    implementation(project(":notification"))
    implementation(project(":misc"))
    implementation(project(":popups"))
    implementation(project(":emoji"))
    implementation(project(":properties"))
    implementation(project(":gui"))
}

application {
    // Define the main class for the application.
    mainClass.set("com.soeguet.app.App")
}