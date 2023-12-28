package com.soeguet.gui.main_frame;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.behaviour.GuiFunctionalityImpl;
import com.soeguet.behaviour.interfaces.GuiFunctionalityInterface;
import com.soeguet.cache.factory.CacheManagerFactory;
import com.soeguet.cache.implementations.WaitingNotificationQueue;
import com.soeguet.cache.manager.CacheManager;
import com.soeguet.dtos.StatusTransferDTO;
import com.soeguet.emoji.EmojiHandler;
import com.soeguet.emoji.EmojiInitializer;
import com.soeguet.emoji.EmojiPopUpMenuHandler;
import com.soeguet.emoji.interfaces.EmojiInitializerInterface;
import com.soeguet.emoji.interfaces.EmojiPopupInterface;
import com.soeguet.gui.comments.interfaces.CommentInterface;
import com.soeguet.gui.image_panel.ImagePanelImpl;
import com.soeguet.gui.image_panel.interfaces.ImageInterface;
import com.soeguet.gui.interrupt_dialog.InterruptDialogImpl;
import com.soeguet.gui.interrupt_dialog.interfaces.InterruptDialogInterface;
import com.soeguet.gui.main_frame.generated.ChatPanel;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.gui.notification_panel.NotificationImpl;
import com.soeguet.gui.popups.PopupPanelImpl;
import com.soeguet.gui.popups.interfaces.PopupInterface;
import com.soeguet.gui.properties.PropertiesPanelImpl;
import com.soeguet.gui.properties.interfaces.PropertiesInterface;
import com.soeguet.initialization.interfaces.MainFrameInitInterface;
import com.soeguet.model.EnvVariables;
import com.soeguet.model.MessageTypes;
import com.soeguet.model.jackson.MessageModel;
import com.soeguet.properties.CustomProperties;
import com.soeguet.properties.dto.CustomUserPropertiesDTO;
import com.soeguet.socket_client.ClientControllerImpl;
import com.soeguet.socket_client.CustomWebsocketClient;
import com.soeguet.socket_client.interfaces.ClientController;
import java.awt.AWTException;
import java.awt.Frame;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/** Main GUI method */
public class ChatMainFrameImpl extends ChatPanel
        implements MainFrameGuiInterface, MainFrameInitInterface {

    // variables -- start
    // FEATURE these need to be re-evaluated and maybe moved into the cache manager
    private final HashMap<String, CustomUserPropertiesDTO> chatClientPropertiesHashMap =
            new HashMap<>();

    // FEATURE cache comments on pane for hot replacements as HashSet -> data structure ready,
    // implementation missing
    // -> add to cache
    private final LinkedHashMap<Long, CommentInterface> commentsHashMap = new LinkedHashMap<>();
    private final List<NotificationImpl> notificationList = new ArrayList<>();

    /////////////////////////
    private final Logger logger = Logger.getLogger(ChatMainFrameImpl.class.getName());

    /** Instance of the object mapper used to convert objects to json and vice versa. */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Instance of cache manager primarily storing data structures of the collections api to help
     * cache some data.
     */
    private final CacheManager cacheManager = CacheManagerFactory.getCacheManager();

    /** Instance of class, holding a few environment variables */
    private EnvVariables envVariables;

    /** Instance of client controller handling everything socket related. */
    private ClientController clientController;

    /** Instance of the gui functionality handler. */
    private GuiFunctionalityInterface guiFunctionality;

    /** The margin east border for the JScrollPane. */
    private int JSCROLLPANE_MARGIN_RIGHT_BORDER;

    /** The margin bottom border for the JScrollPane. */
    private int JSCROLLPANE_MARGIN_BOTTOM_BORDER;

    /** Hashmap of all available emojis. */
    private HashMap<String, ImageIcon> emojiList;

    /** Instance of the emoji handler, which switches emojis with strings and vice versa. */
    private EmojiHandler emojiHandler;

    /**
     * Variable representing the timeAndUsername of this pc's client. The timeAndUsername on the
     * right side
     */
    private String username;

    /**
     * Represents the Y position of a notification. Will be updated everytime a notification is
     * generated
     */
    private volatile int notificationPositionY = 0;

    /** Indicates whether the start-up process has already been completed. */
    private boolean startUp = true;

    /** The name of the client which was last posted on the main panel. */
    private String lastMessageSenderName;

    /** The last time someone posted on the main panel. */
    private String lastMessageTimeStamp;

    /** Timer for blocking all messages. */
    private Timer blockTimer;

    private TrayIcon trayIcon;

    // variables -- end

    // constructors -- start
    /** Creates a new instance of ChatMainFrameImpl. */
    public ChatMainFrameImpl() {}

    // constructors -- end

    /**
     * This method is used to reposition the chat frame for testing purposes. It retrieves the value
     * of the 'chat_x_position' environment variable using System.getenv(), which represents the
     * desired x position of the chat frame. If the environment variable is not null, it repositions
     * the chat frame on the screen by setting the location using the retrieved x position and a
     * fixed y position of 100. This operation is performed asynchronously using
     * SwingUtilities.invokeLater() to ensure compatibility with Swing's event dispatching thread.
     */
    public void repositionChatFrameForTestingPurposes() {

        final String chatXPosition = System.getenv("CHAT_X_POSITION");

        if (chatXPosition != null) {

            SwingUtilities.invokeLater(
                    () -> {
                        final int chatFrameXPosition = Integer.parseInt(chatXPosition);
                        setLocation(chatFrameXPosition, 100);
                    });
        }
    }

    /**
     * Loads the statusArray from the environment variables and assigns it to the appropriate field.
     */
    public void loadUsernameFromEnvVariables(final EnvVariables envVariables) {

        this.envVariables = envVariables;

        // override statusArray if saved in GUI by user
        if (!this.envVariables.getChatUsername().isEmpty()) {

            setUsername(this.envVariables.getChatUsername());
        }
    }

    /**
     * Initializes the emoji list.
     *
     * <p>This method initializes the emoji list by creating a new instance of the EmojiInitializer
     * class and calling the createEmojiList() method to obtain the list of emojis. The emoji list
     * is then assigned to the emojiList instance variable of the current object.
     *
     * <p>The EmojiInitializer class is responsible for creating and initializing the list of
     * emojis.
     *
     * @see EmojiInitializer
     * @see EmojiInitializerInterface
     */
    public void initEmojiList(EmojiInitializerInterface emojiInitializer) {

        emojiList = emojiInitializer.createEmojiList();
    }

    /**
     * Sets the scroll pane margins.
     *
     * <p>This method determines the appropriate margin values for the scroll pane based on the
     * operating system and desktop environment.
     */
    public void setScrollPaneMargins() {

        if (getOSName().toLowerCase().contains("windows")) {

            JSCROLLPANE_MARGIN_BOTTOM_BORDER = 63;
            JSCROLLPANE_MARGIN_RIGHT_BORDER = 20;

        } else {

            if (getDesktopEnv().toLowerCase().contains("gnome")) {

                JSCROLLPANE_MARGIN_BOTTOM_BORDER = 27;

            } else {

                // e.g. KDE Plasma
                JSCROLLPANE_MARGIN_BOTTOM_BORDER = 56;
            }

            JSCROLLPANE_MARGIN_RIGHT_BORDER = 4;
        }
    }

    public void setMainFrameTitle() {

        final String title =
                "teamchat" + " - " + this.chatVersion() + "statusArray: " + getUsername();

        setTitle(title);
    }

    public void setGuiIcon() {

        URL iconURL = ChatMainFrameImpl.class.getResource("/icon.png");
        assert iconURL != null;
        ImageIcon icon = new ImageIcon(iconURL);
        setIconImage(icon.getImage());
    }

    /**
     * Sets the icons for the buttons in the chat form.
     *
     * <p>The icons are obtained from the resources folder and are used to set the icons for the
     * emoji, and picture buttons in the chat form.
     *
     * <p>The resource URLs for the icons are retrieved using the ChatMainFrameImpl class and the
     * corresponding file paths.
     *
     * <p>This method assumes that the required icons exist in the resources folder and will throw
     * an AssertionError if any of the resource URLs are null.
     *
     * @throws AssertionError if any of the required resource URLs are null.
     */
    public void setButtonIcons() {

        URL sendUrl = ChatMainFrameImpl.class.getResource("/emojis/$+1f4e8$+.png");
        URL emojiUrl = ChatMainFrameImpl.class.getResource("/emojis/$+1f60e$+.png");
        URL pictureUrl = ChatMainFrameImpl.class.getResource("/emojis/$+1f4bb$+.png");

        if (sendUrl != null) {

            form_sendButton.setIcon(new ImageIcon(sendUrl));
        }

        if (emojiUrl != null) {

            form_emojiButton.setIcon(new ImageIcon(emojiUrl));
        }

        if (pictureUrl != null) {

            form_pictureButton.setIcon(new ImageIcon(pictureUrl));
        }
    }

    public void setEnvVariables(final EnvVariables envVariables) {

        this.envVariables = envVariables;
    }

    /**
     * This method is used to load custom properties for a specific client. It initializes the
     * customProperties object with a new instance of CustomProperties, passing the current instance
     * as a parameter. It then calls the loaderThisClientProperties() method of the customProperties
     * object to load the properties for the current client . If the client properties are
     * successfully loaded, the method sets the statusArray property to the statusArray obtained
     * from the CustomUserProperties object.
     */
    public void loadCustomProperties() {

        CustomProperties customProperties = CustomProperties.getProperties();

        CustomUserPropertiesDTO client = customProperties.loaderThisClientProperties();

        // only override statusArray if nothing is set on startup
        if (client != null && username == null) {

            this.username = client.username();
        }
    }

    /**
     * Initializes the GUI functionality.
     *
     * <p>This method creates a new instance of the GuiFunctionality class, passing a reference to
     * the current object as a constructor argument. The GuiFunctionality object is then assigned to
     * the guiFunctionality instance variable of the current object.
     */
    public void initGuiFunctionality() {

        this.guiFunctionality = new GuiFunctionalityImpl(this);
        this.guiFunctionality.overrideTransferHandlerOfTextPane();
    }

    /**
     * Initializes the client controller.
     *
     * <p>This method creates a new instance of the ClientControllerImpl class, passing in the
     * current instance of the ChatMainFrameImpl class and the guiFunctionality object. It then
     * calls the determineWebsocketURI() method and the connectToWebsocket() method on the
     * clientController object.
     */
    public void initializeClientController() {

        clientController = new ClientControllerImpl(this, guiFunctionality);
        clientController.determineWebsocketURI();
        clientController.connectToWebsocket();
    }

    /**
     * Initializes the emoji handler.
     *
     * <p>This method creates a new instance of the EmojiHandler class and assigns it to the
     * emojiHandler instance variable of the current object.
     *
     * <p>The EmojiHandler class handles emoji functionality within the application.
     */
    public void initEmojiHandler() {

        this.emojiHandler = new EmojiHandler(this);
    }

    @Override
    public void setFixedScrollSpeed(final int i) {

        this.getMainTextBackgroundScrollPane().getVerticalScrollBar().setUnitIncrement(i);
    }

    /**
     * Resets the connection when the reset connection menu item is pressed.
     *
     * @param e The mouse event that triggered the method.
     */
    @Override
    public void resetConnectionMenuItemMousePressed(MouseEvent e) {

        // clear the main text panel first
        removeAllMessagesOnChatPanel();

        // close the websocket client
        clientController.closeConnection();

        // set null to be sure + preparation for reconnect
        clientController.prepareReconnection();

        // invalidate all caches
        cacheManager.invalidateCache();

        // reconnect to socket
        clientController.connectToWebsocket();
        this.logger.info("Reconnecting websocket client");
    }

    /**
     * Returns the websocket client associated with this instance.
     *
     * @return the websocket client
     */
    @Override
    public CustomWebsocketClient getWebsocketClient() {

        return clientController.getWebsocketClient();
    }

    /**
     * Gets the ObjectMapper instance used for converting JSON to Java objects and vice versa.
     *
     * @return the ObjectMapper instance
     */
    @Override
    public ObjectMapper getObjectMapper() {

        return objectMapper;
    }

    /**
     * Retrieves the GuiFunctionality.
     *
     * @return the GuiFunctionality object.
     */
    @Override
    public GuiFunctionalityInterface getGuiFunctionality() {

        return guiFunctionality;
    }

    /**
     * Retrieves the name of the last message.
     *
     * @return the name of the last message.
     */
    @Override
    public String getLastMessageSenderName() {

        return lastMessageSenderName;
    }

    /**
     * Sets the name of the last message.
     *
     * @param lastMessageSenderName the name of the last message.
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

    /**
     * Retrieves the timeAndUsername.
     *
     * @return the timeAndUsername as a String.
     */
    @Override
    public String getUsername() {

        return username;
    }

    /**
     * Sets the timeAndUsername.
     *
     * @param username the timeAndUsername to set.
     */
    @Override
    public void setUsername(String username) {

        this.username = username;
    }

    /**
     * Returns a HashMap containing the list of emojis and their corresponding image icons.
     *
     * <p>This method returns the emojiList HashMap, which contains the emojis and their
     * corresponding image icons. The key in the HashMap represents the emoji text, and the value
     * represents the corresponding image icon.
     *
     * @return the HashMap containing the list of emojis and their corresponding image icons
     */
    @Override
    public HashMap<String, ImageIcon> getEmojiList() {

        return emojiList;
    }

    /**
     * Returns a HashMap containing the chat client properties.
     *
     * <p>This method returns the chatClientPropertiesHashMap, which contains the chat client
     * properties. The key in the HashMap represents the property name, and the value represents the
     * corresponding value.
     *
     * @return the HashMap containing the chat client properties
     */
    @Override
    public HashMap<String, CustomUserPropertiesDTO> getChatClientPropertiesHashMap() {

        return chatClientPropertiesHashMap;
    }

    /**
     * Returns the Y position of the notification.
     *
     * @return the Y position of the notification.
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
     * Retrieves the comment hash map.
     *
     * @return the comments hash map
     */
    public LinkedHashMap<Long, CommentInterface> getCommentsHashMap() {

        return commentsHashMap;
    }

    @Override
    public boolean isStartUp() {

        return startUp;
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
     * Returns the name of the operating system.
     *
     * @return the name of the operating system.
     */
    public String getOSName() {

        return System.getProperty("os.name");
    }

    // getter & setter -- start
    /**
     * Returns the name of the user's current desktop environment on linux. The method retrieves the
     * value of the environment variable "XDG_CURRENT_DESKTOP".
     *
     * @return the name of the user's current desktop environment or null if the variable is not
     *     set.
     */
    public String getDesktopEnv() {

        return System.getenv("XDG_CURRENT_DESKTOP");
    }

    /**
     * Returns the emoji handler.
     *
     * <p>This method returns the emoji handler associated with the current object.
     *
     * @return the emoji handler.
     */
    public EmojiHandler getEmojiHandler() {

        return emojiHandler;
    }

    /**
     * Retrieves the value of the JSCROLLPANE_MARGIN_BOTTOM_BORDER constant.
     *
     * @return The value of the JSCROLLPANE_MARGIN_BOTTOM_BORDER constant.
     */
    public int getJSCROLLPANE_MARGIN_BOTTOM_BORDER() {

        return JSCROLLPANE_MARGIN_BOTTOM_BORDER;
    }

    /**
     * Retrieves the value of the JSCROLLPANE_MARGIN_RIGHT_BORDER constant.
     *
     * @return The value of the JSCROLLPANE_MARGIN_RIGHT_BORDER constant.
     */
    public int getJSCROLLPANE_MARGIN_RIGHT_BORDER() {

        return JSCROLLPANE_MARGIN_RIGHT_BORDER;
    }

    // getter & setter -- end

    /**
     * @return the logger
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * @return the cacheManager
     */
    public CacheManager getCacheManager() {
        return cacheManager;
    }

    /**
     * @return the envVariables
     */
    public EnvVariables getEnvVariables() {
        return envVariables;
    }

    /**
     * @return the clientController
     */
    public ClientController getClientController() {
        return clientController;
    }

    /**
     * @param clientController the clientController to set
     */
    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }

    /**
     * @param guiFunctionality the guiFunctionality to set
     */
    public void setGuiFunctionality(GuiFunctionalityInterface guiFunctionality) {
        this.guiFunctionality = guiFunctionality;
    }

    /**
     * @param jSCROLLPANE_MARGIN_RIGHT_BORDER the jSCROLLPANE_MARGIN_RIGHT_BORDER to set
     */
    public void setJSCROLLPANE_MARGIN_RIGHT_BORDER(int jSCROLLPANE_MARGIN_RIGHT_BORDER) {
        JSCROLLPANE_MARGIN_RIGHT_BORDER = jSCROLLPANE_MARGIN_RIGHT_BORDER;
    }

    /**
     * @param jSCROLLPANE_MARGIN_BOTTOM_BORDER the jSCROLLPANE_MARGIN_BOTTOM_BORDER to set
     */
    public void setJSCROLLPANE_MARGIN_BOTTOM_BORDER(int jSCROLLPANE_MARGIN_BOTTOM_BORDER) {
        JSCROLLPANE_MARGIN_BOTTOM_BORDER = jSCROLLPANE_MARGIN_BOTTOM_BORDER;
    }

    /**
     * @param emojiList the emojiList to set
     */
    public void setEmojiList(HashMap<String, ImageIcon> emojiList) {
        this.emojiList = emojiList;
    }

    /**
     * @param emojiHandler the emojiHandler to set
     */
    public void setEmojiHandler(EmojiHandler emojiHandler) {
        this.emojiHandler = emojiHandler;
    }

    /**
     * @return the blockTimer
     */
    public Timer getBlockTimer() {
        return blockTimer;
    }

    /**
     * @param blockTimer the blockTimer to set
     */
    public void setBlockTimer(Timer blockTimer) {
        this.blockTimer = blockTimer;
    }

    /**
     * Method called when the component is resized.
     *
     * @param e The ComponentEvent object representing the resize event.
     */
    @Override
    protected void thisComponentResized(ComponentEvent e) {

        final int rightBorderMargin = e.getComponent().getWidth() - JSCROLLPANE_MARGIN_RIGHT_BORDER;
        final int bottomBorderMargin =
                e.getComponent().getHeight()
                        - form_interactionAreaPanel.getHeight()
                        - JSCROLLPANE_MARGIN_BOTTOM_BORDER;

        this.form_mainTextBackgroundScrollPane.setBounds(
                1, 1, rightBorderMargin, bottomBorderMargin);

        repaintMainFrame();
    }

    /**
     * Called when the mouse is pressed on the property menu item.
     *
     * @param e The MouseEvent object representing the event.
     */
    @Override
    protected void propertiesMenuItemMousePressed(MouseEvent e) {

        SwingUtilities.invokeLater(
                () -> {
                    PropertiesInterface properties = new PropertiesPanelImpl(this);
                    properties.setPosition();
                    properties.setupOwnTabbedPane();
                    properties.setupClientsTabbedPane();

                    properties.setVisible(true);
                });
    }

    /**
     * Called when the state of internal notifications menu item is changed.
     *
     * @param e The ItemEvent object representing the event.
     */
    @Override
    protected void internalNotificationsMenuItemItemStateChanged(final ItemEvent e) {

        if (cacheManager.getCache("waitingNotificationQueue")
                instanceof WaitingNotificationQueue waitingNotificationQueue) {

            PopupInterface popup = new PopupPanelImpl(this);

            // remove all remaining and queued notifications
            if (e.getStateChange() == ItemEvent.DESELECTED) {

                // getter call since this one is synchronized
                waitingNotificationQueue.removeAll();

                popup.getMessageTextField().setText("Internal notifications disabled");

            } else {

                popup.getMessageTextField().setText("Internal notifications enabled");
            }

            popup.configurePopupPanelPlacement();
            popup.initiatePopupTimer(2_000);
        }
    }

    /**
     * Called when the connection details button is pressed.
     *
     * @param e The MouseEvent object representing the event.
     */
    @Override
    protected void connectionDetailsButtonMousePressed(final MouseEvent e) {

        clientController.serverInformationOptionPane();
    }

    /**
     * Handles the event when the mouse presses the exit menu item. Sets the default close operation
     * for the current JFrame to EXIT_ON_CLOSE and disposes of the current JFrame.
     *
     * @param e the MouseEvent object that triggered this event
     */
    @Override
    protected void exitMenuItemMousePressed(MouseEvent e) {

        SwingUtilities.invokeLater(
                () -> {

                    // make the window close on exit just to be sure
                    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    this.dispose();

                    System.exit(0);
                });
    }

    /**
     * Called when a key is pressed in the text editor pane. If the pressed key is not the Enter
     * key, the method simply returns. If the pressed key is the Enter key, it consumes the event
     * and performs the appropriate action based on whether the Shift key is pressed or not.
     *
     * @param e The KeyEvent object representing the key press event.
     */
    @Override
    protected void textEditorPaneKeyPressed(KeyEvent e) {

        // typing.. status
        if (e.getKeyCode() != KeyEvent.VK_ENTER) {

            sendIsTypingStatusToWebsocket();
            return;
        }

        // shift + enter -> new line
        if (e.isShiftDown()) {

            appendNewLineToTextEditorPane();
            return;
        }

        e.consume();

        // send message
        handleNonShiftEnterKeyPress();
    }

    /**
     * Handles the event when the mouse clicks the picture button in the current JFrame.
     *
     * @param e the MouseEvent object that triggered this event
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
     * Invoked when the emoji button is clicked in the chat GUI.
     *
     * <p>This method is an override of the emojiButton method from the superclass. It is called
     * when the emoji button is clicked.
     *
     * @param e the ActionEvent object generated when the emoji button is clicked
     */
    @Override
    protected void emojiButton(ActionEvent e) {

        EmojiPopupInterface emojiPopup =
                new EmojiPopUpMenuHandler(this, form_textEditorPane, form_emojiButton);
        emojiPopup.createEmojiPopupMenu();
    }

    /**
     * Handles the event when the send-button is clicked. Clears the text pane in the GUI and sends
     * the message to the socket.
     *
     * @param e the ActionEvent object that triggered this event
     */
    @Override
    protected void sendButton(ActionEvent e) {

        // first replace emoji with text
        emojiHandler.replaceImageIconWithEmojiDescription(getTextEditorPane());

        if (form_textEditorPane.getText().trim().isEmpty()) {

            return;
        }

        this.sendMessageToSocket();
    }

    /**
     * Handles the item state change event of the external notifications menu item. Updates the
     * state of the external notifications and displays a popup message accordingly.
     *
     * @param e the ItemEvent object that triggered this event
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
     * Handles the event when the window is closed.
     *
     * @param e the WindowEvent object that triggered this event
     */
    @Override
    protected void thisWindowClosing(final WindowEvent e) {

        setState(Frame.ICONIFIED);
    }

    /**
     * Handles the event when the interrupt menu item is pressed.
     *
     * @param e the MouseEvent object that triggered this event
     */
    @Override
    protected void interruptMenuItemMousePressed(final MouseEvent e) {

        InterruptDialogInterface interruptDialogInterface = new InterruptDialogImpl(this);

        interruptDialogInterface.populateDialogWithAllRegisteredClients(
                chatClientPropertiesHashMap);
        interruptDialogInterface.pack();
        interruptDialogInterface.setLocationRelativeTo(this);
        interruptDialogInterface.setVisible(true);
    }

    /**
     * Handles the event when the state of the allNotificationsMenuItem changes.
     *
     * @param e the ItemEvent object that triggered this event
     */
    @Override
    protected void allNotificationsMenuItemItemStateChanged(final ItemEvent e) {

        // any kind of change needs to get rid of an existing timer
        resetMessageBlockTimer();

        // block all notifications for 5 minutes
        if (e.getStateChange() == ItemEvent.SELECTED) {

            if (cacheManager.getCache("waitingNotificationQueue")
                    instanceof WaitingNotificationQueue waitingNotificationQueue) {

                // getter call since this one is synchronized
                waitingNotificationQueue.removeAll();
            }

            initializeNewMessageBlockTimer();

            // show popup
            initializePopupMessage("All notifications disabled", "for 5 minutes");
        }
    }

    /**
     * Method used to retrieve the version of the JAR file.
     *
     * <p>This method uses reflection to get the implementation version of the JAR file containing
     * the class. It is assumed that the JAR file has its version specified in its manifest file.
     *
     * @return The version of the JAR file as a string, or null if the version cannot be determined.
     */
    String retrieveJarVersion() {

        return getClass().getPackage().getImplementationVersion();
    }

    private String chatVersion() {

        Properties properties = new Properties();
        InputStream inputStream =
                getClass().getClassLoader().getResourceAsStream("version.properties");

        if (inputStream != null) {

            try {

                properties.load(inputStream);
                return properties.getProperty("version") + " - ";

            } catch (IOException e) {

                throw new RuntimeException(e);
            }

        } else {

            return "";
        }
    }

    /**
     * Method used to repaint the main frame.
     *
     * <p>This method revalidates and repaints the main frame component, ensuring that any changes
     * to its layout or appearance are correctly displayed on the screen.
     */
    private void repaintMainFrame() {

        String version = retrieveJarVersion();
        this.setTitle("teamchat - " + version + " - client name: " + this.getUsername());

        this.revalidate();
        this.repaint();
    }

    /**
     * Removes all messages from the chat panel.
     *
     * <p>The method uses `SwingUtilities.invokeLater()` to ensure that the removal of messages is
     * performed on the event dispatch thread, as it involves modifications to the GUI.
     *
     * <p>Within the method, the `form_mainTextPanel.removeAll()` is called to remove all components
     * from the chat panel. Additionally, the `repaintMainFrame()` method is called to repaint the
     * main frame, ensuring that the changes are immediately visible to the user.
     */
    private void removeAllMessagesOnChatPanel() {

        SwingUtilities.invokeLater(
                () -> {
                    this.form_mainTextPanel.removeAll();
                    repaintMainFrame();
                });
    }

    /**
     * Sends a typing status message to the websocket server. The method forms a JSON object
     * representing the typing status and sends it to the websocket client. If an error occurs while
     * processing the JSON, a RuntimeException is thrown.
     *
     * @throws RuntimeException if an error occurs while processing the JSON.
     */
    private void sendIsTypingStatusToWebsocket() {

        try {

            ArrayList<String> array = new ArrayList<>();
            array.add(this.getUsername());
            final byte[] jsonTypingStatus =
                    objectMapper.writeValueAsBytes(new StatusTransferDTO("typing", array));

            clientController.getWebsocketClient().send(jsonTypingStatus);

        } catch (JsonProcessingException e) {

            throw new RuntimeException(e);
        }
    }

    /**
     * Appends a new line to the text editor pane.
     *
     * <p>Retrieves the current text in the text editor pane and appends a new line character at the
     * end of it.
     */
    private void appendNewLineToTextEditorPane() {

        final int caretPosition = form_textEditorPane.getCaretPosition();
        form_textEditorPane.setSelectionStart(0);
        form_textEditorPane.setSelectionEnd(caretPosition);
        form_textEditorPane.replaceSelection(form_textEditorPane.getSelectedText() + "\n");

        final int endPosition = form_textEditorPane.getCaretPosition();
        form_textEditorPane.setSelectionStart(endPosition);
        form_textEditorPane.setSelectionEnd(endPosition);
    }

    /**
     * Handles a key press event when the enter key is pressed without pressing the shift key.
     *
     * <p>Retrieves the content of the text editor pane, trims any leading or trailing space, and
     * checks if it is empty. If the content is empty, it clears the text editor pane. Otherwise, it
     * calls the `sendMessageToSocket` method to clear the text pane and send the current content to
     * a socket.
     */
    private void handleNonShiftEnterKeyPress() {

        // emoji to text -> text extraction
        emojiHandler.replaceImageIconWithEmojiDescription(form_textEditorPane);
        String textPaneContent = form_textEditorPane.getText().trim();

        if (textPaneContent.isEmpty()) {

            // empty -> reset
            form_textEditorPane.setText("");

        } else {

            this.sendMessageToSocket();
        }
    }

    private void sendMessageToSocket() {

        String textFromInput = guiFunctionality.getTextFromInput();

        MessageModel messageModel = new MessageModel();
        messageModel.setMessage(textFromInput);
        messageModel.setTime(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        messageModel.setSender(this.getUsername());
        messageModel.setMessageType(MessageTypes.NORMAL);

        String serializedTextFromInput = guiFunctionality.convertUserTextToJSON(messageModel);
        if (serializedTextFromInput != null) {
            guiFunctionality.sendMessageToSocket(serializedTextFromInput);
            guiFunctionality.notifyClientsSendStatus();
        }
        guiFunctionality.clearTextPane();
    }

    private void initializePopupMessage(final String x, final String x1) {

        PopupInterface popup = new PopupPanelImpl(this);
        popup.getMessageTextField().setText(x + System.lineSeparator() + x1);
        popup.configurePopupPanelPlacement();
        popup.initiatePopupTimer(3_000);
    }

    private void initializeNewMessageBlockTimer() {

        // blocking for 5 Minutes
        blockTimer =
                new Timer(
                        1_000 * 60 * 5,
                        e1 -> {
                            form_allNotificationsMenuItem.setSelected(false);

                            // show popup
                            initializePopupMessage("Notifications status", "reverted");
                        });

        blockTimer.setRepeats(false);
        blockTimer.start();
    }

    private void resetMessageBlockTimer() {

        if (blockTimer != null) {

            blockTimer.stop();
            blockTimer = null;
        }
    }

    @Override
    public TrayIcon getTrayIcon() {

        return trayIcon;
    }

    @Override
    public void setupSystemTrayIcon() {

        if (SystemTray.isSupported()) {

            URL iconURL = ChatMainFrameImpl.class.getResource("/icon.png");
            assert iconURL != null;
            ImageIcon icon = new ImageIcon(iconURL);

            try {

                SystemTray tray = SystemTray.getSystemTray();
                trayIcon = new TrayIcon(icon.getImage(), "teamchat");
                trayIcon.setImageAutoSize(true);
                tray.add(trayIcon);

            } catch (AWTException e) {

                System.err.println("TrayIcon could not be added.");
            }

        } else {

            System.err.println("System tray is not supported.");
        }
    }
}
