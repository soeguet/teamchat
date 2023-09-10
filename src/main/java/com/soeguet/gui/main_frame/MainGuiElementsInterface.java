package com.soeguet.gui.main_frame;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.behaviour.GuiFunctionality;
import com.soeguet.socket_client.CustomWebsocketClient;

import javax.swing.*;
import java.util.List;

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

    JLayeredPane getMainTextPanelLayeredPane();


}
