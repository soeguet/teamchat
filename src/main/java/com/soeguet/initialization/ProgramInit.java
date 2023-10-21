package com.soeguet.initialization;

import com.soeguet.emoji.EmojiInitializer;
import com.soeguet.emoji.interfaces.EmojiInitializerInterface;
import com.soeguet.gui.main_frame.ChatMainFrameImpl;
import com.soeguet.initialization.enums.Themes;
import com.soeguet.initialization.interfaces.EnvDataProviderInterface;
import com.soeguet.initialization.interfaces.UserInteractionInterface;
import com.soeguet.initialization.themes.interfaces.ThemeManager;
import com.soeguet.model.EnvVariables;
import com.soeguet.properties.CustomProperties;
import com.soeguet.properties.CustomUserProperties;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class ProgramInit {

    private final ThemeManager themeManager;

    /**
     Initializes the ProgramInit object with the given EnvDataProvider and UserInteraction objects.

     @param envDataProvider The object responsible for providing environmental data.
     @param userInteraction The object responsible for interacting with the user.
     @param themeManager    The object responsible for managing themes.
     */
    public ProgramInit(final ThemeManager themeManager) {

        this.themeManager = themeManager;
    }

    /**
     Initializes the graphical user interface of the application.
     This method creates a new instance of the ChatMainFrameImpl class,
     which represents the main frame of the chat application.
     The creation of the main frame is performed on the Event Dispatch Thread
     using the SwingUtilities.invokeLater() method, ensuring that the GUI is
     created and updated on the correct thread.

     @param envVariables the environment variables to be used by the application
     This object contains any required configuration variables
     or settings needed for the proper initialization of the GUI.
     It is up to the caller to provide a valid EnvVariables object.
     */
    public void initializeGUI(EnvVariables envVariables) {

        SwingUtilities.invokeLater(() -> {

            //TODO change this to interface
            ChatMainFrameImpl mainFrame = new ChatMainFrameImpl();

            //REMOVE remove later on
            mainFrame.repositionChatFrameForTestingPurposes();

            //setup functionality
            this.initializeProperties(mainFrame,envVariables);
            this.initializeMainFrame(envVariables, mainFrame);
            mainFrame.loadUsernameFromEnvVariables();

            //setup emojis
            EmojiInitializerInterface emojiInitializer = new EmojiInitializer();
            mainFrame.initEmojiList(emojiInitializer);

            //operating system specific settings
            mainFrame.setScrollPaneMargins();

            //setup GUI
            this.setMainFrameTitle(mainFrame);
            this.setGuiIcon(mainFrame);

            mainFrame.setButtonIcons();

            mainFrame.setVisible(true);
        });
    }

    private void initializeProperties(final ChatMainFrameImpl mainFrame, final EnvVariables envVariables) {

        CustomProperties customProperties = CustomProperties.getProperties();

        customProperties.setMainFrame(mainFrame);

        if (customProperties.checkIfConfigFileExists()) {

            customProperties.addCustomerToHashSet(new CustomUserProperties(envVariables.getChatUsername()));

            customProperties.saveProperties();
        }

        customProperties.loadProperties();
        customProperties.populateHashMapWithNewValues();
    }

    private void initializeMainFrame(final EnvVariables envVariables, final ChatMainFrameImpl mainFrame) {

        mainFrame.setEnvVariables(envVariables);
        mainFrame.loadCustomProperties();
        mainFrame.initGuiFunctionality();
        mainFrame.initializeClientController();
        mainFrame.initEmojiHandler();
    }

    private void setMainFrameTitle(final ChatMainFrameImpl mainFrame) {

        final String title = "teamchat" + " - " +
                chatVersion() +
                "username: " +
                mainFrame.getUsername();

        mainFrame.setTitle(title);
    }

    /**
     Sets the icon of the main frame in the graphical user interface.

     @param mainFrame the instance of ChatMainFrameImpl representing the main frame of the application.
     It is up to the caller to provide a valid instance of ChatMainFrameImpl.
     */
    private void setGuiIcon(final ChatMainFrameImpl mainFrame) {

        URL iconURL = ChatMainFrameImpl.class.getResource("/icon.png");
        assert iconURL != null;
        ImageIcon icon = new ImageIcon(iconURL);
        mainFrame.setIconImage(icon.getImage());
    }

    /**
     Retrieves the version information of the chat application.

     @return a string representing the version information of the chat application.
     If the version information is available in the "version.properties" file,
     it is retrieved and returned along with a suffix "- ". If the file is not found or an error occurs while reading the property,
     an empty string is returned.
     */
    private String chatVersion() {

        Properties properties = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("version.properties");

        if (inputStream != null) {

            try {

                properties.load(inputStream);
                return properties.getProperty("version") + " - ";

            } catch (IOException e) {

                throw new RuntimeException(e);
            }

        } else {

            return "";
        }
    }

    /**
     Sets the theme of the graphical user interface.

     This method applies the specified theme to the GUI using the ThemeManager.
     The themeSetting parameter should be a valid theme identifier supported
     by the application's ThemeManager. If an invalid or unsupported theme identifier
     is provided, the behavior of this method is undefined.

     @param themeSetting the theme identifier to be applied to the GUI.
     This should be a valid theme identifier supported by the application.
     It is up to the caller to provide a valid theme identifier.
     */
    public void setTheme(final Themes themeSetting) {

        themeManager.applyTheme(themeSetting);
    }
}