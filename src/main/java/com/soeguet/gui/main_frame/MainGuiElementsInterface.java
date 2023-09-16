package com.soeguet.gui.main_frame;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.behaviour.GuiFunctionality;
import com.soeguet.properties.CustomUserProperties;
import com.soeguet.socket_client.CustomWebsocketClient;

import javax.swing.*;
import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Queue;

/**
 * The MainGuiElementsInterface is an interface that defines the main GUI elements and functionalities
 * required by the application.
 */
public interface MainGuiElementsInterface {

    /**
     * Retrieves the JTextPane component of the text editor.
     *
     * @return the JTextPane component of the text editor
     */
    JTextPane getTextEditorPane();

    /**
     * Retrieves an instance of the CustomWebsocketClient.
     *
     * @return The CustomWebsocketClient instance.
     */
    CustomWebsocketClient getWebsocketClient();

    /**
     * Returns the main text panel.
     *
     * @return the JPanel representing the main text panel, on which chat bubbles are displayed
     */
    JPanel getMainTextPanel();

    /**
     * Returns the JScrollPane that contains the main text panel.
     *
     * @return the JScrollPane representing the scrollable area for the main text panel,
     * on which chat bubbles are displayed
     */
    JScrollPane getMainTextBackgroundScrollPane();

    /**
     * Returns the ObjectMapper instance used for JSON serialization and deserialization.
     *
     * @return the ObjectMapper instance used for JSON serialization and deserialization
     */
    ObjectMapper getObjectMapper();

    /**
     * Returns the GuiFunctionality object that represents the current state and functionality of the GUI.
     *
     * @return the GuiFunctionality object representing the GUI state and functionality
     */
    GuiFunctionality getGuiFunctionality();

    /**
     * Returns the name of the last message sender.
     *
     * @return the name of the last message sender as a String
     */
    String getLastMessageSenderName();

    /**
     * Returns the time stamp of the last message.
     *
     * @return the time stamp of the last message as a String
     */
    String getLastMessageTimeStamp();


    /**
     * Sets the time stamp of the last message.
     *
     * @param lastMessageTimeStamp the time stamp of the last message as a String
     */
    void setLastMessageTimeStamp(String lastMessageTimeStamp);

    /**
     * Sets the sender name of the last message.
     *
     * @param lastMessageSenderName the sender name of the last message as a String
     */
    void setLastMessageSenderName(String lastMessageSenderName);

    /**
     * Returns the layered pane that contains the main text panel.
     *
     * @return the layered pane that contains the main text panel
     */
    JLayeredPane getMainTextPanelLayeredPane();

    /**
     * Returns the username associated with the current user.
     *
     * @return the username as a String
     */
    String getUsername();

    /**
     * Sets the username for the current user.
     *
     * @param username the new username to be set
     */
    void setUsername(String username);

    /**
     * Retrieves the main JFrame of the application.
     *
     * @return the main JFrame of the application
     */
    JFrame getMainFrame();

    /**
     * Retrieves the message panel of the application.
     *
     * @return the message panel of the application
     */
    JPanel getMessagePanel();

    /**
     * Sets the message panel of the application.
     *
     * @param messagePanel the message panel to be set for the application
     */
    void setMessagePanel(JPanel messagePanel);


    /**
     * Returns the message queue containing Strings.
     *
     * @return the message queue containing Strings
     */
    ArrayDeque<String> getMessageQueue();

    /**
     * Returns the HashMap containing the emoji image icons.
     *
     * @return the HashMap containing the emoji image icons
     */
    HashMap<String, ImageIcon> getEmojiList();

    /**
     * Returns the HashMap containing the chat client properties.
     *
     * @return the HashMap containing the chat client properties
     */
    HashMap<String, CustomUserProperties> getChatClientPropertiesHashMap();
}
