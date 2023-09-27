package com.soeguet.gui.main_frame;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.behaviour.GuiFunctionality;
import com.soeguet.gui.image_panel.ImagePanelImpl;
import com.soeguet.gui.main_frame.generated.ChatPanel;
import com.soeguet.gui.notification_panel.NotificationImpl;
import com.soeguet.gui.popups.PopupPanelImpl;
import com.soeguet.gui.properties.PropertiesPanelImpl;
import com.soeguet.model.EnvVariables;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.properties.CustomProperties;
import com.soeguet.properties.CustomUserProperties;
import com.soeguet.socket_client.CustomWebsocketClient;
import com.soeguet.util.EmojiHandler;
import com.soeguet.util.EmojiInitializer;
import com.soeguet.util.EmojiPopUpMenuHandler;
import com.soeguet.util.NotificationStatus;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

/**
 Represents the main frame for the chat application.
 Inherits from ChatPanel and implements MainFrameInterface.
 */
public class ChatMainFrameImpl extends ChatPanel implements MainFrameInterface {

    private final Logger logger = Logger.getLogger(ChatMainFrameImpl.class.getName());
    private final HashMap<String, CustomUserProperties> chatClientPropertiesHashMap;
    private final LinkedBlockingDeque<String> socketMessageQueue;

    private final LinkedBlockingDeque<String> messageQueue;
    private final LinkedBlockingDeque<BaseModel> notificationActiveQueue;
    private final LinkedBlockingDeque<String> notificationWaitingQueue;
    private final ObjectMapper objectMapper;
    private final CustomProperties customProperties;
    private final List<NotificationImpl> notificationList = new ArrayList<>();
    private final AtomicBoolean isProcessingClientMessages = new AtomicBoolean(false);
    private final EnvVariables envVariables;
    private GuiFunctionality guiFunctionality;
    private URI serverUri;
    private int JSCROLLPANE_MARGIN_RIGHT_BORDER;
    private int JSCROLLPANE_MARGIN_BOTTOM_BORDER;
    private HashMap<String, ImageIcon> emojiList;
    private EmojiHandler emojiHandler;
    private CustomWebsocketClient websocketClient;
    private String username;
    private JPanel messagePanel;
    private volatile int notificationPositionY = 0;
    private boolean startUp = true;
    private volatile int possibleNotifications = 3;
    private String lastMessageSenderName;
    private String lastMessageTimeStamp;

    //TODO maybe remove and use menuitem directly
    private boolean blockAllNotifications = form_allNotificationsMenuItem.isSelected();
    private boolean blockInternalNotifications = !form_internalNotificationsMenuItem.isSelected();
    private boolean blockExternalNotifications = !form_externalNotificationsMenuItem.isSelected();
    private Timer blockTimer;

    public ChatMainFrameImpl(final EnvVariables envVariables) {

        this.envVariables = envVariables;
        this.username = envVariables.getChatUsername();

        //TODO remove for merge in master
        final String chatXPosition = System.getenv("chat.x.position");
        if (chatXPosition != null) {
            SwingUtilities.invokeLater(() -> setLocation(Integer.parseInt(chatXPosition), 100));
        }

        socketMessageQueue = new LinkedBlockingDeque<>();
        messageQueue = new LinkedBlockingDeque<>();
        notificationActiveQueue = new LinkedBlockingDeque<>(3);
        notificationWaitingQueue = new LinkedBlockingDeque<>();
        chatClientPropertiesHashMap = new HashMap<>();
        objectMapper = new ObjectMapper();
        customProperties = new CustomProperties(this);

        initGuiFunctionality();

        setScrollPaneMargins();
        initEmojiHandlerAndList();
        initWebSocketClient();

        setVisible(true);
    }

    private void initGuiFunctionality() {

        guiFunctionality = new GuiFunctionality(this);
    }

    private void setScrollPaneMargins() {

        if (System.getProperty("os.name").toLowerCase().contains("windows")) {

            JSCROLLPANE_MARGIN_BOTTOM_BORDER = 63;
            JSCROLLPANE_MARGIN_RIGHT_BORDER = 20;

        } else {

            if (System.getenv("linux.desktop.env").toLowerCase().contains("gnome")) {

                JSCROLLPANE_MARGIN_BOTTOM_BORDER = 27;

            } else {

                //e.g. KDE Plasma
                JSCROLLPANE_MARGIN_BOTTOM_BORDER = 56;
            }

            JSCROLLPANE_MARGIN_RIGHT_BORDER = 4;
        }
    }

    private void initEmojiHandlerAndList() {

        emojiHandler = new EmojiHandler(this);
        emojiList = new EmojiInitializer().createEmojiList();
    }

    /**
     Initializes the WebSocket client.

     It retrieves the server IP and port from the environment variables and creates a URI for the WebSocket server.
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
        Timer connectTimer = new Timer(1000, e -> {
            new PopupPanelImpl(this, "Connecting to server");
            logger.info("connecting websocket client");
            connectToWebsocket();
        });
        connectTimer.setRepeats(false);
        connectTimer.start();
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

            final String serverIp = serverIpTextField.getText();
            final String serverPort = serverPortTextField.getText();

            envVariables.setChatIp(serverIp);
            envVariables.setChatPort(serverPort);

            try {
                serverUri = new URI("ws://" + serverIp + ":" + serverPort);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     Establishes a connection to a WebSocket server.

     This method creates a new virtual thread executor and submits a task to it. The task is responsible for creating a new instance of a CustomWebsocketClient and connecting to the WebSocket server specified by the serverUri. The connection process is asynchronously executed on a separate thread.
     */
    private void connectToWebsocket() {

        websocketClient = new CustomWebsocketClient(serverUri, this);
        websocketClient.connect();

    }

    /**
     Creates a server information panel.

     @param serverIpTextField   the text field for server IP
     @param serverPortTextField the text field for server port

     @return the created JPanel containing the server information panel
     */
    private JPanel createServerInfoPanel(final JTextField serverIpTextField, final JTextField serverPortTextField) {

        JPanel myPanel = new JPanel(new MigLayout("wrap 2"));

        myPanel.add(new JLabel("Port:"));
        serverIpTextField.setText(envVariables.getChatIp().isBlank() ? "127.0.0.1" : envVariables.getChatIp());
        myPanel.add(serverIpTextField);

        myPanel.add(new JLabel("Ip:"));
        serverPortTextField.setText(envVariables.getChatPort().isBlank() ? "8100" : envVariables.getChatPort());
        myPanel.add(serverPortTextField);

        serverIpTextField.requestFocus();

        return myPanel;
    }

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

        this.form_mainTextBackgroundScrollPane.setBounds(1, 1, e.getComponent().getWidth() - JSCROLLPANE_MARGIN_RIGHT_BORDER, e.getComponent().getHeight() - form_interactionAreaPanel.getHeight() - JSCROLLPANE_MARGIN_BOTTOM_BORDER);
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

    @Override
    protected void internalNotificationsMenuItemItemStateChanged(final ItemEvent e) {

        //remove all remaining and queued notifications
        if (e.getStateChange() == ItemEvent.DESELECTED) {

            this.getNotificationWaitingQueue().removeAll(this.getNotificationWaitingQueue());
            blockInternalNotifications = true;

            new PopupPanelImpl(this, "Internal notifications disabled").implementPopup(2000);

        } else {

            blockInternalNotifications = false;

            new PopupPanelImpl(this, "Internal notifications enabled").implementPopup(2000);
        }
    }

    @Override
    protected void connectionDetailsButtonMousePressed(final MouseEvent e) {

        System.out.println("connectionDetailsButtonMouseClicked");
        serverInformationOptionPane();
    }

    /**
     Resets the connection when the reset connection menu item is pressed.

     @param e The mouse event that triggered the method.
     */
    @Override
    protected void resetConnectionMenuItemMousePressed(MouseEvent e) {

        //clear the main text panel first
        SwingUtilities.invokeLater(() -> {

            form_mainTextPanel.removeAll();
            form_mainTextPanel.revalidate();
            form_mainTextPanel.repaint();
        });

        //close the websocket client
        if (websocketClient.isOpen()) {

            logger.info("Closing websocket client");
            websocketClient.close();
        }

        //set null to be sure
        websocketClient = null;
        logger.info("Reconnecting websocket client");

        connectToWebsocket();
    }

    /**
     Handles the event when the mouse presses the exit menu item. Sets the default close operation
     for the current JFrame to EXIT_ON_CLOSE and disposes the current JFrame.

     @param e the MouseEvent object that triggered this event
     */
    @Override
    protected void exitMenuItemMousePressed(MouseEvent e) {

        SwingUtilities.invokeLater(() -> {

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

        if (e.getKeyCode() != KeyEvent.VK_ENTER) {

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
     checks if it is empty. If the content is empty, it clears the text editor pane. Otherwise, it
     calls the `clearTextPaneAndSendMessageToSocket` method to clear the text pane and send the
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

    @Override
    protected void allNotificationsMenuItemItemStateChanged(final ItemEvent e) {

        //any kind of change needs to get rid of an existing timer
        if (blockTimer != null) {

            blockTimer.stop();
            blockTimer = null;
        }

        //block all notifications for 5 minutes
        if (e.getStateChange() == ItemEvent.SELECTED) {

            blockAllNotifications = true;

            blockTimer = new Timer(300000, e1 -> {

                blockAllNotifications = false;

                new PopupPanelImpl(this, "Notifications status" + System.lineSeparator() + "reverted").implementPopup(2000);
            });
            blockTimer.setRepeats(false);
            blockTimer.start();

            new PopupPanelImpl(this, "All notifications disabled" + System.lineSeparator() + "for 5 minutes").implementPopup(2000);
        }

    }

    public LinkedBlockingDeque<String> getNotificationWaitingQueue() {

        return notificationWaitingQueue;
    }

    public boolean isStartUp() {

        return startUp;
    }

    @Override
    public void setStartUp(final boolean startUp) {

        this.startUp = startUp;
    }

    public NotificationStatus getNotificationStatus() {

        if (blockAllNotifications) {

            System.out.println("block all notifications");
            return NotificationStatus.ALL_DENIED;

        } else if (!blockInternalNotifications && !blockExternalNotifications) {

            System.out.println("allow all notifications");
            return NotificationStatus.ALL_ALLOWED;

        } else if (!blockExternalNotifications) {

            System.out.println("allow external notifications");
            return NotificationStatus.INTERNAL_ONLY;

        } else if (!blockInternalNotifications) {

            System.out.println("allow internal notifications");
            return NotificationStatus.EXTERNAL_ONLY;
        }

        return NotificationStatus.ALL_DENIED;
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

    @Override
    public String getLastMessageSenderName() {

        return lastMessageSenderName;
    }

    @Override
    public void setLastMessageSenderName(final String lastMessageSenderName) {

        this.lastMessageSenderName = lastMessageSenderName;
    }

    @Override
    public String getLastMessageTimeStamp() {

        return lastMessageTimeStamp;
    }

    @Override
    public void setLastMessageTimeStamp(final String lastMessageTimeStamp) {

        this.lastMessageTimeStamp = lastMessageTimeStamp;
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
     Retrieves the message panel.

     @return the JPanel object representing the message panel.
     */
    @Override
    public JPanel getMessagePanel() {

        return messagePanel;
    }

    /**
     Sets the message panel for displaying messages.

     @param messagePanel the JPanel to set as the message panel.
     */
    @Override
    public void setMessagePanel(JPanel messagePanel) {

        this.messagePanel = messagePanel;
    }

    /**
     Retrieves the message queue containing the messages.

     @return the LinkedBlockingDeque<String> representing the message queue.
     */
    @Override
    public LinkedBlockingDeque<String> getMessageQueue() {

        return messageQueue;
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
     Retrieves the client message queue.

     @return A LinkedBlockingDeque object representing the client message queue.
     */
    @Override
    public synchronized LinkedBlockingDeque<String> getClientMessageQueue() {

        return socketMessageQueue;
    }

    @Override
    public AtomicBoolean getIsProcessingClientMessages() {

        return isProcessingClientMessages;
    }

    @Override
    public synchronized int getNotificationPositionY() {

        return this.notificationPositionY;
    }

    @Override
    public synchronized void setNotificationPositionY(int notificationPositionY) {

        this.notificationPositionY = notificationPositionY;
    }

    @Override
    public List<NotificationImpl> getNotificationList() {

        return notificationList;
    }

    @Override
    public synchronized int getPossibleNotifications() {

        return possibleNotifications;
    }

    @Override
    public synchronized void setPossibleNotifications(final int possibleNotifications) {

        this.possibleNotifications = possibleNotifications;
    }

    public LinkedBlockingDeque<BaseModel> getNotificationActiveQueue() {

        return notificationActiveQueue;
    }

    private boolean blockMessages() {
        //TODO handle message blocking
        return false;
    }

}