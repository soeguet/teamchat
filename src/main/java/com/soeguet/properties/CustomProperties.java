package com.soeguet.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class CustomProperties extends Properties {

    public CustomProperties() {

        String userHome = System.getProperty("user.home");

        String appDirPath = userHome + File.separator + ".teamchat";
        String configFilePath = appDirPath + File.separator + "config.properties";

        File appDir = new File(appDirPath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }

        // Versuchen, die Konfigurationsdatei zu laden
        try (FileInputStream input = new FileInputStream(configFilePath)) {
            load(input);
        } catch (IOException e) {
            // Datei nicht gefunden, Standardwerte setzen
            setProperty("username", "defaultUser");
            setProperty("theme", "light");

            // Neue Konfigurationsdatei speichern
            try (FileOutputStream output = new FileOutputStream(configFilePath)) {
                store(output, null);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }


        // Einstellungen verwenden
        String username = getProperty("username");
        String theme = getProperty("theme");
        System.out.println("username = " + username);
        System.out.println("theme = " + theme);
    }
}
