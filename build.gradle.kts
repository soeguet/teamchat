plugins {
    id("java")
}

subprojects {
}

dependencies {
}

tasks.register<Copy>("copyGuiResources") {
    from(project(":gui").file("src/main/resources"))
    into(file("src/main/resources"))
}