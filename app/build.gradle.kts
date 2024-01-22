plugins {
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

//tasks.jar {
//    manifest {
//        attributes["Main-Class"] = "com.soeguet.Main"
//    }
//}
////
//application {
//    // Define the main class for the application.
//    mainClass.set("com.soeguet.app.App")
//}