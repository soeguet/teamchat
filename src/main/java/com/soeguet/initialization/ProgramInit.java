package com.soeguet.initialization;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.soeguet.gui.main_frame.ChatMainFrameImpl;
import com.soeguet.model.EnvVariables;

import javax.swing.*;

/**
 The ProgramInit class initializes the program by setting up the necessary variables and launching the main frame.
 */
public class ProgramInit {

    /**
     Collects necessary data and starts GUI.
     */
    public ProgramInit() {

        //TODO: add theme setting
        //setTheme('');

        EnvVariables envVariables = checkForEnvVariables();
        FlatIntelliJLaf.setup();
        SwingUtilities.invokeLater(() -> new ChatMainFrameImpl(envVariables));
    }

    /**
     Checks for environment variables and retrieves the necessary data for the application.

     @return the environment variables (username, chat IP, and chat port)
     */
    private EnvVariables checkForEnvVariables() {

        //retrieve username
        final String username = retrieveUsername();
        promptForUserNameIfNeeded(username);

        //retrieve websocket relevant data
        String chatIp = getEnvData("chat.ip");
        String chatPort = getEnvData("chat.port");

        return new EnvVariables(username, chatIp, chatPort);
    }

    /**
     Retrieves the username from environment data if it exists, otherwise prompts the user for a username.

     @return the retrieved or prompted username
     */
    private String retrieveUsername() {

        final String chatUsername = getEnvData("chat.username");

        if (chatUsername != null && !chatUsername.isEmpty()) {

            return getEnvData("chat.username");

        } else {

            return askForUsername();
        }
    }

    /**
     Prompts the user for a username if one is needed.

     @param username the username to be checked
     */
    private void promptForUserNameIfNeeded(final String username) {

        if (username == null || username.isEmpty()) {

            SwingUtilities.invokeLater(() -> {

                JOptionPane.showMessageDialog(null, "Username must not be empty", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            });
        }
    }

    /**
     Gets the value of an environment variable specified by the given data.

     @param data the name of the environment variable

     @return the value of the environment variable, or null if the variable is not found
     */
    private String getEnvData(String data) {

        return System.getenv(data);
    }

    /**
     Prompts the user to enter their username.

     @return the username entered by the user
     */
    private static String askForUsername() {

        String username = JOptionPane.showInputDialog(null, "Please enter your username", "");

        // this lead to less hung ups on startup
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return username;
    }

    /**
     Sets the theme based on the given theme setting.

     @param themeSetting the theme setting to apply

     @throws IllegalArgumentException if themeSetting is null
     */
    private void setTheme(String themeSetting) {

        if (themeSetting == null) {
            throw new IllegalArgumentException("Theme setting must not be null");
        }

        switch (themeSetting.toUpperCase()) {
            case "LIGHT":
                FlatLightLaf.setup();
                break;
            case "DARK":
                FlatDarkLaf.setup();
                break;
            case "DARCULA":
                FlatDarculaLaf.setup();
                break;
            default:
                FlatIntelliJLaf.setup();
                break;
        }
    }
}