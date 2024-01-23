package com.soeguet.initialization.interfaces;

/**
 * Represents a user interaction interface. This interface provides methods for prompting the user
 * to enter a timeAndUsername and displaying error messages.
 */
public interface UserInteractionInterface {

  /**
   * Prompts the user to enter a timeAndUsername.
   *
   * @return The timeAndUsername entered by the user as a String.
   */
  String askForUsername();

  /**
   * Displays an error message to the user.
   *
   * @param message the error message to be displayed
   */
  void showError(String message);
}
