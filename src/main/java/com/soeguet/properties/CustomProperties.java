package com.soeguet.properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.gui.main_frame.interfaces.MainFrameInterface;
import com.soeguet.gui.popups.PopupPanelImpl;
import com.soeguet.gui.popups.interfaces.PopupInterface;
import com.soeguet.properties.interfaces.CustomPropertiesInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

public class CustomProperties extends Properties implements CustomPropertiesInterface {

    private static CustomProperties properties;
    private final Logger logger = Logger.getLogger(CustomProperties.class.getName());
    private final Set<CustomUserProperties> userProperties;
    private MainFrameInterface mainFrame;
    private String configFilePath;

    private CustomProperties() {

        userProperties = new HashSet<>();
    }

    public static CustomProperties getProperties() {

        if (properties == null) {

            properties = new CustomProperties();
        }

        return properties;
    }

    public void setMainFrame(final MainFrameInterface mainFrame) {

        this.mainFrame = mainFrame;
    }

    @Override
    public void checkIfConfigFileExists() {

        //TEST this

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

    @Override
    public void loadProperties() {

        try {

            load(Files.newInputStream(new File(configFilePath).toPath()));

        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }

    @Override
    public void populateHashMapWithNewValues() {

        ObjectMapper mapper = mainFrame.getObjectMapper();

        stringPropertyNames().forEach(key -> {

            try {

                CustomUserProperties userProperties = mapper.readValue(getProperty(key), CustomUserProperties.class);

                mainFrame.getChatClientPropertiesHashMap().remove(key);
                mainFrame.getChatClientPropertiesHashMap().put(key, userProperties);

            } catch (IOException e) {

                logger.log(java.util.logging.Level.SEVERE, "Could not load user " + key, e);
                throw new RuntimeException(e);
            }
        });

    }

    @Override
    public void saveProperties() {

        save();
    }

    @Override
    public void addCustomerToHashSet(final CustomUserProperties customUserProperties) {

        userProperties.add(customUserProperties);
    }

    @Override
    public CustomUserProperties getCustomUserProperties(final String username) {

        final Optional<CustomUserProperties> userProperty = userProperties.stream()
                .filter(customUserProperties -> customUserProperties.getUsername().equals(username))
                .findFirst();

        return userProperty.orElseThrow();
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

        PopupInterface popup = new PopupPanelImpl(mainFrame);
        popup.getMessageTextField().setText("properties saved");
        popup.configurePopupPanelPlacement();
        popup.initiatePopupTimer(2_000);

        createPropertiesFile(configFilePath);
    }

    private void createFolderIfNotPresent(final File appDir) {

        if (!appDir.exists()) {

            final boolean mkdir = appDir.mkdir();

            if (!mkdir) {

                logger.log(java.util.logging.Level.SEVERE, "ERROR! Could not create app dir!");
            }
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

    private boolean loadDataSuccessful() {

        try (FileInputStream input = new FileInputStream(configFilePath)) {

            load(input);
            return true;

        } catch (IOException e) {

            logger.log(java.util.logging.Level.SEVERE, "Could not load config file, creating..", e);
            return false;
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
}