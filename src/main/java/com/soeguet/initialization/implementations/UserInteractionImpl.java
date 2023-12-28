package com.soeguet.initialization.implementations;

import com.soeguet.initialization.interfaces.UserInteractionInterface;
import javax.swing.*;

/**
 * Implementation of the UserInteraction interface. Provides methods for user interaction in a
 * graphical user interface (GUI) using JOptionPane.
 */
public class UserInteractionImpl implements UserInteractionInterface {

    /**
     * Asks the user to enter their timeAndUsername.
     *
     * @return The timeAndUsername entered by the user.
     */
    @Override
    public String askForUsername() {

        return JOptionPane.showInputDialog(null, "Please enter your timeAndUsername", "");
    }

    /**
     * Displays an error message dialog box with the specified message.
     *
     * @param message the error message to be displayed
     */
    @Override
    public void showError(final String message) {

        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
