package com.soeguet.gui.main_frame;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.behaviour.GuiFunctionalityImpl;
import com.soeguet.behaviour.interfaces.GuiFunctionality;
import com.soeguet.cache.factory.CacheManagerFactory;
import com.soeguet.cache.implementations.WaitingNotificationQueue;
import com.soeguet.cache.manager.CacheManager;
import com.soeguet.emoji.EmojiHandler;
import com.soeguet.emoji.EmojiInitializer;
import com.soeguet.emoji.EmojiPopUpMenuHandler;
import com.soeguet.emoji.interfaces.EmojiInitializerInterface;
import com.soeguet.emoji.interfaces.EmojiPopupInterface;
import com.soeguet.gui.comments.CommentManagerImpl;
import com.soeguet.gui.comments.interfaces.CommentInterface;
import com.soeguet.gui.comments.interfaces.CommentManager;
import com.soeguet.gui.image_panel.ImagePanelImpl;
import com.soeguet.gui.image_panel.interfaces.ImageInterface;
import com.soeguet.gui.main_frame.generated.ChatPanel;
import com.soeguet.gui.main_frame.interfaces.MainFrameInterface;
import com.soeguet.gui.notification_panel.NotificationImpl;
import com.soeguet.gui.popups.PopupPanelImpl;
import com.soeguet.gui.popups.interfaces.PopupInterface;
import com.soeguet.gui.properties.PropertiesPanelImpl;
import com.soeguet.gui.properties.interfaces.PropertiesInterface;
import com.soeguet.model.EnvVariables;
import com.soeguet.properties.CustomProperties;
import com.soeguet.properties.CustomUserProperties;
import com.soeguet.socket_client.ClientControllerImpl;
import com.soeguet.socket_client.CustomWebsocketClient;
import com.soeguet.socket_client.interfaces.ClientController;
import com.soeguet.util.NotificationStatus;

import javax.swing.Timer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.*;
import java.util.logging.Logger;

/**
 Main GUI method
 */
public class ChatMainFrameImpl extends ChatPanel implements MainFrameInterface {

    //TODO these need to be re-evaluated and maybe moved into the cache manager

    //TODO maybe move to cache manager
    private final HashMap<String, CustomUserProperties> chatClientPropertiesHashMap = new HashMap<>();

    //TODO cache comments on pane for hot replacements as HashSet -> data structure ready, implementation missing -> add to cache
    private final LinkedHashMap<Long, CommentInterface> commentsHashMap = new LinkedHashMap<>();

    //TODO add to cache?
    private final List<NotificationImpl> notificationList = new ArrayList<>();

    /////////////////////////
    private final Logger logger = Logger.getLogger(ChatMainFrameImpl.class.getName());
    /**
     Instance of the object mapper used to convert objects to json and vice versa.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();
    /**
     Instance of cache manager primarily storing data structures of the collections api to help cache some data.
     */
    private final CacheManager cacheManager = CacheManagerFactory.getCacheManager();
    /**
     Instance of class, holding a few environment variables
     */
    private final EnvVariables envVariables;
    /**
     Instance of client controller handling everything socket related.
     */
    private ClientController clientController;
    /**
     Instance of custom properties handling everything properties related.
     */
    private CustomProperties customProperties;
    /**
     Instance of the gui functionality handler.
     */
    private GuiFunctionality guiFunctionality;
    /**
     The margin east border for the JScrollPane.
     */
    private int JSCROLLPANE_MARGIN_RIGHT_BORDER;
    /**
     The margin bottom border for the JScrollPane.
     */
    private int JSCROLLPANE_MARGIN_BOTTOM_BORDER;
    /**
     Hashmap of all available emojis.
     */
    private HashMap<String, ImageIcon> emojiList;
    /**
     Instance of the emoji handler, which switches emojis with strings and vice versa.
     */
    private EmojiHandler emojiHandler;
    /**
     Variable representing the username of this pc's client.
     The username on the right side
     */
    private String username;
    /**
     Represents the Y position of a notification.
     Will be updated everytime a notification is generated
     */
    private volatile int notificationPositionY = 0;
    /**
     Indicates whether the start-up process has already been completed.
     */
    private boolean startUp = true;
    /**
     The name of the client which was last posted on the main panel.
     */
    private String lastMessageSenderName;
    /**
     The last time someone posted on the main panel.
     */
    private String lastMessageTimeStamp;
    /**
     Timer for blocking all messages.
     */
    private Timer blockTimer;

    /**
     Constructor for ChatMainFrameImpl. It initializes the main chat frame and sets up various components and functionalities.

     @param envVariables The environment variables to be used in the chat frame.
     */
    public ChatMainFrameImpl(final EnvVariables envVariables) {

        this.envVariables = envVariables;
    }

    public ChatMainFrameImpl() {
        //for testing
        envVariables = new EnvVariables();
    }

    public void initializeClientController() {

        clientController = new ClientControllerImpl(this, guiFunctionality);
        clientController.determineWebsocketURI();
        clientController.connectToWebsocket();
    }

    public ClientController getClientController() {

        return clientController;
    }

    /**
     Retrieves the value of the JSCROLLPANE_MARGIN_RIGHT_BORDER constant.

     @return The value of the JSCROLLPANE_MARGIN_RIGHT_BORDER constant.
     */
    public int getJSCROLLPANE_MARGIN_RIGHT_BORDER() {

        return JSCROLLPANE_MARGIN_RIGHT_BORDER;
    }

    /**
     Retrieves the value of the JSCROLLPANE_MARGIN_BOTTOM_BORDER constant.

     @return The value of the JSCROLLPANE_MARGIN_BOTTOM_BORDER constant.
     */
    public int getJSCROLLPANE_MARGIN_BOTTOM_BORDER() {

        return JSCROLLPANE_MARGIN_BOTTOM_BORDER;
    }

    /**
     Loads the username from the environment variables and assigns it to the appropriate field.
     */
    public void loadUsernameFromEnvVariables() {

        //override username if saved in GUI by user
        if (!envVariables.getChatUsername().isEmpty()) {

            this.username = envVariables.getChatUsername();
        }
    }

    /**
     This method is used to load custom properties for a specific client.
     It initializes the customProperties object with a new instance of CustomProperties,
     passing the current instance as a parameter.
     It then calls the loaderThisClientProperties() method of the customProperties object
     to load the properties for the current client.
     If the client properties are successfully loaded, the method sets the username property
     to the username obtained from the CustomUserProperties object.
     */
    public void loadCustomProperties() {

        this.customProperties = new CustomProperties(this);

        CustomUserProperties client = this.customProperties.loaderThisClientProperties();

        //only override username if nothing is set on start up
        if (client != null && username == null) {

            this.username = client.getUsername();
        }
    }

    /**
     This method is used to reposition the chat frame for testing purposes.
     It retrieves the value of the 'chat.x.position' environment variable using System.getenv(),
     which represents the desired x position of the chat frame.
     If the environment variable is not null, it repositions the chat frame on the screen
     by setting the location using the retrieved x position and a fixed y position of 100.
     This operation is performed asynchronously using SwingUtilities.invokeLater()
     to ensure compatibility with Swing's event dispatching thread.
     */
    public void repositionChatFrameForTestingPurposes() {

        final String chatXPosition = System.getenv("CHAT_X_POSITION");

        if (chatXPosition != null) {

            SwingUtilities.invokeLater(() -> {

                final int chatFrameXPosition = Integer.parseInt(chatXPosition);
                setLocation(chatFrameXPosition, 100);
            });
        }
    }

    /**
     Initializes the GUI functionality.

     This method creates a new instance of the GuiFunctionality class, passing
     a reference to the current object as a constructor argument. The GuiFunctionality
     object is then assigned to the guiFunctionality instance variable of the current object.
     */
    public void initGuiFunctionality() {

        CommentManager commentManager = new CommentManagerImpl(this);

        this.guiFunctionality = new GuiFunctionalityImpl(this, commentManager);
        this.guiFunctionality.fixScrollPaneScrollSpeed();
        this.guiFunctionality.overrideTransferHandlerOfTextPane();
    }

    /**
     Initializes the EmojiHandler and EmojiList.

     This method creates a new instance of the EmojiHandler class, passing
     a reference to the current object as a constructor argument. The EmojiHandler
     object is then assigned to the emojiHandler instance variable of the current object.

     This method also creates a new instance of the EmojiInitializer class and uses
     it to create a list of emojis. The created emoji list is then assigned to the
     emojiList instance variable of the current object.
     */
    public void initEmojiHandlerAndList() {

        this.emojiHandler = new EmojiHandler(this);

        EmojiInitializerInterface emojiInitializer = new EmojiInitializer();
        this.emojiList = emojiInitializer.createEmojiList();
    }

    /**
     Sets the scroll pane margins.

     This method determines the appropriate margin values for the scroll pane based on the operating system and desktop environment.
     */
    public void setScrollPaneMargins() {

        if (getOSName().toLowerCase().contains("windows")) {

            JSCROLLPANE_MARGIN_BOTTOM_BORDER = 63;
            JSCROLLPANE_MARGIN_RIGHT_BORDER = 20;

        } else {

            if (getDesktopEnv().toLowerCase().contains("gnome")) {

                JSCROLLPANE_MARGIN_BOTTOM_BORDER = 27;

            } else {

                //e.g. KDE Plasma
                JSCROLLPANE_MARGIN_BOTTOM_BORDER = 56;
            }

            JSCROLLPANE_MARGIN_RIGHT_BORDER = 4;
        }
    }

    public String getOSName() {

        return System.getProperty("os.name");
    }

    public String getDesktopEnv() {

        return System.getenv("XDG_CURRENT_DESKTOP");
    }

    /**
     Method called when the component is resized.

     @param e The ComponentEvent object representing the resize event.
     */
    @Override
    protected void thisComponentResized(ComponentEvent e) {

        final int rightBorderMargin = e.getComponent().getWidth() - JSCROLLPANE_MARGIN_RIGHT_BORDER;
        final int bottomBorderMargin = e.getComponent().getHeight() - form_interactionAreaPanel.getHeight() - JSCROLLPANE_MARGIN_BOTTOM_BORDER;

        this.form_mainTextBackgroundScrollPane.setBounds(1, 1, rightBorderMargin, bottomBorderMargin);

        repaintMainFrame();
    }

    /**
     Method used to repaint the main frame.

     This method revalidates and repaints the main frame component, ensuring that any changes to its layout or appearance are correctly displayed on the screen.
     */
    private void repaintMainFrame() {

        String version = retrieveJarVersion();
        this.setTitle("teamchat - " + version + " - username: " + this.getUsername());

        this.revalidate();
        this.repaint();
    }

    /**

     */
    private String retrieveJarVersion() {

        Properties properties = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("version.properties");

        if (inputStream != null) {
            try {
                properties.load(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return properties.getProperty("version");
        }

        return "v.?";
    }

    /**
     Retrieves the username.

     @return the username as a String.
     */
    @Override
    public String getUsername() {

        return username;
    }

    /**
     Sets the username.

     @param username the username to set.
     */
    @Override
    public void setUsername(String username) {

        this.username = username;
    }

    /**
     Returns a HashMap containing the list of emojis and their corresponding image icons.

     <p>This method returns the emojiList HashMap, which contains the emojis and their corresponding image icons.
     The key in the HashMap represents the emoji text, and the value represents the corresponding image icon.

     @return the HashMap containing the list of emojis and their corresponding image icons
     */
    @Override
    public HashMap<String, ImageIcon> getEmojiList() {

        return emojiList;
    }

    /**
     Returns a HashMap containing the chat client properties.

     <p>This method returns the chatClientPropertiesHashMap, which contains the chat client properties.
     The key in the HashMap represents the property name, and the value represents the corresponding value.

     @return the HashMap containing the chat client properties
     */
    @Override
    public HashMap<String, CustomUserProperties> getChatClientPropertiesHashMap() {

        return chatClientPropertiesHashMap;
    }

    /**
     Returns the custom properties object.

     @return the custom properties object.
     */
    @Override
    public CustomProperties getCustomProperties() {

        return customProperties;
    }

    /**
     Returns the Y position of the notification.

     @return the Y position of the notification.
     */
    @Override
    public synchronized int getNotificationPositionY() {

        return this.notificationPositionY;
    }

    /**
     Sets the Y position of the notification.

     @param notificationPositionY the Y position of the notification.
     */
    @Override
    public synchronized void setNotificationPositionY(int notificationPositionY) {

        this.notificationPositionY = notificationPositionY;
    }

    /**
     Gets the list of notifications.

     @return the list of notifications.
     */
    @Override
    public List<NotificationImpl> getNotificationList() {

        return notificationList;
    }

    /**
     Sets the startUp flag to indicate whether the system is starting up.

     @param startUp the startUp flag
     */
    @Override
    public void setStartUp(final boolean startUp) {

        this.startUp = startUp;
    }

    /**
     Retrieves the status of notifications based on the current settings.

     @return the notification status
     */
    public NotificationStatus getNotificationStatus() {

        //on program startup
        if (startUp) {

            return NotificationStatus.ALL_DENIED;
        }

        //all = no -- needs to be first since it will reset after 5 minutes
        if (getAllNotificationsMenuItem().isSelected()) {

            return NotificationStatus.ALL_DENIED;
        }

        //external = yes && internal = yes
        if (getInternalNotificationsMenuItem().isSelected() && getExternalNotificationsMenuItem().isSelected()) {

            return NotificationStatus.ALL_ALLOWED;
        }

        //external = yes && internal = no
        if (getExternalNotificationsMenuItem().isSelected() && !getInternalNotificationsMenuItem().isSelected()) {

            return NotificationStatus.EXTERNAL_ONLY;
        }

        //external = no && internal = yes
        if (!getExternalNotificationsMenuItem().isSelected() && getInternalNotificationsMenuItem().isSelected()) {

            return NotificationStatus.INTERNAL_ONLY;
        }

        return NotificationStatus.ALL_DENIED;
    }

    /**
     Retrieves the comment hash map.

     @return the comments hash map
     */
    public LinkedHashMap<Long, CommentInterface> getCommentsHashMap() {

        return commentsHashMap;
    }

    /**
     Handles the event when the mouse clicks the current JFrame.

     @param e the MouseEvent object that triggered this event
     */
    @Override
    protected void thisMouseClicked(MouseEvent e) {

    }

    /**
     Called when the mouse is pressed on the property menu item.

     @param e The MouseEvent object representing the event.
     */
    @Override
    protected void propertiesMenuItemMousePressed(MouseEvent e) {

        PropertiesInterface properties = new PropertiesPanelImpl(this);

        properties.setPosition();
        properties.setupOwnTabbedPane();
        properties.setupClientsTabbedPane();

        properties.setVisible(true);
    }

    /**
     Called when the state of internal notifications menu item is changed.

     @param e The ItemEvent object representing the event.
     */
    @Override
    protected void internalNotificationsMenuItemItemStateChanged(final ItemEvent e) {

        WaitingNotificationQueue waitingNotificationQueue = (WaitingNotificationQueue) cacheManager.getCache("waitingNotificationQueue");

        PopupInterface popup = new PopupPanelImpl(this);

        //remove all remaining and queued notifications
        if (e.getStateChange() == ItemEvent.DESELECTED) {

            //getter call since this one is synchronized
            waitingNotificationQueue.removeAll();

            popup.getMessageTextField().setText("Internal notifications disabled");

        } else {

            popup.getMessageTextField().setText("Internal notifications enabled");
        }

        popup.configurePopupPanelPlacement();
        popup.initiatePopupTimer(2_000);
    }

    /**
     Called when the connection details button is pressed.

     @param e The MouseEvent object representing the event.
     */
    @Override
    protected void connectionDetailsButtonMousePressed(final MouseEvent e) {
        //TODO is this needed?
        clientController.serverInformationOptionPane();
    }

    /**
     Resets the connection when the reset connection menu item is pressed.

     @param e The mouse event that triggered the method.
     */
    @Override
    public void resetConnectionMenuItemMousePressed(MouseEvent e) {

        //clear the main text panel first
        removeAllMessagesOnChatPanel();

        //close the websocket client
        clientController.closeConnection();

        //set null to be sure + preparation for reconnect
        clientController.prepareReconnection();

        // invalidate all caches
        cacheManager.invalidateCache();

        //reconnect to socket
        clientController.connectToWebsocket();
        this.logger.info("Reconnecting websocket client");
    }

    /**
     Removes all messages from the chat panel.

     The method uses `SwingUtilities.invokeLater()` to ensure that the removal of messages
     is performed on the event dispatch thread, as it involves modifications to the GUI.

     Within the method, the `form_mainTextPanel.removeAll()` is called to remove all
     components from the chat panel. Additionally, the `repaintMainFrame()` method is
     called to repaint the main frame, ensuring that the changes are immediately visible
     to the user.
     */
    private void removeAllMessagesOnChatPanel() {

        SwingUtilities.invokeLater(() -> {

            this.form_mainTextPanel.removeAll();
            repaintMainFrame();
        });
    }

    /**
     Handles the event when the mouse presses the exit menu item. Sets the default close operation
     for the current JFrame to EXIT_ON_CLOSE and disposes of the current JFrame.

     @param e the MouseEvent object that triggered this event
     */
    @Override
    protected void exitMenuItemMousePressed(MouseEvent e) {

        SwingUtilities.invokeLater(() -> {

            //make the window close on exit just to be sure
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.dispose();

            System.exit(0);
        });
    }

    /**
     Called when the participant menu item is pressed. Logs the provided event and method name.

     @param e The MouseEvent associated with the button press.
     */
    @Override
    protected void participantsMenuItemMousePressed(MouseEvent e) {

    }

    /**
     Called when the main text panel is clicked. Logs the provided event and method name.

     @param e The MouseEvent associated with the click event.
     */
    @Override
    protected void mainTextPanelMouseClicked(MouseEvent e) {

    }

    /**
     {@inheritDoc}

     <p>This method is called when the mouse is clicked on the text editor pane in the ChatGuiImpl
     class. It logs the method call with the provided MouseEvent object and the method name.

     @param e the MouseEvent object representing the mouse click event on the text editor pane
     */
    @Override
    protected void textEditorPaneMouseClicked(MouseEvent e) {

    }

    /**
     Called when a key is pressed in the text editor pane. If the pressed key is not the Enter key,
     the method simply returns. If the pressed key is the Enter key, it consumes the event and
     performs the appropriate action based on whether the Shift key is pressed or not.

     @param e The KeyEvent object representing the key press event.
     */
    @Override
    protected void textEditorPaneKeyPressed(KeyEvent e) {

        //typing.. status
        if (e.getKeyCode() != KeyEvent.VK_ENTER) {

            sendIsTypingStatusToWebsocket();
            return;
        }

        //shift + enter -> new line
        if (e.isShiftDown()) {

            appendNewLineToTextEditorPane();
            return;
        }

        e.consume();
        handleNonShiftEnterKeyPress();
    }

    /**
     Sends a typing status message to the websocket server.
     The message is in JSON format and contains the type "typing" and the username of the user.
     */
    private void sendIsTypingStatusToWebsocket() {

        String typingStatus = "{\"type\":\"typing\",\"username\":\"" + this.getUsername() + "\"}";
        clientController.getWebsocketClient().send(typingStatus.getBytes());
    }

    /**
     Appends a new line to the text editor pane.

     <p>Retrieves the current text in the text editor pane and appends a new line character at the
     end of it.
     */
    private void appendNewLineToTextEditorPane() {

        form_textEditorPane.setText(form_textEditorPane.getText() + "\n");
    }

    /**
     Handles a key press event when the enter key is pressed without pressing the shift key.

     <p>Retrieves the content of the text editor pane, trims any leading or trailing space, and
     checks if it is empty.
     If the content is empty, it clears the text editor pane.
     Otherwise, it calls the `clearTextPaneAndSendMessageToSocket` method to clear the text pane and send the
     current content to a socket.
     */
    private void handleNonShiftEnterKeyPress() {

        String textPaneContent = form_textEditorPane.getText().trim();

        if (textPaneContent.isEmpty()) {

            form_textEditorPane.setText("");

        } else {

            guiFunctionality.clearTextPaneAndSendMessageToSocket();
        }
    }

    /**
     Handles the event when the mouse clicks the picture button in the current JFrame.

     @param e the MouseEvent object that triggered this event
     */
    @Override
    protected void pictureButtonMouseClicked(MouseEvent e) {

        ImageInterface imagePanel = new ImagePanelImpl(this);
        imagePanel.setPosition();
        imagePanel.setLayeredPaneLayerPositions();
        imagePanel.setupPictureScrollPaneScrollSpeed();
        imagePanel.populateImagePanelFromClipboard();
    }

    /**
     Invoked when the emoji button is clicked in the chat GUI.

     <p>This method is an override of the emojiButton method from the superclass. It is called when
     the emoji button is clicked.

     @param e the ActionEvent object generated when the emoji button is clicked
     */
    @Override
    protected void emojiButton(ActionEvent e) {

        EmojiPopupInterface emojiPopup = new EmojiPopUpMenuHandler(this, form_textEditorPane, form_emojiButton);
        emojiPopup.createEmojiPopupMenu();
    }

    /**
     Handles the event when the send-button is clicked.
     Clears the text pane in the GUI and sends
     the message to the socket.

     @param e the ActionEvent object that triggered this event
     */
    @Override
    protected void sendButton(ActionEvent e) {

        emojiHandler.replaceImageIconWithEmojiDescription(getTextEditorPane());

        if (form_textEditorPane.getText().trim().isEmpty()) {

            return;
        }

        guiFunctionality.clearTextPaneAndSendMessageToSocket();
    }

    /**
     Handles the item state change event of the external notifications menu item.
     Updates the state of the external notifications and displays a popup message accordingly.

     @param e the ItemEvent object that triggered this event
     */
    @Override
    protected void externalNotificationsMenuItemItemStateChanged(final ItemEvent e) {

        PopupInterface popup = new PopupPanelImpl(this);

        if (e.getStateChange() == ItemEvent.SELECTED) {

            popup.getMessageTextField().setText("External notifications enabled!");

        } else if (e.getStateChange() == ItemEvent.DESELECTED) {

            popup.getMessageTextField().setText("External notifications disabled!");
        }

        popup.configurePopupPanelPlacement();
        popup.initiatePopupTimer(2_000);
    }

    /**
     Handles the event when the window is closed.

     @param e the WindowEvent object that triggered this event
     */
    @Override
    protected void thisWindowClosing(final WindowEvent e) {

        setState(Frame.ICONIFIED);
    }

    /**
     Handles the event when the state of the allNotificationsMenuItem changes.

     @param e the ItemEvent object that triggered this event
     */
    @Override
    protected void allNotificationsMenuItemItemStateChanged(final ItemEvent e) {

        WaitingNotificationQueue waitingNotificationQueue = (WaitingNotificationQueue) cacheManager.getCache("waitingNotificationQueue");

        //any kind of change needs to get rid of an existing timer
        if (blockTimer != null) {

            blockTimer.stop();
            blockTimer = null;
        }

        //block all notifications for 5 minutes
        if (e.getStateChange() == ItemEvent.SELECTED) {

            //getter call since this one is synchronized
            waitingNotificationQueue.removeAll();

            blockTimer = new Timer(1_000 * 60 * 5, e1 -> {

                form_allNotificationsMenuItem.setSelected(false);

                PopupInterface popup = new PopupPanelImpl(this);
                popup.getMessageTextField().setText("Notifications status" + System.lineSeparator() + "reverted");
                popup.configurePopupPanelPlacement();
                popup.initiatePopupTimer(3_000);
            });

            blockTimer.setRepeats(false);
            blockTimer.start();

            PopupInterface popup = new PopupPanelImpl(this);
            popup.getMessageTextField().setText("All notifications disabled" + System.lineSeparator() + "for 5 minutes");
            popup.configurePopupPanelPlacement();
            popup.initiatePopupTimer(3_000);
        }

    }

    /**
     Returns the websocket client associated with this instance.

     @return the websocket client
     */
    @Override
    public CustomWebsocketClient getWebsocketClient() {

        return clientController.getWebsocketClient();
    }

    /**
     Gets the ObjectMapper instance used for converting JSON to Java objects and vice versa.

     @return the ObjectMapper instance
     */
    @Override
    public ObjectMapper getObjectMapper() {

        return objectMapper;
    }

    /**
     Retrieves the GuiFunctionality.

     @return the GuiFunctionality object.
     */
    @Override
    public GuiFunctionality getGuiFunctionality() {

        return guiFunctionality;
    }

    /**
     Retrieves the name of the last message.

     @return the name of the last message.
     */
    @Override
    public String getLastMessageSenderName() {

        return lastMessageSenderName;
    }

    /**
     Sets the name of the last message.

     @param lastMessageSenderName the name of the last message.
     */
    @Override
    public void setLastMessageSenderName(final String lastMessageSenderName) {

        this.lastMessageSenderName = lastMessageSenderName;
    }

    /**
     Retrieves the timestamp of the last message.

     @return the timestamp of the last message.
     */
    @Override
    public String getLastMessageTimeStamp() {

        return lastMessageTimeStamp;
    }

    /**
     Sets the timestamp of the last message.

     @param lastMessageTimeStamp the timestamp of the last message to be set.
     */
    @Override
    public void setLastMessageTimeStamp(final String lastMessageTimeStamp) {

        this.lastMessageTimeStamp = lastMessageTimeStamp;
    }

    public void setButtonIcons() {

        URL sendUrl = ChatMainFrameImpl.class.getResource("/emojis/$+1f4e8$+.png");
        URL emojiUrl = ChatMainFrameImpl.class.getResource("/emojis/$+1f60e$+.png");
        URL pictureUrl = ChatMainFrameImpl.class.getResource("/emojis/$+1f4bb$+.png");

        assert sendUrl != null;
        assert emojiUrl != null;
        assert pictureUrl != null;

        form_sendButton.setIcon(new ImageIcon(sendUrl));
        form_emojiButton.setIcon(new ImageIcon(emojiUrl));
        form_pictureButton.setIcon(new ImageIcon(pictureUrl));
    }


}