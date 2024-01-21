package com.soeguet.util;

public class EnvironmentData {

    /**
     Returns the name of the operating system.

     @return the name of the operating system.
     */
    public static String getOSName() {

        return System.getProperty("os.name");
    }

    // getter & setter -- start

    /**
     Returns the name of the user's current desktop environment on linux. The method retrieves the value of the
     environment variable "XDG_CURRENT_DESKTOP".

     @return the name of the user's current desktop environment or null if the variable is not set.
     */
    public static String getDesktopEnv() {

        return System.getenv("XDG_CURRENT_DESKTOP");
    }
}