package com.soeguet.behaviour;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.behaviour.interfaces.GuiFunctionalityInterface;
import com.soeguet.behaviour.interfaces.SocketToGuiInterface;
import com.soeguet.behaviour.util.CustomTransferHandler;
import com.soeguet.cache.factory.CacheManagerFactory;
import com.soeguet.cache.implementations.MessageQueue;
import com.soeguet.cache.manager.CacheManager;
import com.soeguet.dtos.StatusTransferDTO;
import com.soeguet.gui.comments.CommentManagerImpl;
import com.soeguet.gui.comments.interfaces.CommentManager;
import com.soeguet.gui.interrupt_dialog.handler.InterruptHandler;
import com.soeguet.gui.interrupt_dialog.interfaces.InterruptHandlerInterface;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.gui.main_panel.MessageDisplayHandler;
import com.soeguet.gui.main_panel.interfaces.MessageDisplayHandlerInterface;
import com.soeguet.gui.notification_panel.DesktopNotificationHandler;
import com.soeguet.gui.notification_panel.interfaces.DesktopNotificationHandlerInterface;
import com.soeguet.gui.popups.PopupPanelImpl;
import com.soeguet.gui.popups.interfaces.PopupInterface;
import com.soeguet.gui.typing_panel.TypingPanelHandler;
import com.soeguet.gui.typing_panel.interfaces.TypingPanelHandlerInterface;
import com.soeguet.model.MessageTypes;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.MessageModel;
import com.soeguet.properties.dto.CustomUserPropertiesDTO;
import com.soeguet.socket_client.CustomWebsocketClient;
import com.soeguet.util.NotificationStatus;
import java.awt.Color;
import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * This class provides additional functionality to the GUI.
 * <p>
 * Implements the SocketToGuiInterface for receiving messages from the socket.
 */
public class GuiFunctionalityImpl implements GuiFunctionalityInterface, SocketToGuiInterface {

    private final MainFrameGuiInterface mainFrame;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger = Logger.getLogger(GuiFunctionalityImpl.class.getName());
    private final CacheManager cacheManager = CacheManagerFactory.getCacheManager();
    private CommentManager commentManager;
    private MessageDisplayHandlerInterface messageDisplayHandler;

    /**
     * Constructor for the GuiFunctionalityImpl class.
     * 
     * @param mainFrame the MainFrameGuiInterface object used to interact with the main frame GUI.
     */
    public GuiFunctionalityImpl(MainFrameGuiInterface mainFrame) {

        this.mainFrame = mainFrame;
    }

    /**
     * Generates a random RGB integer value.
     * <p>
     * This method creates a new instance of the Random class to generate random values for the red,
     * green, and blue components of the RGB color. It then creates a new Color object using the
     * generated values and retrieves the RGB integer value using the getRGB() method.
     * 
     * @return the random RGB integer value
     */
    private int getRandomRgbIntValue() {

        Random rand = new Random();
        int r = rand.nextInt(256);
        int g = rand.nextInt(256);
        int b = rand.nextInt(256);
        return new Color(r, g, b).getRGB();
    }

    /**
     * Overrides the transfer handler of the text pane.
     * <p>
     * This method sets a custom transfer handler for the text pane of the main frame. The custom
     * transfer handler is created using the main frame object.
     * 
     * @see CustomTransferHandler
     */
    @Override
    public void overrideTransferHandlerOfTextPane() {

        this.mainFrame.getTextEditorPane().setTransferHandler(new CustomTransferHandler(mainFrame));
    }

    /**
     * Retrieves text input from the main frame's text editor pane.
     * 
     * @return the text input retrieved from the main frame's text editor pane
     */
    @Override
    public String getTextFromInput() {

        return this.mainFrame.getTextEditorPane().getText();
    }


    /**
     * Converts the given user text to JSON format.
     *
     * @param messageModel the model containing the user text to be converted
     * @return the user text converted to JSON format as a string
     */
    @Override
    public String convertUserTextToJSON(MessageModel messageModel) {

        try {

            return this.mainFrame.getObjectMapper().writerWithDefaultPrettyPrinter()
                    .writeValueAsString(messageModel);

        } catch (JsonProcessingException e) {

            logger.log(java.util.logging.Level.SEVERE, "Error converting to JSON", e);

            JOptionPane.showMessageDialog((Component) this.mainFrame,
                    "Your message could not be processed. Try again please.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * Sends a message to the socket.
     * <p>
     * This method sends the given message string to the websocket client associated with the main
     * frame. Additionally, it sends a typing status message to indicate that a message is being
     * sent.
     * 
     * @param messageString the message to be sent
     */
    @Override
    public void sendMessageToSocket(String messageString) {

        // normal message
        this.mainFrame.getWebsocketClient().send(messageString);
    }

    @Override
    public void notifyClientsSendStatus() {

        final CustomWebsocketClient websocketClient = this.mainFrame.getWebsocketClient();

        // sending status
        try {

            StatusTransferDTO statusTransferDTO =
                    new StatusTransferDTO("send", new ArrayList<String>());
            websocketClient.send(objectMapper.writeValueAsBytes(statusTransferDTO));

        } catch (JsonProcessingException e) {

            logger.log(java.util.logging.Level.SEVERE,
                    "GuiFunctionalityImpl > notifyClientsSendStatus", e);
            logger.log(java.util.logging.Level.SEVERE, "Status \"SEND\" could not be sent.", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Clears the text pane.
     * 
     * <p>
     * This method clears the content of the text editor pane associated with the main frame. The
     * text pane will be empty after calling this method.
     */
    @Override
    public void clearTextPane() {

        this.mainFrame.getTextEditorPane().setText("");
    }

    @Override
    public void internalNotificationHandling(final String message) {

        DesktopNotificationHandlerInterface desktopNotificationHandler =
                new DesktopNotificationHandler(mainFrame);
        final NotificationStatus notificationStatus =
                desktopNotificationHandler.determineDesktopNotificationStatus();
        desktopNotificationHandler.createDesktopNotification(message, notificationStatus);
    }

    /**
     * Called when a message is received.
     * 
     * @param message The message received.
     */
    @Override
    public void onMessage(String message) {

        switch (message) {

            case "__startup__end__" -> mainFrame.setStartUp(false);

            case "welcome to the server" -> {
                PopupInterface popup = new PopupPanelImpl(mainFrame);
                popup.getMessageTextField().setText("Welcome to the server!");
                popup.configurePopupPanelPlacement();
                popup.initiatePopupTimer(2_000);
            }

            case null -> throw new IllegalStateException();

            default -> {

                // add to queue
                if (cacheManager.getCache("messageQueue") instanceof MessageQueue messageQueue) {

                    messageQueue.addLast(message);
                }

                // write to pane
                spamBuffer();

                // notification
                createDesktopNotification(message);
            }
        }

        scrollMainPanelDownToLastMessage(this.mainFrame.getMainTextBackgroundScrollPane());
    }

    /**
     * Called when a message is received.
     * 
     * @param message The message received as a byte array.
     */
    @Override
    public void onMessage(byte[] message) {

        final JsonNode parsedJson = parseJsonNode(message);

        StatusTransferDTO statusTransferDTO =
                objectMapper.convertValue(parsedJson, StatusTransferDTO.class);

        switch (statusTransferDTO.type()) {

            case "typing" -> handleTypingPanel(statusTransferDTO);

            case "send" -> mainFrame.getTypingLabel().setText(" ");

            case "interrupt" -> handleInterruption(statusTransferDTO);

            default -> throw new IllegalStateException();
        }
    }

    /**
     * Parses a JSON node from a byte array.
     * 
     * @param message The message as a byte array.
     * 
     * @return The parsed JSON node.
     */
    private JsonNode parseJsonNode(final byte[] message) {

        try {

            return mainFrame.getObjectMapper().readTree(message);

        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }

    /**
     * Handles the typing panel based on the typing status received.
     * 
     * @param typingStatus The typing status information.
     */
    private void handleTypingPanel(StatusTransferDTO typingStatus) {

        TypingPanelHandlerInterface typingPanelHandler = new TypingPanelHandler(mainFrame);

        String textOnTypingLabel = typingPanelHandler.retrieveTextOnTypingLabel();

        // if typing client is already present on label -> return!
        // typingStatus.statusArray()[0] array with only one value, always!
        if (typingStatus.array().get(0) == null) {
            return;
        }
        final String typingUsername = typingStatus.array().get(0);

        if (textOnTypingLabel.contains(typingUsername)) {

            return;
        }

        final StringBuilder stringBuilder =
                typingPanelHandler.generateTypingLabel(textOnTypingLabel, typingUsername);

        typingPanelHandler.displayUpdatedTypingLabel(stringBuilder);
    }

    /**
     * Handles the interruption based on the client interruption status received.
     * 
     * @param clientInterruptDTO The interruption status information.
     */
    private void handleInterruption(StatusTransferDTO clientInterruptDTO) {

        InterruptHandlerInterface interruptHandler = new InterruptHandler(mainFrame);

        clientInterruptDTO.array().forEach(interruptHandler::forceChatGuiToFront);
    }

    /**
     * Spams the buffer with a message to be displayed in the GUI chat panel.
     * <p>
     * This method writes a message to the chat panel and then adds a brief delay before proceeding.
     * It is recommended to replace this method with a more efficient buffering or caching system in
     * the future.
     * 
     * @throws RuntimeException if there is an InterruptedException during the delay.
     */
    private void spamBuffer() {

        writeGuiMessageToChatPanel();

        // TODO replace this with a buffer or caching system
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a desktop notification based on the provided message.
     * 
     * @param message The message to be displayed in the notification.
     * 
     * @throws RuntimeException if there is an error while handling the notification.
     */
    private void createDesktopNotification(final String message) {

        // convert the message to a java object and return if the message came from this client
        final BaseModel baseModel = parseMessageToJsonModel(message);

        // return if the message was sent by this client (no notification for own messages)
        if (compareSenderToUsername(baseModel)) {

            return;
        }

        DesktopNotificationHandlerInterface desktopNotificationHandler =
                new DesktopNotificationHandler(mainFrame);

        NotificationStatus notificationStatus =
                desktopNotificationHandler.determineDesktopNotificationStatus();

        desktopNotificationHandler.createDesktopNotification(message, notificationStatus);
    }

    /**
     * Compares the sender of a BaseModel object to the timeAndUsername of mainFrame.
     * 
     * @param baseModel The BaseModel object to compare the sender to.
     * 
     * @return true if the sender of the baseModel object is equal to the timeAndUsername of
     *         mainFrame; false otherwise.
     */
    private boolean compareSenderToUsername(final BaseModel baseModel) {

        return baseModel.getSender().equals(fetchUsernameFromCache());
    }

    /**
     * This method writes a GUI message to the chat panel and handles all the setup.
     */
    private synchronized void writeGuiMessageToChatPanel() {

        if (commentManager == null) {

            commentManager = new CommentManagerImpl(mainFrame);
        }

        if (messageDisplayHandler == null) {

            messageDisplayHandler = new MessageDisplayHandler(mainFrame, commentManager);
            messageDisplayHandler.setCacheManager(cacheManager);
        }

        // retrieve the message from cache
        final String message = messageDisplayHandler.pollMessageFromCache();
        if (message == null) {
            return;
        }

        // convert message to java object
        final BaseModel baseModel = this.parseMessageToJsonModel(message);

        // register user from message to local cache if not present yet
        this.checkIfMessageSenderAlreadyRegisteredInLocalCache(
                mainFrame.getChatClientPropertiesHashMap(), baseModel.getSender());

        // handle displayed message name - nickname as well as timeAndUsername
        // TODO: 02.11.23 maybe nickname support -- removed it for now
        // String nickname = checkForNickname(baseModel.getSender());

        // process and display message
        if (this.retrieveMessageType(baseModel) == MessageTypes.INTERACTED) {

            messageDisplayHandler.updateExistingMessage(baseModel);

        } else {

            messageDisplayHandler.processAndDisplayMessage(baseModel);
        }

        // check for remaining messages in the local cache
        checkIfDequeIsEmptyOrStartOver();
    }

    private byte retrieveMessageType(final BaseModel baseModel) {

        if (mainFrame.isStartUp()) {

            return MessageTypes.NORMAL;
        }

        return baseModel.getMessageType();
    }

    private BaseModel parseMessageToJsonModel(final String message) {

        try {

            return objectMapper.readValue(message, BaseModel.class);

        } catch (JsonProcessingException e) {

            throw new RuntimeException(e);
        }
    }

    /**
     * Checks if the message queue is empty or if the chat needs to start over. If the message queue
     * is not empty, it sets a timer for displaying new messages.
     */
    private void checkIfDequeIsEmptyOrStartOver() {

        if (cacheManager.getCache("messageQueue") instanceof MessageQueue messageQueue) {

            repaintMainFrame();

            SwingUtilities.invokeLater(() -> scrollMainPanelDownToLastMessage(
                    this.mainFrame.getMainTextBackgroundScrollPane()));

            if (!messageQueue.isEmpty()) {

                timerForNewMessageToChatPanel();
            }
        }
    }

    /**
     * This method sets a timer to periodically update the chat panel with new messages.
     */
    private void timerForNewMessageToChatPanel() {

        Timer timer = new Timer(300, e -> {

            writeGuiMessageToChatPanel();
            scrollMainPanelDownToLastMessage(this.mainFrame.getMainTextBackgroundScrollPane());
        });
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * This method triggers the repainting of the main frame, refreshing the graphical user
     * interface. It revalidates and repaints the main frame to apply any changes made to its
     * components.
     */
    private void repaintMainFrame() {

        if (mainFrame instanceof JFrame jFrame) {

            jFrame.revalidate();
            jFrame.repaint();
        }
    }

    /**
     * Checks if the message sender is already registered in the local cache. If not, it adds the
     * sender to the cache with default properties.
     * 
     * @param clientMap the map representing the local cache of message senders
     * @param sender the sender to be checked and possibly added to the cache
     */
    private void checkIfMessageSenderAlreadyRegisteredInLocalCache(
            HashMap<String, CustomUserPropertiesDTO> clientMap, String sender) {

        if (sender.equals(fetchUsernameFromCache())) {

            if (!checkIfSenderIsRegistered("own")) {

                addClientToLocalCacheRegister(clientMap, "own");
            }

            return;
        }

        if (!clientMap.containsKey(sender)) {

            addClientToLocalCacheRegister(clientMap, sender);
        }
    }

    /**
     * Adds a client to the local cache register.
     * 
     * @param clientMap the map representing the local cache of clients
     * @param sender the client to be added to the cache
     */
    private void addClientToLocalCacheRegister(
            final HashMap<String, CustomUserPropertiesDTO> clientMap, final String sender) {

        String username;
        String nickname;

        if (clientMap.containsKey(sender)) {

            final CustomUserPropertiesDTO userPropertiesDTO = clientMap.get(sender);
            username = userPropertiesDTO.username();
            nickname = retrieveNicknameFromUserPropertiesDTO(userPropertiesDTO);

        } else {

            username = sender;
            nickname = null;
        }

        // 1) statusArray

        final boolean senderIsThisClient = checkIfSenderIsThisClient(username);

        if (senderIsThisClient) {

            username = fetchUsernameFromCache();
        }

        // 2) nickname and 3) border color
        final String borderColor = String.valueOf(getRandomRgbIntValue());

        clientMap.put(sender, new CustomUserPropertiesDTO(username, nickname, borderColor));
    }

    /**
     * Retrieves the nickname from the given CustomUserPropertiesDTO object.
     * 
     * @param userPropertiesDTO the CustomUserPropertiesDTO object from which to retrieve the
     *        nickname
     * 
     * @return the nickname retrieved from the CustomUserPropertiesDTO object, or null if the object
     *         is null
     */
    private String retrieveNicknameFromUserPropertiesDTO(
            final CustomUserPropertiesDTO userPropertiesDTO) {

        if (userPropertiesDTO == null) {

            return null;
        }
        if (userPropertiesDTO.nickname() != null && !userPropertiesDTO.nickname().isEmpty()) {

            return userPropertiesDTO.nickname();
        }
        return null;
    }

    /**
     * Retrieves the username from the cache.
     * 
     * @return the username retrieved from the cache, or null if it does not exist
     */
    private String fetchUsernameFromCache() {

        return this.mainFrame.getUsername();
    }

    /**
     * Checks whether the given username belongs to the current client.
     * 
     * @param username the username to be checked
     * 
     * @return true if the username belongs to the current client, false otherwise
     */
    private boolean checkIfSenderIsThisClient(final String username) {

        return username.equals("own");
    }

    /**
     * Checks if the given sender is registered.
     * 
     * @param sender the sender to check
     * 
     * @return true if the sender is registered, false otherwise
     */
    private boolean checkIfSenderIsRegistered(final String sender) {

        return this.mainFrame.getChatClientPropertiesHashMap().containsKey(sender);
    }

    /**
     * Scrolls the main panel down to the last message in the scroll pane.
     * <p>
     * This method updates the main frame, repaints it, and scrolls the vertical scroll bar to its
     * maximum position to display the last message.
     * <p>
     * Implements MainFrame Revalidate and Repaint as well.
     * 
     * @param scrollPane the scroll pane containing the main panel
     */
    private void scrollMainPanelDownToLastMessage(JScrollPane scrollPane) {

        final int scrollBarMaxValue = getMaximumVerticalScrollbarValue(scrollPane);

        repaintMainFrame();

        SwingUtilities.invokeLater(() -> {

            scrollPane.getVerticalScrollBar().setValue(scrollBarMaxValue);
        });
    }

    private int getMaximumVerticalScrollbarValue(final JScrollPane scrollPane) {

        return scrollPane.getVerticalScrollBar().getMaximum();
    }

}
