package com.soeguet.gui.main_frame;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.behaviour.GuiFunctionality;
import com.soeguet.gui.newcomment.helper.CommentInterface;
import com.soeguet.gui.notification_panel.NotificationImpl;
import com.soeguet.properties.CustomProperties;
import com.soeguet.properties.CustomUserProperties;
import com.soeguet.socket_client.CustomWebsocketClient;
import com.soeguet.util.NotificationStatus;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 This interface defines the main GUI elements and functionality that can be accessed and manipulated by other classes.
 */
public interface MainFrameInterface {

    /**
     Retrieves the JTextPane component of the text editor.

     @return the JTextPane component of the text editor
     */
    JTextPane getTextEditorPane();

    /**
     Retrieves an instance of the CustomWebsocketClient.

     @return The CustomWebsocketClient instance.
     */
    CustomWebsocketClient getWebsocketClient();

    /**
     Returns the main text panel.

     @return the JPanel representing the main text panel, on which chat bubbles are displayed
     */
    JPanel getMainTextPanel();

    /**
     Returns the JScrollPane that contains the main text panel.

     @return the JScrollPane representing the scrollable area for the main text panel,
     on which chat bubbles are displayed
     */
    JScrollPane getMainTextBackgroundScrollPane();

    /**
     Returns the ObjectMapper instance used for JSON serialization and deserialization.

     @return the ObjectMapper instance used for JSON serialization and deserialization
     */
    ObjectMapper getObjectMapper();

    /**
     Returns the GuiFunctionality object that represents the current state and functionality of the GUI.

     @return the GuiFunctionality object representing the GUI state and functionality
     */
    GuiFunctionality getGuiFunctionality();

    /**
     Returns the name of the last message sender.

     @return the name of the last message sender as a String
     */
    String getLastMessageSenderName();

    /**
     Sets the sender name of the last message.

     @param lastMessageSenderName the sender name of the last message as a String
     */
    void setLastMessageSenderName(String lastMessageSenderName);

    /**
     Returns the time stamp of the last message.

     @return the time stamp of the last message as a String
     */
    String getLastMessageTimeStamp();

    /**
     Sets the time stamp of the last message.

     @param lastMessageTimeStamp the time stamp of the last message as a String
     */
    void setLastMessageTimeStamp(String lastMessageTimeStamp);

    /**
     Returns the layered pane that contains the main text panel.

     @return the layered pane that contains the main text panel
     */
    JLayeredPane getMainTextPanelLayeredPane();

    /**
     Returns the username associated with the current user.

     @return the username as a String
     */
    String getUsername();

    /**
     Sets the username for the current user.

     @param username the new username to be set
     */
    void setUsername(String username);

    /**
     Returns the HashMap containing the emoji image icons.

     @return the HashMap containing the emoji image icons
     */
    HashMap<String, ImageIcon> getEmojiList();

    /**
     Returns the HashMap containing the chat client properties.

     @return the HashMap containing the chat client properties
     */
    HashMap<String, CustomUserProperties> getChatClientPropertiesHashMap();

    /**
     Returns the custom properties of the chat client.

     @return the custom properties of the chat client
     */
    CustomProperties getCustomProperties();

    /**
     * Returns the Y position of the notification.
     *
     * @return the Y position of the notification
     */
    int getNotificationPositionY();

    /**
     * Sets the Y position of the notification.
     *
     * @param notificationPositionY the new Y position of the notification
     */
    void setNotificationPositionY(int notificationPositionY);

    /**
     * Retrieves the list of notification objects.
     *
     * @return the list of NotificationImpl objects
     */
    List<NotificationImpl> getNotificationList();

    /**
     * Sets the start up flag.
     *
     * @param b the boolean value to set the start up flag
     */
    void setStartUp(boolean b);

    /**
     * Returns the notification status.
     *
     * @return the current notification status
     */
    NotificationStatus getNotificationStatus();

    /**
     * Returns a LinkedHashMap of comments.
     *
     * @return a LinkedHashMap with Long as keys and CommentInterface objects as values representing the comments
     */
    LinkedHashMap<Long, CommentInterface> getCommentsHashMap();

    /**
     * Resets the connection when the reset connection menu item is pressed.
     *
     * @param e the MouseEvent object representing the mouse press event
     */
    void resetConnectionMenuItemMousePressed(MouseEvent e);

}