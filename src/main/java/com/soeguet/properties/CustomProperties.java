package com.soeguet.properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.gui.main_frame.MainGuiElementsInterface;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.util.Properties;
import java.util.logging.Logger;

public class CustomProperties extends Properties {

    private final JFrame mainFrame;
    private final Logger logger = Logger.getLogger(CustomProperties.class.getName());
    private String configFilePath;

    public CustomProperties(JFrame mainFrame) {

        this.mainFrame = mainFrame;

        checkIfConfigFileExists();

        try {

            loadProperties();
            populateHashMapWithNewValues();

        } catch (IOException e) {

            throw new RuntimeException(e);
        }


    }

    private void populateHashMapWithNewValues() {


        MainGuiElementsInterface gui = getMainFrame();
        assert gui != null;

        stringPropertyNames().forEach(key -> {

            String json = getProperty(key);

            ObjectMapper mapper = gui.getObjectMapper();

            try {

                CustomUserProperties userProperties = mapper.readValue(json, CustomUserProperties.class);

                gui.getChatClientPropertiesHashMap().remove(key);
                gui.getChatClientPropertiesHashMap().put(key, userProperties);

            } catch (IOException e) {

                logger.log(java.util.logging.Level.SEVERE, "Could not load user " + key, e);
            }
        });

    }

    private void checkIfConfigFileExists() {

        String userHome = System.getProperty("user.home");

        String appDirPath = userHome + File.separator + ".teamchat";
        configFilePath = appDirPath + File.separator + "config.properties";

        File appDir = new File(appDirPath);

        if (!appDir.exists()) {

            appDir.mkdir();
        }

        try (FileInputStream input = new FileInputStream(configFilePath)) {

            load(input);

        } catch (IOException e) {

            createPropertiesFile(configFilePath);
        }
    }

    private void createPropertiesFile(String configFilePath) {

        try (FileOutputStream output = new FileOutputStream(configFilePath)) {

            store(output, null);

        } catch (IOException ioException) {

            logger.log(java.util.logging.Level.SEVERE, "Could not save config file", ioException);
        }
    }

    private void loadProperties() throws IOException {

        load(Files.newInputStream(new File(configFilePath).toPath()));
    }

    public void save() {

        System.out.println("Saving properties triggered!");

        MainGuiElementsInterface gui = getMainFrame();
        assert gui != null;

        ObjectMapper mapper = gui.getObjectMapper();

        gui.getChatClientPropertiesHashMap().forEach((key, value) -> {

            String json = null;
            try {
                json = mapper.writeValueAsString(value);

            } catch (IOException e) {

                logger.log(java.util.logging.Level.SEVERE, "Could not save user " + key, e);
            }

            setProperty(key, json);

            logger.info("Saved user " + key);
        });

        createPropertiesFile(configFilePath);
    }

    private MainGuiElementsInterface getMainFrame() {

        if (!(mainFrame instanceof MainGuiElementsInterface)) {
            return null;
        }
        return (MainGuiElementsInterface) mainFrame;
    }

    private void random() {

        // Versuchen, die Konfigurationsdatei zu laden

        // Einstellungen verwenden
        String username = getProperty("username");
        String theme = getProperty("theme");
        System.out.println("username = " + username);
        System.out.println("theme = " + theme);
    }

}
