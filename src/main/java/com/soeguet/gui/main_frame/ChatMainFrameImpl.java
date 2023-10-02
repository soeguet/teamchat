package com.soeguet.gui.main_frame;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.behaviour.GuiFunctionality;
import com.soeguet.cache.factory.CacheManagerFactory;
import com.soeguet.cache.implementations.WaitingNotificationQueue;
import com.soeguet.cache.manager.CacheManager;
import com.soeguet.gui.image_panel.ImagePanelImpl;
import com.soeguet.gui.main_frame.generated.ChatPanel;
import com.soeguet.gui.newcomment.helper.CommentInterface;
import com.soeguet.gui.notification_panel.NotificationImpl;
import com.soeguet.gui.popups.PopupPanelImpl;
import com.soeguet.gui.properties.PropertiesPanelImpl;
import com.soeguet.model.EnvVariables;
import com.soeguet.properties.CustomProperties;
import com.soeguet.properties.CustomUserProperties;
import com.soeguet.socket_client.CustomWebsocketClient;
import com.soeguet.util.EmojiHandler;
import com.soeguet.util.EmojiInitializer;
import com.soeguet.util.EmojiPopUpMenuHandler;
import com.soeguet.util.NotificationStatus;
import net.miginfocom.swing.MigLayout;

import javax.swing.Timer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.*;
import java.util.logging.Logger;

/**
 Main GUI method
 */
public class ChatMainFrameImpl extends ChatPanel implements MainFrameInterface {

    private final Logger logger = Logger.getLogger(ChatMainFrameImpl.class.getName());
    private final HashMap<String, CustomUserProperties> chatClientPropertiesHashMap;

    //TODO cache comments on pane for hot replacements as HashSet -> data structure ready, implementation missing -> add to cache
    private final LinkedHashMap<Long, CommentInterface> commentsHashMap = new LinkedHashMap<>();
    private final ObjectMapper objectMapper;
    //TODO add to cache?
    private final List<NotificationImpl> notificationList = new ArrayList<>();
    private final CacheManager cacheManager = CacheManagerFactory.getCacheManager();
    private CustomProperties customProperties;
    private EnvVariables envVariables;
    private GuiFunctionality guiFunctionality;
    private URI serverUri;
    private int JSCROLLPANE_MARGIN_RIGHT_BORDER;
    private int JSCROLLPANE_MARGIN_BOTTOM_BORDER;
    private HashMap<String, ImageIcon> emojiList;
    private EmojiHandler emojiHandler;
    private CustomWebsocketClient websocketClient;
    private String username;
    private volatile int notificationPositionY = 0;
    private boolean startUp = true;
    private String lastMessageSenderName;
    private String lastMessageTimeStamp;
    private boolean blockAllNotifications = form_allNotificationsMenuItem.isSelected();
    private boolean blockInternalNotifications = !form_internalNotificationsMenuItem.isSelected();
    private boolean blockExternalNotifications = !form_externalNotificationsMenuItem.isSelected();
    private Timer blockTimer;

    /**
     Constructor for ChatMainFrameImpl. It initializes the main chat frame and sets up various components and functionalities.

     @param envVariables The environment variables to be used in the chat frame.
     */
    public ChatMainFrameImpl(final EnvVariables envVariables) {

        //TODO maybe move to cache manager
        this.chatClientPropertiesHashMap = new HashMap<>();
        this.objectMapper = new ObjectMapper();

        loadEnvVariables(envVariables);
        loadCustomProperties();

        //TODO remove for merge in master
        repositionChatFrameForTestingPurposes();

        //setup functionality
        initGuiFunctionality();
        initEmojiHandlerAndList();

        //operating system specific settings
        setScrollPaneMargins();

        //setup GUI
        setTitle("teamchat");
        setVisible(true);

        //setup websocket client
        initWebSocketClient();
    }

    /**
     Loads environment variables and assigns them to the appropriate fields.

     @param envVariables The object containing the environment variables.
     */
    private void loadEnvVariables(final EnvVariables envVariables) {

        this.envVariables = envVariables;

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
    private void loadCustomProperties() {

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
    private void repositionChatFrameForTestingPurposes() {

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
    private void initGuiFunctionality() {

        this.guiFunctionality = new GuiFunctionality(this);
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
    private void initEmojiHandlerAndList() {

        emojiHandler = new EmojiHandler(this);
        emojiList = new EmojiInitializer().createEmojiList();
    }

    /**
     Sets the scroll pane margins.

     This method determines the appropriate margin values for the scroll pane based on the operating system and desktop environment.
     */
    private void setScrollPaneMargins() {

        if (System.getProperty("os.name").toLowerCase().contains("windows")) {

            JSCROLLPANE_MARGIN_BOTTOM_BORDER = 63;
            JSCROLLPANE_MARGIN_RIGHT_BORDER = 20;

        } else {

            if (System.getenv("XDG_CURRENT_DESKTOP").toLowerCase().contains("gnome")) {

                JSCROLLPANE_MARGIN_BOTTOM_BORDER = 27;

            } else {

                //e.g. KDE Plasma
                JSCROLLPANE_MARGIN_BOTTOM_BORDER = 56;
            }

            JSCROLLPANE_MARGIN_RIGHT_BORDER = 4;
        }
    }

    /**
     Initializes the WebSocket client.

     This method retrieves the server IP and port from the environment variables and creates a URI for the WebSocket server.
     If the server IP or port is missing or empty, it opens a server information option pane.
     Otherwise, it initializes the server URI and connects to the WebSocket server.

     @throws RuntimeException if there is an error creating the server URI
     */
    private void initWebSocketClient() {

        final String serverIp = envVariables.getChatIp();
        final String serverPort = envVariables.getChatPort();

        if (serverIp.isEmpty() || serverPort.isEmpty()) {

            serverInformationOptionPane();

        } else {

            try {

                serverUri = new URI("ws://" + serverIp + ":" + serverPort);

            } catch (URISyntaxException e) {

                new PopupPanelImpl(this, "Error creating server URI: " + e.getMessage());
                logger.log(java.util.logging.Level.SEVERE, "Error creating server URI", e);
                throw new RuntimeException(e);
            }
        }

        //connect after 1 second
        setupConnectionTimer();
    }

    /**
     Displays a JOptionPane to gather server information from the user.
     The user is prompted to enter the server IP address and port number.
     If the user selects the OK option, the server IP address and port number
     are stored in the envVariables object and a URI object representing the
     server URI is created and stored in the serverUri variable.
     */
    private void serverInformationOptionPane() {

        JTextField serverIpTextField = new JTextField(7);
        JTextField serverPortTextField = new JTextField(7);

        final JPanel serverInfoPanel = createServerInfoPanel(serverIpTextField, serverPortTextField);

        int result = JOptionPane.showConfirmDialog(this, serverInfoPanel, "please enter ip and port values", JOptionPane.OK_CANCEL_OPTION);

        validateServerInformationInputByUser(serverIpTextField.getText(), serverPortTextField.getText());

        if (result == JOptionPane.OK_OPTION) {

            processValidatedServerInformation(serverIpTextField, serverPortTextField);
        }
    }

    /**
     Sets up a timer to connect to the WebSocket server after 1 second.

     It creates a timer that waits for 1 second and then executes the connection process.
     The connection process involves displaying a popup panel indicating the connection status,
     logging the connection attempt, and calling the connectToWebsocket() method to establish the WebSocket connection.
     */
    private void setupConnectionTimer() {

        Timer connectTimer = new Timer(1000, e -> {
            new PopupPanelImpl(this, "Connecting to server");
            logger.info("connecting websocket client");
            connectToWebsocket();
        });
        connectTimer.setRepeats(false);
        connectTimer.start();
    }

    /**
     Creates a server information panel.

     @param serverIpTextField   the text field for server IP
     @param serverPortTextField the text field for server port

     @return the created JPanel containing the server information panel
     */
    private JPanel createServerInfoPanel(final JTextField serverIpTextField, final JTextField serverPortTextField) {

        JPanel myPanel = new JPanel(new MigLayout("wrap 2"));

        //port information
        myPanel.add(new JLabel("Port:"));
        serverIpTextField.setText(envVariables.getChatIp().isBlank() ? "127.0.0.1" : envVariables.getChatIp());
        myPanel.add(serverIpTextField);

        //ip information
        myPanel.add(new JLabel("Ip:"));
        serverPortTextField.setText(envVariables.getChatPort().isBlank() ? "8100" : envVariables.getChatPort());
        myPanel.add(serverPortTextField);

        serverIpTextField.requestFocus();

        return myPanel;
    }

    /**
     Validates the server information input by the user.

     @param serverIpText   the server IP input text
     @param serverPortText the server port input text
     */
    private void validateServerInformationInputByUser(final String serverIpText, final String serverPortText) {

        if (serverIpText.isEmpty() || serverPortText.isEmpty()) {

            JOptionPane.showMessageDialog(this, "Server IP or port is empty", "Error", JOptionPane.ERROR_MESSAGE);
        }

        if (!serverIpText.matches("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$")) {

            JOptionPane.showMessageDialog(this, "Server IP is invalid", "Error", JOptionPane.ERROR_MESSAGE);
        }

        if (!serverPortText.matches("^[0-9]+$")) {

            JOptionPane.showMessageDialog(this, "Server port is invalid", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     Processes the validated server information provided by the user.
     The server IP address and port number are extracted from the text fields.
     The server IP address and port number are then stored in the envVariables object.
     Finally, a URI object representing the server URI is created using the server IP address and port number.

     @param serverIpTextField   the text field containing the server IP address
     @param serverPortTextField the text field containing the server port number
     */
    private void processValidatedServerInformation(final JTextField serverIpTextField, final JTextField serverPortTextField) {

        Optional<String> serverIp = Optional.of(serverIpTextField.getText());
        Optional<String> serverPort = Optional.of(serverPortTextField.getText());

        envVariables.setChatIp(serverIp.orElse("127.0.0.1"));
        envVariables.setChatPort(serverPort.orElse("8100"));

        try {

            serverUri = new URI("ws://" + envVariables.getChatIp() + ":" + envVariables.getChatPort());

        } catch (URISyntaxException e) {

            throw new RuntimeException(e);
        }
    }

    /**
     Establishes a connection to a WebSocket server.

     This method creates a new instance of a CustomWebsocketClient and connects to the specified WebSocket server. The connection process is asynchronously executed on a separate thread.
     */
    private void connectToWebsocket() {

        websocketClient = new CustomWebsocketClient(serverUri, this);
        websocketClient.connect();
    }

    /**
     {@inheritDoc}

     <p>This method is called when a property change event occurs. It logs the provided event and
     method name.

     @param e The property change event to be handled. Must not be null.
     */
    @Override
    protected void thisPropertyChange(PropertyChangeEvent e) {

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

        this.revalidate();
        this.repaint();
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

        new PropertiesPanelImpl(this);
    }

    /**
     Called when the state of internal notifications menu item is changed.

     @param e The ItemEvent object representing the event.
     */
    @Override
    protected void internalNotificationsMenuItemItemStateChanged(final ItemEvent e) {

        WaitingNotificationQueue waitingNotificationQueue = (WaitingNotificationQueue) cacheManager.getCache("waitingNotificationQueue");

        //remove all remaining and queued notifications
        if (e.getStateChange() == ItemEvent.DESELECTED) {

            //getter call since this one is synchronized
            waitingNotificationQueue.removeAll();
            blockInternalNotifications = true;

            new PopupPanelImpl(this, "Internal notifications disabled").implementPopup(2000);

        } else {

            blockInternalNotifications = false;

            new PopupPanelImpl(this, "Internal notifications enabled").implementPopup(2000);
        }
    }

    /**
     Called when the connection details button is pressed.

     @param e The MouseEvent object representing the event.
     */
    @Override
    protected void connectionDetailsButtonMousePressed(final MouseEvent e) {
        //TODO is this needed?
        this.serverInformationOptionPane();
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
        closeActiveConnectionToSocket();

        //set null to be sure + preparation for reconnect
        prepareReconnection();

        // invalidate all caches
        cacheManager.invalidateCache();

        //reconnect to socket
        this.connectToWebsocket();
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
     Closes the active connection to the socket.

     If the websocket client is open, the method closes the websocket client and logs an info message.
     */
    private void closeActiveConnectionToSocket() {

        if (this.websocketClient.isOpen()) {

            this.logger.info("Closing websocket client");
            this.websocketClient.close();
        }
    }

    /**
     Prepares the application for reconnection.

     Resets the websocket client to null, sets the last message sender name and timestamp to null,
     and sets the startUp flag to true to disable notifications during initial message flood.
     */
    private void prepareReconnection() {

        this.websocketClient = null;

        //new evaluation of last sender and time
        this.setLastMessageSenderName(null);
        this.setLastMessageTimeStamp(null);

        //no notifications during initial message flood
        this.startUp = true;
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

            //TODO typing.. status!
            String typingStatus = "{\"type\":\"typing\",\"username\":\"" + this.getUsername() + "\"}";
            websocketClient.send(typingStatus.getBytes());

            return;
        }

        e.consume();

        if (e.isShiftDown()) {

            appendNewLineToTextEditorPane();

            return;
        }

        handleNonShiftEnterKeyPress();
    }

    /**
     Invoked when a key is released in the text editor pane.

     <p>This method is an override of the textEditorPaneKeyReleased method from the superclass. It
     is called when a key is released in the text editor pane.

     @param e the KeyEvent object generated when a key is released
     */
    @Override
    protected void textEditorPaneKeyReleased(KeyEvent e) {

    }

    /**
     Handles the event when the mouse clicks the picture button in the current JFrame.

     @param e the MouseEvent object that triggered this event
     */
    @Override
    protected void pictureButtonMouseClicked(MouseEvent e) {

        new ImagePanelImpl(this);
    }

    /**
     Invoked when the emoji button is clicked in the chat GUI.

     <p>This method is an override of the emojiButton method from the superclass. It is called when
     the emoji button is clicked.

     @param e the ActionEvent object generated when the emoji button is clicked
     */
    @Override
    protected void emojiButton(ActionEvent e) {

        new EmojiPopUpMenuHandler(this, form_textEditorPane, form_emojiButton);
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

        if (e.getStateChange() == ItemEvent.SELECTED) {

            blockExternalNotifications = false;

            new PopupPanelImpl(this, "External notifications enabled").implementPopup(2000);

        } else if (e.getStateChange() == ItemEvent.DESELECTED) {

            blockExternalNotifications = true;

            new PopupPanelImpl(this, "External notifications disabled").implementPopup(2000);
        }
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

            this.blockAllNotifications = true;

            //getter call since this one is synchronized
            waitingNotificationQueue.removeAll();

            blockTimer = new Timer(300000, e1 -> {

                blockAllNotifications = false;

                new PopupPanelImpl(this, "Notifications status" + System.lineSeparator() + "reverted").implementPopup(2000);
            });
            blockTimer.setRepeats(false);
            blockTimer.start();

            new PopupPanelImpl(this, "All notifications disabled" + System.lineSeparator() + "for 5 minutes").implementPopup(2000);
        }

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
     Appends a new line to the text editor pane.

     <p>Retrieves the current text in the text editor pane and appends a new line character at the
     end of it.
     */
    private void appendNewLineToTextEditorPane() {

        String currentText = form_textEditorPane.getText();
        form_textEditorPane.setText(currentText + "\n");
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
     * Sets the Y position of the notification.
     *
     * @param notificationPositionY the Y position of the notification.
     */
    @Override
    public synchronized void setNotificationPositionY(int notificationPositionY) {

        this.notificationPositionY = notificationPositionY;
    }

    /**
     * Gets the list of notifications.
     *
     * @return the list of notifications.
     */
    @Override
    public List<NotificationImpl> getNotificationList() {

        return notificationList;
    }

    /**
     * Sets the startUp flag to indicate whether the system is starting up.
     *
     * @param startUp the startUp flag
     */
    @Override
    public void setStartUp(final boolean startUp) {

        this.startUp = startUp;
    }

    /**
     * Retrieves the status of notifications based on the current settings.
     *
     * @return the notification status
     */
    public NotificationStatus getNotificationStatus() {

        if (blockAllNotifications || startUp) {

            return NotificationStatus.ALL_DENIED;

        } else if (!blockInternalNotifications && !blockExternalNotifications) {

            return NotificationStatus.ALL_ALLOWED;

        } else if (!blockExternalNotifications) {

            return NotificationStatus.INTERNAL_ONLY;

        } else if (!blockInternalNotifications) {

            return NotificationStatus.EXTERNAL_ONLY;
        }

        return NotificationStatus.ALL_DENIED;
    }

    /**
     * Retrieves the comments hash map.
     *
     * @return the comments hash map
     */
    public LinkedHashMap<Long, CommentInterface> getCommentsHashMap() {

        return commentsHashMap;
    }

    /**
     Retrieves the WebSocket client.

     @return The WebSocket client.
     */
    @Override
    public CustomWebsocketClient getWebsocketClient() {

        return websocketClient;
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
     * Retrieves the name of the sender of the last message.
     *
     * @return the name of the sender of the last message.
     */
    @Override
    public String getLastMessageSenderName() {

        return lastMessageSenderName;
    }

    /**
     * Sets the name of the sender of the last message.
     *
     * @param lastMessageSenderName the name of the sender of the last message.
     */
    @Override
    public void setLastMessageSenderName(final String lastMessageSenderName) {

        this.lastMessageSenderName = lastMessageSenderName;
    }

    /**
     * Retrieves the timestamp of the last message.
     *
     * @return the timestamp of the last message.
     */
    @Override
    public String getLastMessageTimeStamp() {

        return lastMessageTimeStamp;
    }

    /**
     * Sets the timestamp of the last message.
     *
     * @param lastMessageTimeStamp the timestamp of the last message to be set.
     */
    @Override
    public void setLastMessageTimeStamp(final String lastMessageTimeStamp) {

        this.lastMessageTimeStamp = lastMessageTimeStamp;
    }
}