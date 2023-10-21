package com.soeguet.initialization;

import com.soeguet.emoji.EmojiInitializer;
import com.soeguet.emoji.interfaces.EmojiInitializerInterface;
import com.soeguet.initialization.enums.Themes;
import com.soeguet.initialization.interfaces.MainFrameInitInterface;
import com.soeguet.initialization.themes.interfaces.ThemeManager;
import com.soeguet.model.EnvVariables;
import com.soeguet.properties.CustomProperties;
import com.soeguet.properties.CustomUserProperties;

import javax.swing.*;

public class ProgramInit {

    private final ThemeManager themeManager;
    private final MainFrameInitInterface mainFrame;

    /**
     Initializes the program with the provided theme manager.

     @param themeManager The theme manager to be used by the program.
     */
    public ProgramInit(final MainFrameInitInterface mainFrame, final ThemeManager themeManager) {

        this.mainFrame = mainFrame;
        this.themeManager = themeManager;
    }

    /**
     Initializes the graphical user interface of the application.
     This method creates a new instance of the Chatthis.mainFrameImpl class,
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

            //REMOVE remove later on
            this.mainFrame.repositionChatFrameForTestingPurposes();

            //setup functionality
            this.initializeProperties(envVariables);
            this.initiliazeMainFrame();
            this.mainFrame.loadUsernameFromEnvVariables(envVariables);

            //setup emojis
            final EmojiInitializerInterface emojiInitializer = new EmojiInitializer();
            this.mainFrame.initEmojiList(emojiInitializer);

            //operating system specific settings
            this.mainFrame.setScrollPaneMargins();

            //setup GUI
            this.mainFrame.setMainFrameTitle();
            this.mainFrame.setGuiIcon();

            this.mainFrame.setButtonIcons();

            this.mainFrame.setVisible(true);
        });
    }

    private void initializeProperties(final EnvVariables envVariables) {

        CustomProperties customProperties = CustomProperties.getProperties();

        customProperties.setMainFrame(this.mainFrame);

        if (customProperties.checkIfConfigFileExists()) {

            customProperties.addCustomerToHashSet(new CustomUserProperties(envVariables.getChatUsername()));

            customProperties.saveProperties();
        }

        customProperties.loadProperties();
        customProperties.populateHashMapWithNewValues();
    }

    private void initiliazeMainFrame() {

        this.mainFrame.loadCustomProperties();
        this.mainFrame.initGuiFunctionality();
        this.mainFrame.initializeClientController();
        this.mainFrame.initEmojiHandler();
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