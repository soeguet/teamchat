package com.soeguet.properties;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.gui.popups.PopupPanelImpl;
import com.soeguet.gui.popups.interfaces.PopupInterface;
import com.soeguet.initialization.interfaces.MainFrameInitInterface;
import com.soeguet.properties.dto.CustomUserPropertiesDTO;
import com.soeguet.properties.interfaces.CustomPropertiesInterface;

public class CustomProperties extends Properties implements CustomPropertiesInterface {

    private static CustomProperties properties;
    private final Logger logger = Logger.getLogger(CustomProperties.class.getName());
    private final Set<CustomUserPropertiesDTO> userPropertiesHashSet;
    private MainFrameGuiInterface mainFrame;
    private String configFilePath;

    private CustomProperties() {

        userPropertiesHashSet = new HashSet<>();
    }

    public static CustomProperties getProperties() {

        if (properties == null) {

            properties = new CustomProperties();
        }

        return properties;
    }

    public void setMainFrame(final MainFrameInitInterface mainFrame) {

        if (mainFrame instanceof MainFrameGuiInterface mainFrameGui) {

            this.mainFrame = mainFrameGui;

        } else {

            throw new IllegalArgumentException("ERROR: MainFrameInitInterface must be of type MainFrameGuiInterface!");
        }
    }

    @Override
    public boolean checkIfConfigFileExists() {

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

            //createQuoteTopTextPane file if not present
            createPropertiesFile(configFilePath);
            return true;
        }

        return false;
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

                CustomUserPropertiesDTO userProperties = mapper.readValue(getProperty(key), CustomUserPropertiesDTO.class);

                mainFrame.getChatClientPropertiesHashMap().replace(key, userProperties);

            } catch (IOException e) {

                logger.log(java.util.logging.Level.SEVERE, "Could not load user " + key, e);
                throw new RuntimeException(e);
            }
        });

    }

    @Override
    public void saveProperties() {

        ObjectMapper mapper = mainFrame.getObjectMapper();

        this.userPropertiesHashSet.forEach(user -> {

            String json = null;

            try {

                json = mapper.writeValueAsString(user);

            } catch (IOException e) {

                logger.log(java.util.logging.Level.SEVERE, "ERROR: Could not save user " + user.username(), e);
                throw new RuntimeException();
            }

            setProperty(user.username(), json);
        });

        PopupInterface popup = new PopupPanelImpl(mainFrame);
        popup.getMessageTextField().setText("properties saved");
        popup.configurePopupPanelPlacement();
        popup.initiatePopupTimer(2_000);

        createPropertiesFile(configFilePath);
    }

    @Override
    public void addCustomerToHashSet(final CustomUserPropertiesDTO customUserProperties) {

        userPropertiesHashSet.add(customUserProperties);
    }

    @Override
    public CustomUserPropertiesDTO getCustomUserProperties(final String username) {

        final Optional<CustomUserPropertiesDTO> userProperty =
                userPropertiesHashSet.stream().filter(customUserProperties -> customUserProperties.username().equals(username)).findFirst();

        return userProperty.orElseThrow();
    }

    private void createFolderIfNotPresent(final File appDir) {

        if (!appDir.exists()) {

            final boolean mkdir = appDir.mkdir();

            if (!mkdir) {

                logger.log(java.util.logging.Level.SEVERE, "ERROR! Could not createQuoteTopTextPane app dir!");
                throw new RuntimeException();
            }
        }
    }

    private void createPropertiesFile(String configFilePath) {

        try (FileOutputStream output = new FileOutputStream(configFilePath)) {

            store(output, null);

        } catch (IOException e) {

            logger.log(java.util.logging.Level.SEVERE, "ERROR: Could not createQuoteTopTextPane config file!", e);
            throw new RuntimeException(e);
        }
    }

    public void save() {

        ObjectMapper mapper = mainFrame.getObjectMapper();

        mainFrame.getChatClientPropertiesHashMap().forEach((key, value) -> {

            String json = null;

            try {

                json = mapper.writeValueAsString(value);

            } catch (IOException e) {

                logger.log(java.util.logging.Level.SEVERE, "ERROR: Could not save user " + key, e);
                throw new RuntimeException();
            }

            setProperty(key, json);
        });

        PopupInterface popup = new PopupPanelImpl(mainFrame);
        popup.getMessageTextField().setText("properties saved");
        popup.configurePopupPanelPlacement();
        popup.initiatePopupTimer(2_000);

        createPropertiesFile(configFilePath);
    }

    public CustomUserPropertiesDTO loaderThisClientProperties() {

        final String clientProperties = getProperty("own");

        //replace "own" preset timeAndUsername
        if (this.mainFrame.getUsername() != null) {

            return new CustomUserPropertiesDTO(this.mainFrame.getUsername(), null, null);
        }

        if (clientProperties == null || clientProperties.isEmpty()) {

            return null;
        }

        try {

            return this.mainFrame.getObjectMapper().readValue(clientProperties, CustomUserPropertiesDTO.class);

        } catch (JsonProcessingException e) {

            logger.log(java.util.logging.Level.SEVERE, "ERROR: Could not load own properties!", e);
            logger.log(java.util.logging.Level.SEVERE, "CustomProperties > loaderThisClientProperties()", e);
        }

        return null;
    }
}
