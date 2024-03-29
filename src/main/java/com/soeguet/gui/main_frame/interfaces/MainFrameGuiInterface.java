package com.soeguet.gui.main_frame.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.behaviour.interfaces.GuiFunctionalityInterface;
import com.soeguet.gui.comments.interfaces.CommentInterface;
import com.soeguet.gui.notification_panel.NotificationImpl;
import com.soeguet.properties.dto.CustomUserPropertiesDTO;
import com.soeguet.socket_client.CustomWebsocketClient;
import java.awt.TrayIcon;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**
 * This interface defines the main GUI elements and functionality that can be accessed and
 * manipulated by other classes.
 */
public interface MainFrameGuiInterface {

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
     * @return the JScrollPane representing the scrollable area for the main text panel, on which
     *     chat bubbles are displayed
     */
    JScrollPane getMainTextBackgroundScrollPane();

    /**
     * Returns the ObjectMapper instance used for JSON serialization and deserialization.
     *
     * @return the ObjectMapper instance used for JSON serialization and deserialization
     */
    ObjectMapper getObjectMapper();

    /**
     * Returns the GuiFunctionality object that represents the current state and functionality of
     * the GUI.
     *
     * @return the GuiFunctionality object representing the GUI state and functionality
     */
    GuiFunctionalityInterface getGuiFunctionality();

    /**
     * Returns the name of the last message sender.
     *
     * @return the name of the last message sender as a String
     */
    String getLastMessageSenderName();

    /**
     * Sets the sender name of the last message.
     *
     * @param lastMessageSenderName the sender name of the last message as a String
     */
    void setLastMessageSenderName(String lastMessageSenderName);

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
     * Returns the layered pane that contains the main text panel.
     *
     * @return the layered pane that contains the main text panel
     */
    JLayeredPane getMainTextPanelLayeredPane();

    /**
     * Returns the timeAndUsername associated with the current user.
     *
     * @return the timeAndUsername as a String
     */
    String getUsername();

    /**
     * Sets the timeAndUsername for the current user.
     *
     * @param username the new timeAndUsername to be set
     */
    void setUsername(String username);

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
    HashMap<String, CustomUserPropertiesDTO> getChatClientPropertiesHashMap();

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
     * Returns a LinkedHashMap of comments.
     *
     * @return a LinkedHashMap with Long as keys and CommentInterface objects as values representing
     *     the comments
     */
    LinkedHashMap<Long, CommentInterface> getCommentsHashMap();

    /**
     * Resets the connection when the reset connection menu item is pressed.
     *
     * @param e the MouseEvent object representing the mouse press event
     */
    void resetConnectionMenuItemMousePressed(MouseEvent e);

    /**
     * Returns the JLabel used for displaying typing animation.
     *
     * @return the JLabel for typing animation
     */
    JLabel getTypingLabel();

    boolean isStartUp();

    /**
     * Sets the start up flag.
     *
     * @param b the boolean value to set the start up flag
     */
    void setStartUp(boolean b);

    JCheckBoxMenuItem getAllNotificationMenuItem();

    JCheckBoxMenuItem getInternalNotificationsMenuItem();

    JCheckBoxMenuItem getExternalNotificationsMenuItem();

    String getOSName();

    void revalidate();

    void repaint();

    TrayIcon getTrayIcon();
}
