package com.soeguet.initialization.interfaces;

/**
 Represents a user interaction interface.
 This interface provides methods for prompting the user to enter a username and displaying error messages.
 */
public interface UserInteraction {

    /**
     Prompts the user to enter a username.

     @return The username entered by the user as a String.
     */
    String askForUsername();

    /**
     Displays an error message to the user.

     @param message the error message to be displayed
     */
    void showError(String message);
}