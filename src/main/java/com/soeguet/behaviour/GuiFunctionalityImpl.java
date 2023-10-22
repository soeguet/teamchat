package com.soeguet.behaviour;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.behaviour.interfaces.GuiFunctionality;
import com.soeguet.behaviour.interfaces.SocketToGuiInterface;
import com.soeguet.cache.factory.CacheManagerFactory;
import com.soeguet.cache.implementations.MessageQueue;
import com.soeguet.cache.manager.CacheManager;
import com.soeguet.dtos.StatusTransferDTO;
import com.soeguet.gui.comments.CommentManagerImpl;
import com.soeguet.gui.comments.interfaces.CommentManager;
import com.soeguet.gui.comments.util.WrapEditorKit;
import com.soeguet.gui.image_panel.ImagePanelImpl;
import com.soeguet.gui.image_panel.interfaces.ImageInterface;
import com.soeguet.gui.interrupt_dialog.handler.InterruptHandler;
import com.soeguet.gui.interrupt_dialog.interfaces.InterruptHandlerInterface;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.gui.main_panel.MessageDisplayHandler;
import com.soeguet.gui.main_panel.interfaces.MessageDisplayHandlerInterface;
import com.soeguet.gui.notification_panel.DesktopNotificationHandler;
import com.soeguet.gui.notification_panel.interfaces.DesktopNotificationHandlerInterface;
import com.soeguet.gui.option_pane.links.LinkDialogHandler;
import com.soeguet.gui.option_pane.links.LinkDialogImpl;
import com.soeguet.gui.option_pane.links.dtos.MetadataStorageRecord;
import com.soeguet.gui.option_pane.links.interfaces.LinkDialogInterface;
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

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;

/**
 This class provides additional functionality to the GUI.
 <p>
 Implements the SocketToGuiInterface for receiving messages from the socket.
 */
public class GuiFunctionalityImpl implements GuiFunctionality, SocketToGuiInterface {

    private final MainFrameGuiInterface mainFrame;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger = Logger.getLogger(GuiFunctionalityImpl.class.getName());
    CacheManager cacheManager = CacheManagerFactory.getCacheManager();

    /**
     Constructor for the GuiFunctionalityImpl class.

     @param mainFrame
     the MainFrameGuiInterface object used to interact with the main frame GUI.
     */
    public GuiFunctionalityImpl(MainFrameGuiInterface mainFrame) {

        this.mainFrame = mainFrame;
    }

    /**
     Generates a random RGB integer value.
     <p>
     This method creates a new instance of the Random class to generate random values for the red, green, and blue components of the RGB color. It
     then
     creates a new Color object using the generated values and retrieves the RGB integer value using the getRGB() method.

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
     Fixes the scroll speed for the main text background scroll pane. This method sets the unit increment of the vertical scrollbar of the main text
     background scroll pane to 20. This ensures that the scrolling speed is faster.
     */
    @Override
    public void fixScrollPaneScrollSpeed() {

        this.mainFrame.getMainTextBackgroundScrollPane().getVerticalScrollBar().setUnitIncrement(25);
    }

    /**
     Overrides the transfer handler of the text pane in the GUI. This method is responsible for handling dropped data onto the text pane,
     distinguishing between image and text data and triggering the appropriate actions.
     <p>
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

                    //image route

                    // image to text pane -> activate image panel
                    callImagePanel();

                    return true;

                } else if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {

                    //string route

                    String data = "";

                    try {

                        data = (String) transferable.getTransferData(DataFlavor.stringFlavor);

                    } catch (UnsupportedFlavorException |
                             IOException e) {

                        logger.log(java.util.logging.Level.SEVERE, "Error importing data", e);
                        throw new RuntimeException(e);
                    }

                    //FEATURE maybe emoji detection?

                    //check if link
                    if (data.startsWith("http://") || data.startsWith("https://")) {

                        //send the link
                        final String finalData = data;

                        SwingUtilities.invokeLater(() -> {

                            callLinkConfirmationDialog(finalData);
                        });

                        //don't append the link to the text pane
                        return false;
                    }

                    //append what was pasted to the text pane
                    return originalHandler.importData(jComponent, transferable);
                }

                return originalHandler.importData(jComponent, transferable);
            }
        });
    }

    /**
     Calls the image panel to handle image data dropped onto the text pane. The image panel is responsible for displaying and manipulating the image
     data.
     */
    private void callImagePanel() {

        ImageInterface imagePanel = new ImagePanelImpl(mainFrame);

        imagePanel.setPosition();
        imagePanel.setLayeredPaneLayerPositions();
        imagePanel.setupPictureScrollPaneScrollSpeed();
        imagePanel.populateImagePanelFromClipboard();
    }

    /**
     Calls the link confirmation dialog for the given link.
     <p>
     This method is responsible for displaying a link confirmation dialog with the given link.

     @param link
     the link to be confirmed
     */
    private void callLinkConfirmationDialog(String link) {

        final LinkDialogInterface linkDialog = createLinkDialog(link);
        linkDialog.generate();
    }

    /**
     Creates a link confirmation dialog for the given link.
     <p>
     This method is responsible for creating and initializing a link confirmation dialog with the given link. If metadata for the link exists, it adds
     a metadata panel to the dialog's content panel.

     @param link
     the link to be confirmed

     @return the initialized link dialog
     */
    private LinkDialogInterface createLinkDialog(final String link) {

        final LinkDialogInterface linkDialog = initializeLinkDialog(link);

        MetadataStorageRecord metadataStorageRecord = linkDialog.checkForMetaData(link);

        if (metadataStorageRecord != null) {

            JPanel metaDataPanel = linkDialog.createMetadataPanel(metadataStorageRecord);

            if (metaDataPanel != null) {

                linkDialog.getContentPanel().add(metaDataPanel, BorderLayout.CENTER);
            }
        }

        return linkDialog;
    }

    /**
     Creates and initializes a link confirmation dialog with the given link.
     <p>
     This method creates a new instance of the LinkDialogImpl class and initializes it by setting the editor kits for the link and comment text panes.
     The link text pane is populated with the given link.

     @param link
     the link to be confirmed

     @return the initialized link dialog
     */
    private LinkDialogInterface initializeLinkDialog(final String link) {

        LinkDialogInterface linkDialog = new LinkDialogImpl(mainFrame, new LinkDialogHandler());

        linkDialog.getLinkTextPane().setEditorKit(new WrapEditorKit());
        linkDialog.getLinkTextPane().setText(link);
        linkDialog.getCommentTextPane().setEditorKit(new WrapEditorKit());

        return linkDialog;
    }

    /**
     Sends a message to the socket.
     <p>
     This method retrieves text input from the user, converts it to JSON format, and sends the message to the socket using the
     sendMessageToSocket(String) method.
     <p>
     The user input is retrieved using the getTextFromInput() method. The retrieved text is then converted to JSON format using the
     convertUserTextToJSON(String) method. Finally, the message is sent to the socket using the sendMessageToSocket(String) method.

     @see #getTextFromInput()
     @see #convertUserTextToJSON(String)
     @see #sendMessageToSocket(String)
     */
    @Override
    public void sendMessageToSocket() {

        String userTextInput = getTextFromInput();
        String messageString = convertUserTextToJSON(userTextInput);
        sendMessageToSocket(messageString);
    }

    /**
     Retrieves the text from the input text editor pane.
     <p>
     This method retrieves the text from the input text editor pane of the main frame.

     @return the text obtained from the input text editor pane
     */
    private String getTextFromInput() {

        return this.mainFrame.getTextEditorPane().getText();
    }

    /**
     Converts user text input to JSON representation.
     <p>
     This method takes the user text input and converts it to a MessageModel object using the textToMessageModel method. It then converts the
     MessageModel object to its JSON representation using the convertToJSON method.

     @param userTextInput
     the user text input to be converted to JSON

     @return the JSON representation of the user text input
     */
    private String convertUserTextToJSON(String userTextInput) {

        return convertToJSON(textToMessageModel(userTextInput));
    }

    /**
     Sends a message to the socket.
     <p>
     This method sends the given message string to the websocket client associated with the main frame. Additionally, it sends a typing status message
     to indicate that a message is being sent.

     @param messageString
     the message to be sent
     */
    private void sendMessageToSocket(String messageString) {

        final CustomWebsocketClient websocketClient = this.mainFrame.getWebsocketClient();

        //normal message
        websocketClient.send(messageString);

        //sending status
        try {

            StatusTransferDTO statusTransferDTO = new StatusTransferDTO("send");
            websocketClient.send(objectMapper.writeValueAsBytes(statusTransferDTO));

        } catch (JsonProcessingException e) {

            throw new RuntimeException(e);
        }
    }

    /**
     Converts the given BaseModel object to JSON string.
     <p>
     This method uses the Jackson ObjectMapper to convert the BaseModel object to JSON. It uses the writerWithDefaultPrettyPrinter() method to format
     the JSON string with default pretty printer settings.

     @param messageModel
     the BaseModel object to convert to JSON

     @return the JSON string representation of the messageModel object, or null if an error occurs
     */
    private String convertToJSON(BaseModel messageModel) {

        try {

            return this.mainFrame.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(messageModel);

        } catch (JsonProcessingException e) {

            logger.log(java.util.logging.Level.SEVERE, "Error converting to JSON", e);
            throw new RuntimeException(e);
        }
    }

    /**
     Converts a user input text into a MessageModel object.

     @param userTextInput
     The text entered by the user.

     @return A MessageModel object representing the user input.
     */
    private MessageModel textToMessageModel(String userTextInput) {

        return new MessageModel((byte) MessageTypes.NORMAL, mainFrame.getUsername(), userTextInput);
    }

    /**
     Clears the text in the text editor pane.
     <p>
     This method sets the text in the text editor pane to an empty string, effectively clearing it.
     */
    public void clearTextPane() {

        this.mainFrame.getTextEditorPane().setText("");
    }

    @Override
    public void internalNotificationHandling(final String message) {

        DesktopNotificationHandlerInterface desktopNotificationHandler = new DesktopNotificationHandler(mainFrame);
        final NotificationStatus notificationStatus = desktopNotificationHandler.determineDesktopNotificationStatus();
        desktopNotificationHandler.createDesktopNotification(message, notificationStatus);
    }

    /**
     Called when a message is received.

     @param message
     The message received.
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

                //add to queue
                if (cacheManager.getCache("messageQueue") instanceof MessageQueue messageQueue) {

                    messageQueue.addLast(message);
                }

                //write to pane
                spamBuffer();

                //notification
                createDesktopNotification(message);
            }
        }
    }

    /**
     Called when a message is received.

     @param message
     The message received as a byte array.
     */
    @Override
    public void onMessage(byte[] message) {

        final JsonNode parsedJson = parseJsonNode(message);

        StatusTransferDTO statusTransferDTO = objectMapper.convertValue(parsedJson, StatusTransferDTO.class);

        switch (statusTransferDTO.type()) {

            case "typing" -> handleTypingPanel(statusTransferDTO);

            case "send" -> mainFrame.getTypingLabel().setText(" ");

            case "interrupt" -> handleInterruption(statusTransferDTO);

            default -> throw new IllegalStateException();
        }
    }

    /**
     Parses a JSON node from a byte array.

     @param message
     The message as a byte array.

     @return The parsed JSON node.
     */
    private JsonNode parseJsonNode(final byte[] message) {

        try {

            return mainFrame.getObjectMapper().readTree(message);

        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }

    /**
     Handles the typing panel based on the typing status received.

     @param typingStatus
     The typing status information.
     */
    private void handleTypingPanel(StatusTransferDTO typingStatus) {

        TypingPanelHandlerInterface typingPanelHandler = new TypingPanelHandler(mainFrame);

        String textOnTypingLabel = typingPanelHandler.retrieveTextOnTypingLabel();

        //if typing client is already present on label -> return!
        //typingStatus.statusArray()[0] array with only one value, always!
        final String typingUsername = typingStatus.statusArray()[0];

        if (textOnTypingLabel.contains(typingUsername)) {

            return;
        }

        final StringBuilder stringBuilder = typingPanelHandler.generateTypingLabel(textOnTypingLabel, typingUsername);

        typingPanelHandler.displayUpdatedTypingLabel(stringBuilder);
    }

    /**
     Handles the interruption based on the client interruption status received.

     @param clientInterruptDTO
     The interruption status information.
     */
    private void handleInterruption(StatusTransferDTO clientInterruptDTO) {

        InterruptHandlerInterface interruptHandler = new InterruptHandler(mainFrame);

        Arrays.stream(clientInterruptDTO.statusArray()).forEach(interruptHandler::forceChatGuiToFront);
    }

    /**
     Spams the buffer with a message to be displayed in the GUI chat panel.
     <p>
     This method writes a message to the chat panel and then adds a brief delay before proceeding. It is recommended to replace this method with a
     more
     efficient buffering or caching system in the future.

     @throws RuntimeException
     if there is an InterruptedException during the delay.
     */
    private void spamBuffer() {

        writeGuiMessageToChatPanel();

        //TODO replace this with a buffer or caching system
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     Creates a desktop notification based on the provided message.

     @param message
     The message to be displayed in the notification.

     @throws RuntimeException
     if there is an error while handling the notification.
     */
    private void createDesktopNotification(final String message) {

        //convert the message to a java object and return if the message came from this client
        final BaseModel baseModel = parseMessageToJsonModel(message);

        //return if the message was sent by this client (no notification for own messages)
        if (compareSenderToUsername(baseModel)) {

            return;
        }

        DesktopNotificationHandlerInterface desktopNotificationHandler = new DesktopNotificationHandler(mainFrame);
        NotificationStatus notificationStatus = desktopNotificationHandler.determineDesktopNotificationStatus();
        desktopNotificationHandler.createDesktopNotification(message, notificationStatus);
    }

    /**
     Compares the sender of a BaseModel object to the username of mainFrame.

     @param baseModel
     The BaseModel object to compare the sender to.

     @return true if the sender of the baseModel object is equal to the username of mainFrame; false otherwise.
     */
    private boolean compareSenderToUsername(final BaseModel baseModel) {

        return baseModel.getSender().equals(this.mainFrame.getUsername());
    }

    /**
     This method writes a GUI message to the chat panel and handles all the setup.
     */
    private synchronized void writeGuiMessageToChatPanel() {

        CommentManager commentManager = new CommentManagerImpl(mainFrame);
        MessageDisplayHandlerInterface messageDisplayHandler = new MessageDisplayHandler(mainFrame, commentManager);
        messageDisplayHandler.setCacheManager(cacheManager);

        //retrieve the message from cache
        final String message = messageDisplayHandler.pollMessageFromCache();
        if (message == null) {return;}

        //convert message to java object
        final BaseModel baseModel = parseMessageToJsonModel(message);

        //register user from message to local cache if not present yet
        checkIfMessageSenderAlreadyRegisteredInLocalCache(mainFrame.getChatClientPropertiesHashMap(), baseModel.getSender());

        //handle displayed message name - nickname as well as username
        String nickname = checkForNickname(baseModel.getSender());

        //process and display message
        messageDisplayHandler.processAndDisplayMessage(baseModel, nickname);

        //check for remaining messages in the local cache
        checkIfDequeIsEmptyOrStartOver();
    }

    private BaseModel parseMessageToJsonModel(final String message) {

        try {

            return objectMapper.readValue(message, BaseModel.class);

        } catch (JsonProcessingException e) {

            throw new RuntimeException(e);
        }
    }

    /**
     Checks if the message queue is empty or if the chat needs to start over. If the message queue is not empty, it sets a timer for displaying new
     messages.
     */
    private void checkIfDequeIsEmptyOrStartOver() {

        if (cacheManager.getCache("messageQueue") instanceof MessageQueue messageQueue) {

            repaintMainFrame();

            SwingUtilities.invokeLater(() -> scrollMainPanelDownToLastMessage(this.mainFrame.getMainTextBackgroundScrollPane()));

            if (!messageQueue.isEmpty()) {

                timerForNewMessageToChatPanel();
            }
        }
    }

    /**
     This method sets a timer to periodically update the chat panel with new messages.
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
     This method triggers the repainting of the main frame, refreshing the graphical user interface. It revalidates and repaints the main frame to
     apply any changes made to its components.
     */
    private void repaintMainFrame() {

        if (mainFrame instanceof JFrame jFrame) {

            jFrame.revalidate();
            jFrame.repaint();
        }
    }

    /**
     Checks if the message sender is already registered in the local cache. If not, it adds the sender to the cache with default properties.

     @param clientMap
     the map representing the local cache of message senders
     @param sender
     the sender to be checked and possibly added to the cache
     */
    private void checkIfMessageSenderAlreadyRegisteredInLocalCache(HashMap<String, CustomUserPropertiesDTO> clientMap, String sender) {

        if (sender.equals(this.mainFrame.getUsername())) {

            if (!this.mainFrame.getChatClientPropertiesHashMap().containsKey("own")) {

                addClientToLocalCacheRegister(clientMap, "own");
            }

            return;
        }

        if (!clientMap.containsKey(sender)) {

            addClientToLocalCacheRegister(clientMap, sender);
        }
    }

    /**
     Adds a client to the local cache register.

     @param clientMap
     the map representing the local cache of clients
     @param sender
     the client to be added to the cache
     */
    private void addClientToLocalCacheRegister(final HashMap<String, CustomUserPropertiesDTO> clientMap, final String sender) {

        final CustomUserPropertiesDTO userPropertiesDTO = clientMap.get(sender);

        // 1) statusArray
        String username = userPropertiesDTO != null ? userPropertiesDTO.username() : sender;

        if (username.equals("own")) {

            username = this.mainFrame.getUsername();
        }

        // 2) nickname and 3) border color
        final String nickname = userPropertiesDTO != null ? userPropertiesDTO.nickname() : null;
        final String borderColor = String.valueOf(getRandomRgbIntValue());

        clientMap.put(sender, new CustomUserPropertiesDTO(username, nickname, borderColor));
    }

    /**
     Checks if the nickname for a given sender is already registered.

     @param sender
     the sender to check for a registered nickname

     @return the nickname of the sender if it is registered, otherwise null
     */
    private String checkForNickname(String sender) {

        if (this.mainFrame.getChatClientPropertiesHashMap().containsKey(sender) && this.mainFrame.getChatClientPropertiesHashMap().get(sender).nickname() != null && !this.mainFrame.getChatClientPropertiesHashMap().get(sender).nickname().isEmpty()) {

            return this.mainFrame.getChatClientPropertiesHashMap().get(sender).nickname();
        }

        return null;
    }

    /**
     Scrolls the main panel down to the last message in the scroll pane.
     <p>
     This method updates the main frame, repaints it, and scrolls the vertical scroll bar to its maximum position to display the last message.

     @param scrollPane
     the scroll pane containing the main panel
     */
    private void scrollMainPanelDownToLastMessage(JScrollPane scrollPane) {

        SwingUtilities.invokeLater(() -> {

            repaintMainFrame();
            scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
            repaintMainFrame();
        });
    }

}