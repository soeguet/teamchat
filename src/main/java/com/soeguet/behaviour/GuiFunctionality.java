package com.soeguet.behaviour;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.gui.main_frame.MainGuiElementsInterface;
import com.soeguet.gui.newcomment.helper.CommentInterface;
import com.soeguet.gui.newcomment.left.PanelLeftImpl;
import com.soeguet.gui.newcomment.right.PanelRightImpl;
import com.soeguet.model.MessageModel;
import com.soeguet.model.MessageTypes;
import com.soeguet.model.PanelTypes;
import com.soeguet.properties.CustomUserProperties;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Random;

/**
 This class provides additional functionality to the GUI.

 Implements the SocketToGuiInterface for receiving messages from the socket.
 */
public class GuiFunctionality implements SocketToGuiInterface {

    private final JFrame mainFrame;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     Constructs a new GuiFunctionality object with the given main frame.
     This class is responsible for providing additional functionality to the GUI.

     @param mainFrame the main frame of the GUI
     */
    public GuiFunctionality(JFrame mainFrame) {

        this.mainFrame = mainFrame;
        fixScrollPaneScrollSpeed();
    }

    /**
     Fixes the scroll speed for the main text background scroll pane.
     This method sets the unit increment of the vertical scrollbar of the main text background scroll pane to 20.
     This ensures that the scrolling speed is faster.
     */
    private void fixScrollPaneScrollSpeed() {

        MainGuiElementsInterface gui = getMainFrame();
        assert gui != null;

        gui.getMainTextBackgroundScrollPane().getVerticalScrollBar().setUnitIncrement(25);
    }

    /**
     Retrieves the main frame if it is an instance of {@link MainGuiElementsInterface}.

     @return The main frame if it is an instance of {@link MainGuiElementsInterface}, otherwise null.
     */
    private MainGuiElementsInterface getMainFrame() {

        if (!(mainFrame instanceof MainGuiElementsInterface)) {
            return null;
        }

        return (MainGuiElementsInterface) mainFrame;
    }

    /**
     Displays the nickname instead of the username in a comment.

     If the nickname parameter is not null and not empty after trimming,
     it sets the text of the name label in the comment to the nickname.

     @param nickname the nickname to be displayed
     @param comment  the comment object containing the name label
     */
    private static void displayNicknameInsteadOfUsername(String nickname, CommentInterface comment) {

        if (nickname != null && !nickname.trim().isEmpty()) {
            comment.getNameLabel().setText(nickname);
        }
    }

    private static void addMessagePanelToMainChatPanel(MainGuiElementsInterface gui, CommentInterface message, String alignment) {

        SwingUtilities.invokeLater(() -> gui.getMainTextPanel().add((JPanel) message, "w 70%, " + alignment + ", wrap"));
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
     Clear the text pane and send the message to the socket.

     This method retrieves the main frame instance and performs the following steps:
     1. Retrieves the text input from the GUI using the {@link #getTextFromInput(MainGuiElementsInterface)} method.
     2. Clears the text pane using the {@link #clearTextPane(MainGuiElementsInterface)} method.
     3. Converts the user text input to JSON format using the {@link #convertUserTextToJSON(String, MainGuiElementsInterface)} method.
     4. Sends the message to the socket using the {@link #sendMessageToSocket(String, MainGuiElementsInterface)} method.

     @throws AssertionError if the main frame instance is null
     @see #getTextFromInput(MainGuiElementsInterface)
     @see #clearTextPane(MainGuiElementsInterface)
     @see #convertUserTextToJSON(String, MainGuiElementsInterface)
     @see #sendMessageToSocket(String, MainGuiElementsInterface)
     */
    public void clearTextPaneAndSendMessageToSocket() {

        MainGuiElementsInterface gui = getMainFrame();
        assert gui != null;

        String userTextInput = getTextFromInput(gui);
        clearTextPane(gui);
        String messageString = convertUserTextToJSON(userTextInput, gui);
        sendMessageToSocket(messageString, gui);

    }

    /**
     Retrieves the text from the input in the graphical user interface.

     @param gui The instance of {@link MainGuiElementsInterface} representing the graphical user interface.

     @return The text from the input in the graphical user interface.
     */
    private String getTextFromInput(MainGuiElementsInterface gui) {

        return gui.getTextEditorPane().getText();
    }

    /**
     Clears the text contents of a text pane.

     @param gui the MainGuiElementsInterface object representing the GUI elements
     */
    private void clearTextPane(MainGuiElementsInterface gui) {

        gui.getTextEditorPane().setText("");
    }

    /**
     Converts user input text to JSON format.

     @param userTextInput the text input provided by the user
     @param gui           the MainGuiElementsInterface object used to convert the text to JSON

     @return the converted JSON string
     */
    private String convertUserTextToJSON(String userTextInput, MainGuiElementsInterface gui) {

        return convertToJSON(textToMessageModel(userTextInput), gui);
    }

    /**
     Sends a message to the socket.

     @param messageString The message to be sent.
     @param gui           The interface for accessing the main GUI elements.
     */
    private void sendMessageToSocket(String messageString, MainGuiElementsInterface gui) {

        gui.getWebsocketClient().send(messageString);
    }

    /**
     Converts the given MessageModel object to a JSON string representation.

     @param messageModel The MessageModel object to be converted.
     @param gui          The MainGuiElementsInterface object used to access the ObjectMapper for JSON conversion.

     @return The JSON string representation of the MessageModel object.

     @throws RuntimeException if there is an error during the JSON conversion process.
     */
    private String convertToJSON(MessageModel messageModel, MainGuiElementsInterface gui) {

        try {

            return gui.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(messageModel);

        } catch (JsonProcessingException e) {

            throw new RuntimeException(e);
        }
    }

    /**
     Converts a user input text into a MessageModel object.

     @param userTextInput The text entered by the user.

     @return A MessageModel object representing the user input.
     */
    private MessageModel textToMessageModel(String userTextInput) {

        return new MessageModel((byte) MessageTypes.NORMAL, "yasman", userTextInput);
    }

    /**
     This method is called when a new message is received.

     @param message The message received from the chat.
     */
    @Override
    public void onMessage(String message) {

        MainGuiElementsInterface gui = getMainFrame();
        assert gui != null;

        gui.getClientMessageQueue().add(message);

        if (!gui.getIsProcessingClientMessages().get()) {
            writeGuiMessageToChatPanel();
        }
    }

    private void writeGuiMessageToChatPanel() {

        MainGuiElementsInterface gui = getMainFrame();
        assert gui != null;

        gui.getIsProcessingClientMessages().set(true);

        String message = gui.getClientMessageQueue().poll();
        assert message != null;

        MessageModel messageModel = getMessageModel(message);

        checkIfMessageSenderAlreadyRegisteredInLocalCache(gui.getChatClientPropertiesHashMap(), messageModel.getSender());

        String nickname = checkForNickname(gui, messageModel.getSender());

        if (messageModel.getSender().equals(gui.getUsername())) {

            Color borderColor = determineBorderColor(gui, "own");

            PanelRightImpl panelRight = new PanelRightImpl(mainFrame, messageModel, PanelTypes.NORMAL);
            panelRight.setBorderColor(borderColor);
            displayNicknameInsteadOfUsername(nickname, panelRight);
            addMessagePanelToMainChatPanel(gui, panelRight, "trailing");

        } else {

            Color borderColor = determineBorderColor(gui, messageModel.getSender());

            PanelLeftImpl panelLeft = new PanelLeftImpl(mainFrame, messageModel, PanelTypes.NORMAL);
            panelLeft.setBorderColor(borderColor);
            displayNicknameInsteadOfUsername(nickname, panelLeft);
            addMessagePanelToMainChatPanel(gui, panelLeft, "leading");
        }

        mainFrame.revalidate();
        mainFrame.repaint();
        scrollMainPanelDownToLastMessage(gui.getMainTextBackgroundScrollPane());

        checkIfDequeIsEmptyOrStartOver(gui);
    }

    private void checkIfDequeIsEmptyOrStartOver(MainGuiElementsInterface gui) {

        if (!gui.getClientMessageQueue().isEmpty()) {

            writeGuiMessageToChatPanel();

        } else {

            gui.getIsProcessingClientMessages().set(false);
            scrollToMax(gui.getMainTextBackgroundScrollPane().getVerticalScrollBar());
        }
    }

    /**
     Converts a JSON string to a MessageModel object.

     @param message the JSON string to be converted

     @return the MessageModel object representing the converted JSON string

     @throws IllegalArgumentException if the JSON string is malformed
     */
    private MessageModel getMessageModel(String message) {

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

        MainGuiElementsInterface gui = getMainFrame();
        assert gui != null;

        if (sender.equals(gui.getUsername())) {

            if (!gui.getChatClientPropertiesHashMap().containsKey("own")) {

                CustomUserProperties customUserProperties = new CustomUserProperties();
                customUserProperties.setUsername("own");
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
    private Color determineBorderColor(MainGuiElementsInterface gui, String sender) {

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
    private String checkForNickname(MainGuiElementsInterface gui, String sender) {

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

        SwingUtilities.invokeLater(() -> {
            mainFrame.revalidate();
            mainFrame.repaint();
            scrollToMax(scrollPane.getVerticalScrollBar());
        });
    }

    /**
     Converts a JSON message into a MessageModel object.

     @param jsonMessage the JSON message to be converted

     @return the converted MessageModel object

     @throws JsonProcessingException if there is an error processing the JSON message
     */
    private MessageModel convertJsonToMessageModel(String jsonMessage) throws JsonProcessingException {

        return objectMapper.readValue(jsonMessage, MessageModel.class);
    }

    /**
     Scrolls the given scroll bar to its maximum value.

     @param scrollBar the scroll bar to be scrolled
     */
    private void scrollToMax(JScrollBar scrollBar) {

        scrollBar.setValue(scrollBar.getMaximum());
    }
}