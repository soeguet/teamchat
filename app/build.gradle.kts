plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("com.soeguet.java-application-conventions")
}

dependencies {
    implementation(project(":bubble"))
    implementation(project(":notification"))
    implementation(project(":misc"))
    implementation(project(":popups"))
    implementation(project(":emoji"))
    implementation(project(":properties"))
    implementation(project(":gui"))
}

application {
    mainClass.set("com.soeguet.Main")
}

tasks.shadowJar {
    archiveClassifier.set("")
    manifest {
        attributes("Main-Class" to "com.soeguet.Main")
    }
}