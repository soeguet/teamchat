package com.soeguet.initialization;

import com.soeguet.gui.main_frame.ChatMainFrameImpl;
import com.soeguet.initialization.enums.Themes;
import com.soeguet.initialization.interfaces.EnvDataProvider;
import com.soeguet.initialization.interfaces.UserInteraction;
import com.soeguet.initialization.themes.interfaces.ThemeManager;
import com.soeguet.model.EnvVariables;

import javax.swing.*;

public class ProgramInit {

    private final EnvDataProvider envDataProvider;
    private final UserInteraction userInteraction;
    private final ThemeManager themeManager;

    /**
     Initializes the ProgramInit object with the given EnvDataProvider and UserInteraction objects.

     @param envDataProvider The object responsible for providing environmental data.
     @param userInteraction The object responsible for interacting with the user.
     @param themeManager    The object responsible for managing themes.
     */
    public ProgramInit(EnvDataProvider envDataProvider, UserInteraction userInteraction, final ThemeManager themeManager) {

        this.envDataProvider = envDataProvider;
        this.userInteraction = userInteraction;
        this.themeManager = themeManager;
    }

    /**
     Collects the environment variables needed for program initialization.

     @return An EnvVariables object containing the collected environment variables.
     */
    public EnvVariables collectEnvVariables() {

        final String username = retrieveUsername();
        promptForUserNameIfNeeded(username);
        final String chatIp = envDataProvider.getEnvData("CHAT_IP");
        final String chatPort = envDataProvider.getEnvData("CHAT_PORT");
        return new EnvVariables(username, chatIp, chatPort);
    }

    /**
     Retrieves the username for the chat.

     @return The username for the chat.
     */
    private String retrieveUsername() {

        String chatUsername = envDataProvider.getEnvData("CHAT_USERNAME");
        return (chatUsername != null && !chatUsername.isEmpty()) ? chatUsername : userInteraction.askForUsername();
    }

    /**
     Prompts the user for a username, if needed.
     If the given username is null or empty, an error message is shown.

     @param username The username to check.
     */
    private void promptForUserNameIfNeeded(String username) {

        if (username == null || username.isEmpty()) {
            userInteraction.showError("Username must not be empty");
        }
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

        SwingUtilities.invokeLater(() -> new ChatMainFrameImpl(envVariables));
    }

    /**
     Sets the theme of the graphical user interface.

     This method applies the specified theme to the GUI using the ThemeManager.
     The themeSetting parameter should be a valid theme identifier that is supported
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