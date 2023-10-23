package com.soeguet.initialization;

import com.soeguet.emoji.EmojiInitializer;
import com.soeguet.emoji.interfaces.EmojiInitializerInterface;
import com.soeguet.initialization.interfaces.MainFrameInitInterface;
import com.soeguet.model.EnvVariables;
import com.soeguet.properties.CustomProperties;
import com.soeguet.properties.dto.CustomUserPropertiesDTO;

import javax.swing.*;

public class ProgramInit {

    private final MainFrameInitInterface mainFrame;

    public ProgramInit(final MainFrameInitInterface mainFrame) {

        this.mainFrame = mainFrame;
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
            this.initializeMainFrame();
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

            customProperties.addCustomerToHashSet(new CustomUserPropertiesDTO(envVariables.getChatUsername(), null, null));

            customProperties.saveProperties();
        }

        customProperties.loadProperties();
        customProperties.populateHashMapWithNewValues();
    }

    private void initializeMainFrame() {

        this.mainFrame.loadCustomProperties();
        this.mainFrame.initGuiFunctionality();
        this.mainFrame.initializeClientController();
        this.mainFrame.initEmojiHandler();
    }
}