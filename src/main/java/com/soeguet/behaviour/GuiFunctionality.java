package com.soeguet.behaviour;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.gui.image_panel.ImagePanelImpl;
import com.soeguet.gui.main_frame.MainFrameInterface;
import com.soeguet.gui.newcomment.helper.CommentInterface;
import com.soeguet.gui.newcomment.left.PanelLeftImpl;
import com.soeguet.gui.newcomment.right.PanelRightImpl;
import com.soeguet.gui.notification_panel.NotificationImpl;
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

        mainFrame.getMainTextBackgroundScrollPane().getVerticalScrollBar().setUnitIncrement(25);
    }

    private void addDocumentListenerToTextPane() {

        mainFrame.getTextEditorPane().getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {

                int offset = e.getOffset();
                int length = e.getLength();
                Document doc = e.getDocument();

                try {
                    String insertedText = doc.getText(offset, length);
                    //TODO clickable links
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
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
        TransferHandler originalHandler = mainFrame.getTextEditorPane().getTransferHandler();

        mainFrame.getTextEditorPane().setTransferHandler(new TransferHandler() {

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

    private void addMessagePanelToMainChatPanel(MainFrameInterface mainFrame, CommentInterface message, String alignment) {

        mainFrame.getMainTextPanel().add((JPanel) message, "w 70%, " + alignment + ", wrap");
    }

    /**
     Clear the text pane and send the message to the socket.

     This method retrieves the main frame instance and performs the following steps:
     1. Retrieves the text input from the GUI using the {@link #getTextFromInput(MainFrameInterface)} method.
     2. Clears the text pane using the {@link #clearTextPane(MainFrameInterface)} method.
     3. Converts the user text input to JSON format using the {@link #convertUserTextToJSON(String, MainFrameInterface)} method.
     4. Sends the message to the socket using the {@link #sendMessageToSocket(String, MainFrameInterface)} method.

     @throws AssertionError if the main frame instance is null
     @see #getTextFromInput(MainFrameInterface)
     @see #clearTextPane(MainFrameInterface)
     @see #convertUserTextToJSON(String, MainFrameInterface)
     @see #sendMessageToSocket(String, MainFrameInterface)
     */
    public void clearTextPaneAndSendMessageToSocket() {

        String userTextInput = getTextFromInput(mainFrame);
        clearTextPane(mainFrame);
        String messageString = convertUserTextToJSON(userTextInput, mainFrame);
        sendMessageToSocket(messageString, mainFrame);

    }

    /**
     Retrieves the text from the input in the graphical user interface.

     @param gui The instance of {@link MainFrameInterface} representing the graphical user interface.

     @return The text from the input in the graphical user interface.
     */
    private String getTextFromInput(MainFrameInterface gui) {

        return gui.getTextEditorPane().getText();
    }

    /**
     Retrieves the text from the input in the graphical user interface.

     @param gui The instance of {@link MainFrameInterface} representing the graphical user interface.
     */
    private void clearTextPane(MainFrameInterface gui) {

        gui.getTextEditorPane().setText("");
    }

    /**
     Converts user input text to JSON format.

     @param userTextInput the text input provided by the user
     @param gui           The instance of {@link MainFrameInterface} representing the graphical user interface.

     @return The converted JSON string.
     */
    private String convertUserTextToJSON(String userTextInput, MainFrameInterface gui) {

        return convertToJSON(textToMessageModel(userTextInput), gui);
    }

    /**
     Sends a message to the socket.

     @param messageString The message to be sent.
     @param gui           The interface for accessing the main GUI elements.
     */
    private void sendMessageToSocket(String messageString, MainFrameInterface gui) {

        gui.getWebsocketClient().send(messageString);
    }

    /**
     Converts the given {@link BaseModel} object to a JSON string representation.

     @param messageModel The {@link BaseModel} object to be converted.
     @param gui          The {@link MainFrameInterface} object used to access the ObjectMapper for JSON conversion.

     @return The JSON string representation of the {@link BaseModel} object.

     @throws RuntimeException if there is an error during the JSON conversion process.
     */
    private String convertToJSON(BaseModel messageModel, MainFrameInterface gui) {

        try {

            return gui.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(messageModel);

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
            case "X" -> {
                System.out.println("X");
            }
            case "__startup__end__" -> {
                mainFrame.setStartUp(false);
            }
            case "welcome to the server!" -> {
                System.out.println("welcome to the server!");
            }
            default -> {

                mainFrame.getClientMessageQueue().add(message);

                if (!mainFrame.getIsProcessingClientMessages().get()) {

                    writeGuiMessageToChatPanel();
                }
            }
        }
    }

    private synchronized void writeGuiMessageToChatPanel() {

        String message = mainFrame.getClientMessageQueue().poll();

        if (message == null) {
            return;
        }

        BaseModel messageModel = getMessageModel(message);

        checkIfMessageSenderAlreadyRegisteredInLocalCache(mainFrame.getChatClientPropertiesHashMap(), messageModel.getSender());
        String nickname = checkForNickname(mainFrame, messageModel.getSender());

        final String username = mainFrame.getUsername();

        processAndDisplayMessage(messageModel, username, nickname);

        checkIfDequeIsEmptyOrStartOver(mainFrame);
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

            default -> {
                LOGGER.info("Unknown message type");
            }
        }
    }

    private void setupPicturesLeftSide(final BaseModel messageModel, final String nickname) {

        Color borderColor = determineBorderColor(mainFrame, messageModel.getSender());

        PanelLeftImpl panelLeft = new PanelLeftImpl(mainFrame, messageModel);
        panelLeft.setupPicturePanelWrapper();
        panelLeft.setBorderColor(borderColor);
        displayNicknameInsteadOfUsername(nickname, panelLeft);
        addMessagePanelToMainChatPanel(mainFrame, panelLeft, "leading");

        new NotificationImpl(mainFrame, messageModel).setNotificationText();
    }

    private void setupPicturesRightSide(final BaseModel messageModel, final String nickname) {

        Color borderColor = determineBorderColor(mainFrame, "own");

        PanelRightImpl panelRight = new PanelRightImpl(mainFrame, messageModel);
        panelRight.setupPicturePanelWrapper();
        panelRight.setBorderColor(borderColor);
        displayNicknameInsteadOfUsername(nickname, panelRight);
        addMessagePanelToMainChatPanel(mainFrame, panelRight, "trailing");
    }

    private void setupMessagesLeftSide(final BaseModel messageModel, final String nickname) {

        Color borderColor = determineBorderColor(mainFrame, messageModel.getSender());

        PanelLeftImpl panelLeft = new PanelLeftImpl(mainFrame, messageModel, PanelTypes.NORMAL);
        panelLeft.setupTextPanelWrapper();
        panelLeft.setBorderColor(borderColor);
        displayNicknameInsteadOfUsername(nickname, panelLeft);
        addMessagePanelToMainChatPanel(mainFrame, panelLeft, "leading");

        new NotificationImpl(mainFrame, messageModel).setNotificationText();
    }

    private void setupMessagesRightSide(final BaseModel messageModel, final String nickname) {

        Color borderColor = determineBorderColor(mainFrame, "own");

        PanelRightImpl panelRight = new PanelRightImpl(mainFrame, messageModel, PanelTypes.NORMAL);
        panelRight.setupTextPanelWrapper();
        panelRight.setBorderColor(borderColor);
        displayNicknameInsteadOfUsername(nickname, panelRight);
        addMessagePanelToMainChatPanel(mainFrame, panelRight, "trailing");
    }

    private void checkIfDequeIsEmptyOrStartOver(MainFrameInterface gui) {

        // ensure that the scrollpane can keep up with the additons!
        try {

            Thread.sleep(200);

        } catch (InterruptedException e) {

            throw new RuntimeException(e);
        }

        SwingUtilities.invokeLater(() -> {
            scrollMainPanelDownToLastMessage(mainFrame.getMainTextBackgroundScrollPane());
        });

        if (!gui.getClientMessageQueue().isEmpty()) {

            writeGuiMessageToChatPanel();

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

        if (sender.equals(mainFrame.getUsername())) {

            if (!mainFrame.getChatClientPropertiesHashMap().containsKey("own")) {

                CustomUserProperties customUserProperties = new CustomUserProperties();
                customUserProperties.setUsername(mainFrame.getUsername());
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

    /**
     Determines the border color for a given sender.

     @param gui    the interface to access the chat client properties hashmap
     @param sender the sender to determine the border color for

     @return the determined border color for the given sender
     */
    private Color determineBorderColor(MainFrameInterface gui, String sender) {

        if (gui.getChatClientPropertiesHashMap().containsKey(sender)) {

            return new Color(gui.getChatClientPropertiesHashMap().get(sender).getBorderColor());
        }

        return new Color(getRandomRgbIntValue());
    }

    /**
     Checks if a nickname is defined for a given sender in the GUI.

     If the sender is present in the chat client properties map and has a valid nickname defined,
     the method returns the nickname as a String. Otherwise, it returns null.

     @param gui    the main GUI elements interface containing the chat client properties map
     @param sender the sender for which the nickname is to be checked

     @return the nickname for the sender as a String, or null if not found
     */
    private String checkForNickname(MainFrameInterface gui, String sender) {

        if (gui.getChatClientPropertiesHashMap().containsKey(sender) && gui.getChatClientPropertiesHashMap().get(sender).getNickname() != null && !gui.getChatClientPropertiesHashMap().get(sender).getNickname().isEmpty()) {

            return gui.getChatClientPropertiesHashMap().get(sender).getNickname();
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
        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
        ((JFrame) mainFrame).repaint();
    }

    /**
     Converts a JSON message into a MessageModel object.

     @param jsonMessage the JSON message to be converted

     @return the converted MessageModel object

     @throws JsonProcessingException if there is an error processing the JSON message
     */
    private BaseModel convertJsonToMessageModel(String jsonMessage) throws JsonProcessingException {

        return objectMapper.readValue(jsonMessage, BaseModel.class);
    }
}