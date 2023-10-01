package com.soeguet.properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.gui.main_frame.MainFrameInterface;
import com.soeguet.gui.popups.PopupPanelImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;
import java.util.logging.Logger;

public class CustomProperties extends Properties {

    private final MainFrameInterface mainFrame;
    private final Logger logger = Logger.getLogger(CustomProperties.class.getName());
    private String configFilePath;

    public CustomProperties(MainFrameInterface mainFrame) {

        this.mainFrame = mainFrame;

        checkIfConfigFileExists();

        try {

            loadProperties();
            populateHashMapWithNewValues();

        } catch (IOException e) {

            throw new RuntimeException(e);
        }

    }

    private void checkIfConfigFileExists() {

        //handle the path in os filesystem
        String userHome = System.getProperty("user.home");
        String appDirPath = userHome + File.separator + ".teamchat";
        configFilePath = appDirPath + File.separator + "config.properties";

        //handle folder
        File appDir = new File(appDirPath);
        createFolderIfNotPresent(appDir);

        //try loading data from file
        if (!new File(configFilePath).exists()) {

            //create file if not present
            createPropertiesFile(configFilePath);
        }
    }

    private void loadProperties() throws IOException {

        load(Files.newInputStream(new File(configFilePath).toPath()));
    }

    private void populateHashMapWithNewValues() {

        ObjectMapper mapper = mainFrame.getObjectMapper();

        stringPropertyNames().forEach(key -> {

            try {

                CustomUserProperties userProperties = mapper.readValue(getProperty(key), CustomUserProperties.class);

                mainFrame.getChatClientPropertiesHashMap().remove(key);
                mainFrame.getChatClientPropertiesHashMap().put(key, userProperties);

            } catch (IOException e) {

                logger.log(java.util.logging.Level.SEVERE, "Could not load user " + key, e);
            }
        });

    }

    private void createFolderIfNotPresent(final File appDir) {

        if (!appDir.exists()) {

            final boolean mkdir = appDir.mkdir();
            if (!mkdir) {

                logger.log(java.util.logging.Level.SEVERE, "ERROR! Could not create app dir!");
            }
        }
    }

    private boolean loadDataSuccessful() {

        try (FileInputStream input = new FileInputStream(configFilePath)) {

            load(input);
            return true;

        } catch (IOException e) {

            logger.log(java.util.logging.Level.SEVERE, "Could not load config file, creating..", e);
            return false;
        }
    }

    private void createPropertiesFile(String configFilePath) {

        try (FileOutputStream output = new FileOutputStream(configFilePath)) {

            store(output, null);

        } catch (IOException e) {

            logger.log(java.util.logging.Level.SEVERE, "ERROR: Could not create config file!", e);
            throw new RuntimeException(e);
        }
    }

    public CustomUserProperties loaderThisClientProperties() {

        final String clientProperties = getProperty("own");

        //replace "own" preset username
        if (this.mainFrame.getUsername() != null) {
            return new CustomUserProperties(this.mainFrame.getUsername());
        }

        if (clientProperties == null || clientProperties.isEmpty()) {

            return null;
        }

        try {

            return this.mainFrame.getObjectMapper().readValue(clientProperties, CustomUserProperties.class);

        } catch (JsonProcessingException e) {

            logger.log(java.util.logging.Level.SEVERE, "ERROR: Could not load own properties!", e);
        }

        return null;
    }

    public void save() {

        ObjectMapper mapper = mainFrame.getObjectMapper();

        mainFrame.getChatClientPropertiesHashMap().forEach((key, value) -> {

            String json = null;
            try {
                json = mapper.writeValueAsString(value);

            } catch (IOException e) {

                logger.log(java.util.logging.Level.SEVERE, "ERROR: Could not save user " + key, e);
            }

            setProperty(key, json);
        });

        new PopupPanelImpl(mainFrame, "properties saved").implementPopup(1000);

        createPropertiesFile(configFilePath);
    }
}