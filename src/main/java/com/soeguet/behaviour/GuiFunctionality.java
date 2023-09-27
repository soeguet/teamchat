package com.soeguet.behaviour;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.gui.image_panel.ImagePanelImpl;
import com.soeguet.gui.main_frame.MainFrameInterface;
import com.soeguet.gui.newcomment.helper.CommentInterface;
import com.soeguet.gui.newcomment.left.PanelLeftImpl;
import com.soeguet.gui.newcomment.right.PanelRightImpl;
import com.soeguet.gui.notification_panel.NotificationImpl;
import com.soeguet.gui.popups.PopupPanelImpl;
import com.soeguet.model.MessageTypes;
import com.soeguet.model.PanelTypes;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.MessageModel;
import com.soeguet.model.jackson.PictureModel;
import com.soeguet.properties.CustomUserProperties;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
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
public class GuiFunctionality implements SocketToGuiInterface {

    private final MainFrameInterface mainFrame;
    private final ObjectMapper objectMapper = new ObjectMapper();
    Logger LOGGER = Logger.getLogger(GuiFunctionality.class.getName());
    private Timer messageToPanelTimer;

    /**
     Constructs a new GuiFunctionality object with the given main frame.
     This class is responsible for providing additional functionality to the GUI.

     @param mainFrame the main frame of the GUI
     */
    public GuiFunctionality(MainFrameInterface mainFrame) {

        this.mainFrame = mainFrame;
        ((JFrame) mainFrame).setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        fixScrollPaneScrollSpeed();
        addDocumentListenerToTextPane();
        overrideTransferHandlerOfTextPane();
    }

    /**
     Fixes the scroll speed for the main text background scroll pane.
     This method sets the unit increment of the vertical scrollbar of the main text background scroll pane to 20.
     This ensures that the scrolling speed is faster.
     */
    private void fixScrollPaneScrollSpeed() {

        this.mainFrame.getMainTextBackgroundScrollPane().getVerticalScrollBar().setUnitIncrement(25);
    }

    private void addDocumentListenerToTextPane() {

        this.mainFrame.getTextEditorPane().getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {

                int offset = e.getOffset();
                int length = e.getLength();
                Document doc = e.getDocument();

                try {

                    String insertedText = doc.getText(offset, length);
                    //TODO clickable links

                } catch (BadLocationException ex) {

                    LOGGER.log(java.util.logging.Level.SEVERE, "Error inserting text", ex);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

    }

    /**
     Overrides the transfer handler of the text pane in the GUI.
     This method is responsible for handling dropped data onto the text pane,
     distinguishing between image and text data and triggering the appropriate actions.

     The original transfer handler is preserved before overriding it with a new transfer handler.
     */
    private void overrideTransferHandlerOfTextPane() {

        //preserve the original transfer handler, otherwise clucky behavior
        TransferHandler originalHandler = this.mainFrame.getTextEditorPane().getTransferHandler();

        this.mainFrame.getTextEditorPane().setTransferHandler(new TransferHandler() {

            @Override
            public boolean importData(JComponent comp, Transferable t) {

                if (t.isDataFlavorSupported(DataFlavor.imageFlavor)) {

                    // image to text pane -> activate image panel
                    new ImagePanelImpl(mainFrame).populateImagePanelFromClipboard();

                    return true;
                } else if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {

                    try {
                        String data = (String) t.getTransferData(DataFlavor.stringFlavor);

                        //TODO maybe emoji detection?

                        if (data.startsWith("http://") || data.startsWith("https://")) {
                            //TODO maybe link detection?
                            LOGGER.info("HTTP Link detected");
                        } else {
                            return originalHandler.importData(comp, t);
                        }

                    } catch (UnsupportedFlavorException | IOException e) {

                        LOGGER.log(java.util.logging.Level.SEVERE, "Error importing data", e);
                    }

                    //TODO clickable links
                    return originalHandler.importData(comp, t);
                }
                return false;
            }
        });
    }

    /**
     Generates a random RGB integer value.

     This method creates a new instance of the Random class to generate random values for the red, green,
     and blue components of the RGB color. It then creates a new Color object using the generated values
     and retrieves the RGB integer value using the getRGB() method.

     @return the random RGB integer value
     */
    private static int getRandomRgbIntValue() {

        Random rand = new Random();
        int r = rand.nextInt(256);
        int g = rand.nextInt(256);
        int b = rand.nextInt(256);
        return new Color(r, g, b).getRGB();
    }

    /**
     Displays the nickname instead of the username in a comment.

     If the nickname parameter is not null and not empty after trimming,
     it sets the text of the name label in the comment to the nickname.

     @param nickname the nickname to be displayed
     @param comment  the comment object containing the name label
     */
    private void displayNicknameInsteadOfUsername(String nickname, CommentInterface comment) {

        if (nickname != null && !nickname.trim().isEmpty()) {
            comment.getNameLabel().setText(nickname);
        }
    }

    private void addMessagePanelToMainChatPanel(CommentInterface message, String alignment) {

        this.mainFrame.getMainTextPanel().add((JPanel) message, "w 70%, " + alignment + ", wrap");
    }

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

    /**
     This method is called when a new message is received.

     @param message The message received from the chat.
     */
    @Override
    public void onMessage(String message) {

        switch (message) {

            case "X" -> System.out.println("X");

            case "__startup__end__" -> mainFrame.setStartUp(false);

            case "welcome to the server!" -> new PopupPanelImpl(mainFrame, "Welcome to the server!").implementPopup(1000);

            default -> {

                mainFrame.getClientMessageQueue().add(message);
                createDesktopNotification(message);

                spamBuffer();
            }
        }
    }

    private void spamBuffer() {
        //using time to prevent spamming the GUI and making use of the cached messages
        if (messageToPanelTimer != null) {
            messageToPanelTimer.stop();
        }

        messageToPanelTimer = new Timer(500, e -> writeGuiMessageToChatPanel());
        messageToPanelTimer.setRepeats(false);
        messageToPanelTimer.start();
    }

    private void createDesktopNotification(final String message) {

        final int remainingCapacity = this.mainFrame.getNotificationActiveQueue().remainingCapacity();

        if (remainingCapacity < 1) {

            //max 3 notification at a time, cache message and bail out
            this.mainFrame.getNotificationWaitingQueue().add(message);
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
                //do nothing
            }
        }
    }

    private void externalNotificationHandling(final String message) {

        try {
            final BaseModel baseModel = convertJsonToMessageModel(message);

            switch (baseModel) {

                case MessageModel text -> {

                    try {
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

    private void internalNotificationHandling(final String message) {

        notificationActiveQueueHandling(message);
    }

    public synchronized void notificationActiveQueueHandling(String message) {

        if (message == null) {
            return;
        }

        final BaseModel baseModel = getMessageModel(message);

        if (this.mainFrame.getNotificationActiveQueue().remainingCapacity() < 1) {

            this.mainFrame.getNotificationWaitingQueue().add(message);

        } else {

            this.mainFrame.getNotificationActiveQueue().add(baseModel);
            createNotification(baseModel);
        }

        handleRemainingCapacityInQueue();
    }

    private void handleRemainingCapacityInQueue() {

        if (this.mainFrame.getNotificationActiveQueue().remainingCapacity() > 0) {

            final String first = this.mainFrame.getNotificationWaitingQueue().pollFirst();

            Timer timer = new Timer(750, e -> notificationActiveQueueHandling(first));
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void createNotification(final BaseModel baseModel) {

        switch (baseModel) {

            case MessageModel text -> {

                Timer timer = new Timer(500, e -> {
                    new NotificationImpl(this.mainFrame, text).setNotificationText();
                });
                timer.setRepeats(false);
                timer.start();
            }

            case PictureModel picture -> {

                Timer timer = new Timer(500, e -> {
                    new NotificationImpl(this.mainFrame, picture).setNotificationPicture();
                });
                timer.setRepeats(false);
                timer.start();
            }

            default -> LOGGER.info("Unknown message type");
        }
    }

    private synchronized void writeGuiMessageToChatPanel() {

        String message = mainFrame.getClientMessageQueue().pollFirst();

        if (message == null) {
            return;
        }

        BaseModel messageModel = getMessageModel(message);

        checkIfMessageSenderAlreadyRegisteredInLocalCache(mainFrame.getChatClientPropertiesHashMap(), messageModel.getSender());
        String nickname = checkForNickname(messageModel.getSender());

        final String username = mainFrame.getUsername();

        processAndDisplayMessage(messageModel, username, nickname);

        checkIfDequeIsEmptyOrStartOver();
    }

    private void processAndDisplayMessage(final BaseModel messageModel, final String username, final String nickname) {

        switch (messageModel) {

            case MessageModel text -> {

                if (text.getSender().equals(username)) {

                    setupMessagesRightSide(text, nickname);

                } else {

                    setupMessagesLeftSide(text, nickname);
                }

            }

            case PictureModel picture -> {

                if (messageModel.getSender().equals(username)) {

                    setupPicturesRightSide(picture, nickname);

                } else {

                    setupPicturesLeftSide(picture, nickname);
                }
            }

            default -> LOGGER.info("Unknown message type");
        }
    }

    private void setupPicturesLeftSide(final BaseModel messageModel, final String nickname) {

        Color borderColor = determineBorderColor(messageModel.getSender());

        CommentInterface panelLeft = new PanelLeftImpl(this.mainFrame, messageModel);
        panelLeft.setupPicturePanelWrapper();
        panelLeft.setBorderColor(borderColor);
        displayNicknameInsteadOfUsername(nickname, panelLeft);
        addMessagePanelToMainChatPanel(panelLeft, "leading");
    }

    private void setupPicturesRightSide(final BaseModel messageModel, final String nickname) {

        Color borderColor = determineBorderColor("own");

        CommentInterface panelRight = new PanelRightImpl(this.mainFrame, messageModel);
        panelRight.setupPicturePanelWrapper();
        panelRight.setBorderColor(borderColor);
        displayNicknameInsteadOfUsername(nickname, panelRight);
        addMessagePanelToMainChatPanel(panelRight, "trailing");
    }

    private void setupMessagesLeftSide(final BaseModel messageModel, final String nickname) {

        Color borderColor = determineBorderColor(messageModel.getSender());

        CommentInterface panelLeft = new PanelLeftImpl(this.mainFrame, messageModel, PanelTypes.NORMAL);
        panelLeft.setupTextPanelWrapper();
        panelLeft.setBorderColor(borderColor);
        displayNicknameInsteadOfUsername(nickname, panelLeft);
        addMessagePanelToMainChatPanel(panelLeft, "leading");

    }

    private void setupMessagesRightSide(final BaseModel messageModel, final String nickname) {

        Color borderColor = determineBorderColor("own");

        CommentInterface panelRight = new PanelRightImpl(this.mainFrame, messageModel, PanelTypes.NORMAL);
        panelRight.setupTextPanelWrapper();
        panelRight.setBorderColor(borderColor);
        displayNicknameInsteadOfUsername(nickname, panelRight);
        addMessagePanelToMainChatPanel(panelRight, "trailing");
    }

    private void checkIfDequeIsEmptyOrStartOver() {

        ((JFrame) this.mainFrame).revalidate();
        ((JFrame) this.mainFrame).repaint();

        SwingUtilities.invokeLater(() -> scrollMainPanelDownToLastMessage(this.mainFrame.getMainTextBackgroundScrollPane()));

        if (!this.mainFrame.getClientMessageQueue().isEmpty()) {

            Timer timer = new Timer(300, e -> {

                writeGuiMessageToChatPanel();
                scrollMainPanelDownToLastMessage(this.mainFrame.getMainTextBackgroundScrollPane());
            });
            timer.setRepeats(false);
            timer.start();
        }

    }

    /**
     Converts a JSON string to a MessageModel object.

     @param message the JSON string to be converted

     @return the MessageModel object representing the converted JSON string

     @throws IllegalArgumentException if the JSON string is malformed
     */
    private BaseModel getMessageModel(String message) {

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

    private Color determineBorderColor(String sender) {

        if (this.mainFrame.getChatClientPropertiesHashMap().containsKey(sender)) {

            return new Color(this.mainFrame.getChatClientPropertiesHashMap().get(sender).getBorderColor());
        }

        return new Color(getRandomRgbIntValue());
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
        ((JFrame) this.mainFrame).revalidate();
        ((JFrame) this.mainFrame).repaint();
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