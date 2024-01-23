package com.soeguet.initialization;

import com.soeguet.dtos.CustomUserPropertiesDTO;
import com.soeguet.emoji.EmojiInitializer;
import com.soeguet.emoji.EmojiRegister;
import com.soeguet.emoji.interfaces.EmojiInitializerInterface;
import com.soeguet.main_frame.ChatMainFrameImpl;
import com.soeguet.model.EnvVariables;
import com.soeguet.properties.CustomProperties;

import javax.swing.*;

public class ProgramInit {

    private final ChatMainFrameImpl mainFrame;

    public ProgramInit() {

        this.mainFrame = ChatMainFrameImpl.getMainFrameInstance();
    }

    /**
     * Initializes the graphical user interface of the application. This method creates a new
     * instance of the Chatthis.mainFrameImpl class, which represents the main frame of the chat
     * application. The creation of the main frame is performed on the Event Dispatch Thread using
     * the SwingUtilities.invokeLater() method, ensuring that the GUI is created and updated on the
     * correct thread.
     *
     * @param envVariables the environment variables to be used by the application This object
     *     contains any required configuration variables or settings needed for the proper
     *     initialization of the GUI. It is up to the caller to provide a valid EnvVariables object.
     */
    public void initializeGUI(final EnvVariables envVariables) {

        SwingUtilities.invokeLater(
                () -> {

                    // REMOVE remove later on
                    // module ::gui
                    this.mainFrame.repositionChatFrameForTestingPurposes();

                    // setup emojis
                    // module ::emoji
                    final EmojiInitializerInterface emojiInitializer = new EmojiInitializer();
                    EmojiRegister emojiRegister = EmojiRegister.getEmojiRegisterInstance();
                    emojiRegister.initEmojiList(emojiInitializer);

                    // setup functionality
                    this.initializeProperties(envVariables);
                    this.initializeMainFrame();
                    this.mainFrame.loadUsernameFromEnvVariables(envVariables);

                    // add system tray icon
                    this.mainFrame.setupSystemTrayIcon();

                    // operating system specific settings - margins for JScrollPane
                    this.mainFrame.setScrollPaneMargins();

                    // setup GUI
                    this.mainFrame.setMainFrameTitle();
                    this.mainFrame.setFixedScrollSpeed(25);
                    this.mainFrame.setGuiIcon();

                    this.mainFrame.setButtonIcons();

                    this.mainFrame.setVisible(true);
                });
    }

    private void initializeProperties(final EnvVariables envVariables) {

        // module ::properties
        final CustomProperties customProperties = CustomProperties.getPropertiesInstance();

        if (customProperties.checkIfConfigFileExists()) {

            customProperties.addCustomerToHashSet(
                    new CustomUserPropertiesDTO(envVariables.getChatUsername(), null, null));

            customProperties.saveProperties();
        }

        customProperties.loadProperties();
        customProperties.populateHashMapWithNewValues();
    }

    private void initializeMainFrame() {

        // module ::gui
        this.mainFrame.loadCustomProperties();
        this.mainFrame.initGuiFunctionality();
        this.mainFrame.initializeClientController();
    }
}