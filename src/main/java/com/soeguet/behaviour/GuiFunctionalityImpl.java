package com.soeguet.behaviour;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.behaviour.interfaces.GuiFunctionality;
import com.soeguet.behaviour.interfaces.SocketToGuiInterface;
import com.soeguet.cache.factory.CacheManagerFactory;
import com.soeguet.cache.implementations.ActiveNotificationQueue;
import com.soeguet.cache.implementations.MessageQueue;
import com.soeguet.cache.implementations.WaitingNotificationQueue;
import com.soeguet.cache.manager.CacheManager;
import com.soeguet.gui.comments.interfaces.CommentManager;
import com.soeguet.gui.comments.util.LinkWrapEditorKit;
import com.soeguet.gui.comments.util.WrapEditorKit;
import com.soeguet.gui.image_panel.ImagePanelImpl;
import com.soeguet.gui.image_panel.interfaces.ImageInterface;
import com.soeguet.gui.main_frame.interfaces.MainFrameInterface;
import com.soeguet.gui.notification_panel.NotificationImpl;
import com.soeguet.gui.notification_panel.interfaces.NotificationInterface;
import com.soeguet.gui.option_pane.links.LinkDialogHandler;
import com.soeguet.gui.option_pane.links.LinkDialogImpl;
import com.soeguet.gui.option_pane.links.dtos.MetadataStorageRecord;
import com.soeguet.gui.option_pane.links.interfaces.LinkDialogInterface;
import com.soeguet.gui.popups.PopupPanelImpl;
import com.soeguet.gui.popups.interfaces.PopupInterface;
import com.soeguet.model.MessageTypes;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.MessageModel;
import com.soeguet.model.jackson.PictureModel;
import com.soeguet.properties.CustomUserProperties;
import com.soeguet.util.MessageCategory;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;

/**
 This class provides additional functionality to the GUI.

 Implements the SocketToGuiInterface for receiving messages from the socket.
 */
public class GuiFunctionalityImpl implements GuiFunctionality, SocketToGuiInterface {

    private final MainFrameInterface mainFrame;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CommentManager commentManager;
    Logger LOGGER = Logger.getLogger(GuiFunctionalityImpl.class.getName());
    CacheManager cacheManager = CacheManagerFactory.getCacheManager();

    /**
     Constructor for the GuiFunctionality class.
     Initializes the mainFrame and commentManager properties.

     @param mainFrame      The main frame of the GUI.
     @param commentManager The implementation of the CommentManager interface.
     */
    public GuiFunctionalityImpl(MainFrameInterface mainFrame, final CommentManager commentManager) {

        this.mainFrame = mainFrame;
        this.commentManager = commentManager;
    }

    /**
     Generates a random RGB integer value.

     This method creates a new instance of the Random class to generate random values for the red, green,
     and blue components of the RGB color. It then creates a new Color object using the generated values
     and retrieves the RGB integer value using the getRGB() method.

     @return the random RGB integer value
     */
    private int getRandomRgbIntValue() {

        Random rand = new Random();
        int r = rand.nextInt(256);
        int g = rand.nextInt(256);
        int b = rand.nextInt(256);
        return new Color(r, g, b).getRGB();
    }

    /**
     Fixes the scroll speed for the main text background scroll pane.
     This method sets the unit increment of the vertical scrollbar of the main text background scroll pane to 20.
     This ensures that the scrolling speed is faster.
     */
    @Override
    public void fixScrollPaneScrollSpeed() {

        this.mainFrame.getMainTextBackgroundScrollPane().getVerticalScrollBar().setUnitIncrement(25);
    }

    /**
     Overrides the transfer handler of the text pane in the GUI.
     This method is responsible for handling dropped data onto the text pane,
     distinguishing between image and text data and triggering the appropriate actions.

     The original transfer handler is preserved before overriding it with a new transfer handler.
     */
    @Override
    public void overrideTransferHandlerOfTextPane() {

        //preserve the original transfer handler, otherwise clunky behavior
        TransferHandler originalHandler = this.mainFrame.getTextEditorPane().getTransferHandler();

        this.mainFrame.getTextEditorPane().setTransferHandler(new TransferHandler() {

            @Override
            public boolean importData(JComponent jComponent, Transferable transferable) {

                if (transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {

                    // image to text pane -> activate image panel
                    ImageInterface imagePanel = new ImagePanelImpl(mainFrame);
                    imagePanel.setPosition();
                    imagePanel.setLayeredPaneLayerPositions();
                    imagePanel.setupPictureScrollPaneScrollSpeed();
                    imagePanel.populateImagePanelFromClipboard();

                    return true;

                } else if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {

                    try {
                        String data = (String) transferable.getTransferData(DataFlavor.stringFlavor);

                        //TODO maybe emoji detection?

                        if (data.startsWith("http://") || data.startsWith("https://")) {

                            //send the link
                            callLinkConfirmationDialog(data);

                            return false;

                        } else {

                            return originalHandler.importData(jComponent, transferable);
                        }

                    } catch (UnsupportedFlavorException | IOException e) {

                        LOGGER.log(java.util.logging.Level.SEVERE, "Error importing data", e);
                    }

                    return originalHandler.importData(jComponent, transferable);
                }
                return false;
            }
        });
    }

    private void callLinkConfirmationDialog(String link) {

        //TODO clickable link working, maybe add some kind of JPanel for further editing

        final LinkDialogInterface linkDialog = createLinkDialog(link);
        linkDialog.generate();

//        MessageModel messageModel = new MessageModel((byte) MessageTypes.LINK, mainFrame.getUsername(), data);
//        String messageString = convertToJSON(messageModel);
//        sendMessageToSocket(messageString);
//        mainFrame.getTextEditorPane().setText("");
    }

    private LinkDialogInterface createLinkDialog(final String link) {

        final LinkDialogInterface linkDialog = initializeLinkDialog(link);

        MetadataStorageRecord metadataStorageRecord = linkDialog.checkForMetaData(link);

        if (metadataStorageRecord != null) {

            JPanel metaDataPanel = linkDialog.createMetadataPanel(metadataStorageRecord);
            linkDialog.getContentPanel().add(metaDataPanel, BorderLayout.CENTER);
        }

        return linkDialog;
    }

    private LinkDialogInterface initializeLinkDialog(final String link) {

        LinkDialogInterface linkDialog = new LinkDialogImpl(mainFrame, new LinkDialogHandler());

        linkDialog.getLinkTextPane().setEditorKit(new LinkWrapEditorKit());
        linkDialog.getLinkTextPane().setText(link);
        linkDialog.getCommentTextPane().setEditorKit(new WrapEditorKit());
        return linkDialog;
    }

    @Override
    public void clearTextPaneAndSendMessageToSocket() {

        String userTextInput = getTextFromInput();
        clearTextPane();
        String messageString = convertUserTextToJSON(userTextInput);
        sendMessageToSocket(messageString);

    }

    private String getTextFromInput() {

        return this.mainFrame.getTextEditorPane().getText();
    }

    private void clearTextPane() {

        this.mainFrame.getTextEditorPane().setText("");
    }

    private String convertUserTextToJSON(String userTextInput) {

        return convertToJSON(textToMessageModel(userTextInput));
    }

    private void sendMessageToSocket(String messageString) {

        this.mainFrame.getWebsocketClient().send(messageString);

        final String typingStatus = "{\"type\":\"send\"}";

        this.mainFrame.getWebsocketClient().send(typingStatus.getBytes());
    }

    private String convertToJSON(BaseModel messageModel) {

        try {

            return this.mainFrame.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(messageModel);

        } catch (JsonProcessingException e) {

            LOGGER.log(java.util.logging.Level.SEVERE, "Error converting to JSON", e);
        }

        return null;
    }

    /**
     Converts a user input text into a MessageModel object.

     @param userTextInput The text entered by the user.

     @return A MessageModel object representing the user input.
     */
    private MessageModel textToMessageModel(String userTextInput) {

        return new MessageModel((byte) MessageTypes.NORMAL, mainFrame.getUsername(), userTextInput);
    }

    @Override
    public synchronized void internalNotificationHandling(String message) {

        //TODO clean this mess up..
        BaseModel baseModel = convertMessageToBaseModel(message);
        internalNotificationHandling(message, baseModel);
    }

    /**
     Called when a message is received.

     @param message The message received.
     */
    @Override
    public void onMessage(String message) {

        MessageQueue messageQueue = (MessageQueue) cacheManager.getCache("messageQueue");

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

                //add to queue
                messageQueue.addLast(message);

                //write to pane
                spamBuffer();

                //notification
                createDesktopNotification(message);
            }
        }
    }

    @Override
    public void onMessage(byte[] message) {

        //TODO clean this up

        final JLabel typingLabel = mainFrame.getTypingLabel();

        final JsonNode parsedJson;

        try {

            parsedJson = mainFrame.getObjectMapper().readTree(message);

        } catch (IOException e) {

            throw new RuntimeException(e);
        }

        final String eventType = parsedJson.get("type").asText();

        switch (eventType) {

            case "typing" -> {

                final String username = parsedJson.get("username").asText();

                String textOnTypingLabel = typingLabel.getText();

                if (textOnTypingLabel.contains(username)) {
                    return;
                }

                StringBuilder stringBuilder = new StringBuilder();

                if (!textOnTypingLabel.isBlank()) {

                    textOnTypingLabel = textOnTypingLabel.replace(" is typing...", "");
                    stringBuilder.append(textOnTypingLabel);
                    stringBuilder.append(", ");

                } else {

                    stringBuilder.append("  ");
                }

                stringBuilder.append(username);
                stringBuilder.append(" is typing...");

                SwingUtilities.invokeLater(() -> typingLabel.setText(stringBuilder.toString()));
            }

            case "send" -> typingLabel.setText(" ");
        }

    }

    private void spamBuffer() {

        //TODO this does not work at all :( intention: prevent spamming the GUI and making use of a timeout system if messages are flooded in
        //using time to prevent spamming the GUI and making use of the cached messages

//        if (messageToPanelTimer != null &&  messageToPanelTimer.isRunning()) {
//
//            return;
//        }
//
//        messageToPanelTimer = new Timer(1000, e -> writeGuiMessageToChatPanel());
//        messageToPanelTimer.setRepeats(false);
//        messageToPanelTimer.start();

        writeGuiMessageToChatPanel();

        //TODO remove this part as soon as the buffer is working
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void createDesktopNotification(final String message) {

        //TODO clean this up

        //convert the message to a java object and return if the message came from this client
        final BaseModel baseModel = convertMessageToBaseModel(message);
        if (baseModel.getSender().equals(this.mainFrame.getUsername())) {
            return;
        }

        //check if notifications are even wanted
        switch (this.mainFrame.getNotificationStatus()) {

            case INTERNAL_ONLY -> {

                internalNotificationHandling(message);
                Toolkit.getDefaultToolkit().beep();
            }

            case EXTERNAL_ONLY -> {

                externalNotificationHandling(message);
                Toolkit.getDefaultToolkit().beep();
            }

            case ALL_ALLOWED -> {

                internalNotificationHandling(message);
                externalNotificationHandling(message);
                Toolkit.getDefaultToolkit().beep();
            }

            case ALL_DENIED, STARTUP -> {
                //TODO check if message was even cached which in that case needs to be removed from cache
                //do nothing, intentionally left blank
                break;
            }
        }
    }

    private void externalNotificationHandling(final String message) {

        try {

            final BaseModel baseModel = convertJsonToMessageModel(message);

            switch (baseModel) {

                case MessageModel text -> {

                    try {

                        //TODO linux only // windows needed
                        Runtime.getRuntime().exec(new String[]{"notify-send", "text message", text.getMessage()});

                    } catch (IOException e) {

                        throw new RuntimeException(e);
                    }
                }

                case PictureModel picture -> {

                    try {

                        Runtime.getRuntime().exec(new String[]{"notify-send", "picture message", "[picture]" + System.lineSeparator() + picture.getMessage()});

                    } catch (IOException e) {

                        throw new RuntimeException(e);
                    }
                }
            }

        } catch (JsonProcessingException e) {

            throw new RuntimeException(e);
        }
    }

    public synchronized void internalNotificationHandling(String message, BaseModel baseModel) {

        //TODO clean this mess up..
        if (cacheManager.getCache("ActiveNotificationQueue") instanceof ActiveNotificationQueue activeNotificationQueue) {

            //max 3 notification at a time, cache message and bail out
            if (activeNotificationQueue.getRemainingCapacity() < 1) {

                if (cacheManager.getCache("WaitingNotificationQueue") instanceof WaitingNotificationQueue waitingNotificationQueue) {

                    //add to queue and skip the rest
                    waitingNotificationQueue.addLast(message);

                }

            } else {

                try {

                    activeNotificationQueue.addLast(baseModel);
                    createNotification(baseModel);

                } catch (IllegalStateException e) {

                    throw new RuntimeException(e);
                }

                handleRemainingCapacityInQueue(activeNotificationQueue);
            }
        }
    }

    private void handleRemainingCapacityInQueue(ActiveNotificationQueue activeNotificationsCache) {

        WaitingNotificationQueue waitingNotificationsCache = (WaitingNotificationQueue) cacheManager.getCache("WaitingNotificationQueue");

        if (activeNotificationsCache.getRemainingCapacity() > 0 && !waitingNotificationsCache.isEmpty()) {

            final String queuedNotification = waitingNotificationsCache.pollFirst();

            //TODO is this one right?
            Timer timer = new Timer(250, e -> internalNotificationHandling(queuedNotification));
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void createNotification(final BaseModel baseModel) {

        switch (baseModel) {

            case MessageModel text -> {

                Timer timer = new Timer(500, e -> {
                    NotificationInterface notification = new NotificationImpl(this.mainFrame, text);
                    notification.setNotificationText();
                    notification.setMaximumSize(new Dimension(400, 300));
                });
                timer.setRepeats(false);
                timer.start();
            }

            case PictureModel picture -> {

                Timer timer = new Timer(500, e -> {
                    NotificationInterface notification = new NotificationImpl(this.mainFrame, picture);

                    notification.setNotificationPicture();
                    notification.setMaximumSize(new Dimension(400, 300));

                });
                timer.setRepeats(false);
                timer.start();
            }

            default -> LOGGER.info("Unknown message type");
        }
    }

    /**
     This method writes a GUI message to the chat panel and handles all the setup.
     */
    private synchronized void writeGuiMessageToChatPanel() {

        //retrieve the message from cache
        final String message = pollMessageFromCache();
        if (message == null) return;

        //convert message to java object
        BaseModel baseModel = convertMessageToBaseModel(message);

        //TODO user name checkup seems off
        //register user from message to local cache if not present yet
        checkIfMessageSenderAlreadyRegisteredInLocalCache(mainFrame.getChatClientPropertiesHashMap(), baseModel.getSender());

        //handle displayed message name - nickname as well as username
        String nickname = checkForNickname(baseModel.getSender());

        //process and display message
        processAndDisplayMessage(baseModel, nickname);

        //check for remaining messages in the local cache
        checkIfDequeIsEmptyOrStartOver();
    }

    /**
     This method retrieves and removes the first message from the message queue.

     @return The first message in the message queue, or null if the queue is empty.
     */
    private String pollMessageFromCache() {

        MessageQueue messageQueue = (MessageQueue) cacheManager.getCache("messageQueue");

        return messageQueue.pollFirst();
    }

    /**
     This method processes and displays a message based on the message model, username, and nickname.

     @param baseModel The message model representing the message.
     @param nickname  The nickname of the client.
     */
    private void processAndDisplayMessage(final BaseModel baseModel, final String nickname) {

        int messageCategory = commentManager.categorizeMessageFromSocket(baseModel);

        switch (messageCategory) {

            case MessageCategory.RIGHT_SIDE_TEXT_MESSAGE -> commentManager.setupMessagesRightSide(baseModel, nickname);

            case MessageCategory.RIGHT_SIDE_PICTURE_MESSAGE -> commentManager.setupPicturesRightSide(baseModel, nickname);

            case MessageCategory.RIGHT_SIDE_LINK_MESSAGE -> commentManager.setupLinkRightSite(baseModel);

            case MessageCategory.LEFT_SIDE_TEXT_MESSAGE -> commentManager.setupMessagesLeftSide(baseModel, nickname);

            case MessageCategory.LEFT_SIDE_PICTURE_MESSAGE -> commentManager.setupPicturesLeftSide(baseModel, nickname);

            case MessageCategory.LEFT_SIDE_LINK_MESSAGE -> commentManager.setupLinkLeftSide(baseModel);
        }
    }

    private void checkIfDequeIsEmptyOrStartOver() {

        MessageQueue messageQueue = (MessageQueue) cacheManager.getCache("messageQueue");

        repaintMainFrame();

        SwingUtilities.invokeLater(() -> scrollMainPanelDownToLastMessage(this.mainFrame.getMainTextBackgroundScrollPane()));

        if (!messageQueue.isEmpty()) {

            Timer timer = new Timer(300, e -> {

                writeGuiMessageToChatPanel();
                scrollMainPanelDownToLastMessage(this.mainFrame.getMainTextBackgroundScrollPane());
            });
            timer.setRepeats(false);
            timer.start();
        }

    }

    /**
     This method triggers the repainting of the main frame, refreshing the graphical user interface.
     It revalidates and repaints the main frame to apply any changes made to its components.
     */
    private void repaintMainFrame() {

        if (mainFrame instanceof JFrame jFrame) {

            jFrame.revalidate();
            jFrame.repaint();
        }
    }

    /**
     Converts a JSON string to a MessageModel object.

     @param message the JSON string to be converted

     @return the MessageModel object representing the converted JSON string

     @throws IllegalArgumentException if the JSON string is malformed
     */
    private BaseModel convertMessageToBaseModel(String message) {

        try {

            return convertJsonToMessageModel(message);

        } catch (JsonProcessingException e) {

            throw new IllegalArgumentException("Malformed message", e);
        }
    }

    /**
     Checks if the message sender is already registered in the local cache.
     If not, it adds the sender to the cache with default properties.

     @param clientMap the map representing the local cache of message senders
     @param sender    the sender to be checked and possibly added to the cache
     */
    private void checkIfMessageSenderAlreadyRegisteredInLocalCache(HashMap<String, CustomUserProperties> clientMap, String sender) {

        if (sender.equals(this.mainFrame.getUsername())) {

            if (!this.mainFrame.getChatClientPropertiesHashMap().containsKey("own")) {

                CustomUserProperties customUserProperties = new CustomUserProperties();
                customUserProperties.setUsername(this.mainFrame.getUsername());
                customUserProperties.setBorderColor(getRandomRgbIntValue());
                clientMap.put("own", customUserProperties);
            }

            return;
        }

        if (!clientMap.containsKey(sender)) {

            CustomUserProperties customUserProperties = new CustomUserProperties();
            customUserProperties.setUsername(sender);
            customUserProperties.setBorderColor(getRandomRgbIntValue());
            clientMap.put(sender, customUserProperties);
        }
    }

    private String checkForNickname(String sender) {

        if (this.mainFrame.getChatClientPropertiesHashMap().containsKey(sender) && this.mainFrame.getChatClientPropertiesHashMap().get(sender).getNickname() != null && !this.mainFrame.getChatClientPropertiesHashMap().get(sender).getNickname().isEmpty()) {

            return this.mainFrame.getChatClientPropertiesHashMap().get(sender).getNickname();
        }

        return null;
    }

    /**
     Scrolls the main panel down to the last message in the scroll pane.

     This method updates the main frame, repaints it, and scrolls the vertical scroll bar
     to its maximum position in order to display the last message.

     @param scrollPane the scroll pane containing the main panel
     */
    public void scrollMainPanelDownToLastMessage(JScrollPane scrollPane) {

        //EDT check done!
        repaintMainFrame();
        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
    }

    /**
     Converts a JSON message into a MessageModel object.

     @param jsonMessage the JSON message to be converted

     @return the converted MessageModel object

     @throws JsonProcessingException if there is an error processing the JSON message
     */
    private BaseModel convertJsonToMessageModel(String jsonMessage) throws JsonProcessingException {

        return this.objectMapper.readValue(jsonMessage, BaseModel.class);
    }
}