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
version = "1.0.7"

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

val generateVersionClass by tasks.registering {
    doLast {
        val versionClassFile = file("src/main/java/com/soeguet/Version.java")
        versionClassFile.writeText("""
            package com.soeguet;

            public class Version {
                public static final String VERSION = "${project.version}";
            }
        """.trimIndent())
    }
}

tasks.compileJava {
    dependsOn(generateVersionClass)
}