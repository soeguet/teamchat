package com.soeguet.behaviour;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.gui.main_frame.MainGuiElementsInterface;
import com.soeguet.gui.newcomment.left.PanelLeftImpl;
import com.soeguet.gui.newcomment.right.PanelRightImpl;
import com.soeguet.model.MessageModel;
import com.soeguet.model.MessageTypes;
import com.soeguet.model.PanelTypes;

import javax.swing.*;

public class GuiFunctionality implements SocketToGuiInterface {

    private final JFrame mainFrame;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public GuiFunctionality(JFrame mainFrame) {

        this.mainFrame = mainFrame;
        fixScrollPaneScrollSpeed();
    }

    /**
     * Fixes the scroll speed of the scroll pane associated with the main text background.
     * If the main frame is an instance of {@link MainGuiElementsInterface}, then it retrieves the vertical scroll bar
     * of the main text background scroll pane and sets its unit increment to 16.
     */
    private void fixScrollPaneScrollSpeed() {

        if (mainFrame instanceof MainGuiElementsInterface) {
            ((MainGuiElementsInterface) mainFrame).getMainTextBackgroundScrollPane().getVerticalScrollBar().setUnitIncrement(16);
        }
    }

    /**
     * Clears the text pane and sends a message to the socket.
     * If the main frame is an instance of {@link MainGuiElementsInterface}, it performs the following steps:
     * 1. Retrieves the user's input from the GUI and stores it in a variable.
     * 2. Clears the text pane in the GUI.
     * 3. Converts the user's input to JSON format and stores it in a variable.
     * 4. Sends the message to the socket using the JSON string and the GUI.
     */
    public void clearTextPaneAndSendMessageToSocket() {

        if (isInstanceOfMainGuiElementsInterface(mainFrame)) {

            MainGuiElementsInterface gui = (MainGuiElementsInterface) mainFrame;
            String userTextInput = getTextFromInput(gui);
            clearTextPane(gui);
            String messageString = convertUserTextToJSON(userTextInput, gui);
            sendMessageToSocket(messageString, gui);
        }
    }

    /**
     * Checks if the given object is an instance of {@link MainGuiElementsInterface}.
     *
     * @param obj The object to check.
     * @return <code>true</code> if the given object is an instance of {@link MainGuiElementsInterface}, <code>false</code> otherwise.
     */
    private boolean isInstanceOfMainGuiElementsInterface(Object obj) {

        return obj instanceof MainGuiElementsInterface;
    }

    /**
     * Retrieves the text from the input in the graphical user interface.
     *
     * @param gui The instance of {@link MainGuiElementsInterface} representing the graphical user interface.
     * @return The text from the input in the graphical user interface.
     */
    private String getTextFromInput(MainGuiElementsInterface gui) {

        return gui.getTextEditorPane().getText();
    }

    /**
     * Converts user input text to JSON format.
     *
     * @param userTextInput the text input provided by the user
     * @param gui           the MainGuiElementsInterface object used to convert the text to JSON
     * @return the converted JSON string
     */
    private String convertUserTextToJSON(String userTextInput, MainGuiElementsInterface gui) {

        return convertToJSON(textToMessageModel(userTextInput), gui);
    }


    /**
     * Clears the text contents of a text pane.
     *
     * @param gui the MainGuiElementsInterface object representing the GUI elements
     */
    private void clearTextPane(MainGuiElementsInterface gui) {

        gui.getTextEditorPane().setText("");
    }

    /**
     * Converts the given MessageModel object to a JSON string representation.
     *
     * @param messageModel The MessageModel object to be converted.
     * @param gui          The MainGuiElementsInterface object used to access the ObjectMapper for JSON conversion.
     * @return The JSON string representation of the MessageModel object.
     * @throws RuntimeException if there is an error during the JSON conversion process.
     */
    private String convertToJSON(MessageModel messageModel, MainGuiElementsInterface gui) {

        try {
            return gui.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(messageModel);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends a message to the socket.
     *
     * @param messageString The message to be sent.
     * @param gui           The interface for accessing the main GUI elements.
     */
    private void sendMessageToSocket(String messageString, MainGuiElementsInterface gui) {

        gui.getWebsocketClient().send(messageString);
    }


    /**
     * Converts a user input text into a MessageModel object.
     *
     * @param userTextInput The text entered by the user.
     * @return A MessageModel object representing the user input.
     */
    private MessageModel textToMessageModel(String userTextInput) {

        return new MessageModel((byte) MessageTypes.NORMAL, "yasman", userTextInput);
    }

    /**
     * This method is called when a new message is received.
     *
     * @param message The message received from the chat.
     */
    @Override
    public void onMessage(String message) {

        writeGuiMessageToChatPanel(message);
    }

    /**
     * Writes a GUI message to the chat panel.
     *
     * @param message the message to write
     */
    private void writeGuiMessageToChatPanel(String message) {

        if (!(mainFrame instanceof MainGuiElementsInterface)) {
            return;
        }

        MainGuiElementsInterface gui = (MainGuiElementsInterface) mainFrame;

        MessageModel messageModel = getMessageModel(message);

        if (messageModel.getSender().equals(gui.getUsername())) {

            PanelRightImpl panelRight = new PanelRightImpl(mainFrame, messageModel, PanelTypes.NORMAL);
            ((MainGuiElementsInterface) mainFrame).getMainTextPanel().add(panelRight, "w 80%, trailing, wrap");
        } else {
            PanelLeftImpl panelLeft = new PanelLeftImpl(mainFrame, messageModel, PanelTypes.NORMAL);
            ((MainGuiElementsInterface) mainFrame).getMainTextPanel().add(panelLeft, "w 80%, leading, wrap");
        }

        mainFrame.revalidate();
        mainFrame.repaint();
        scrollMainPanelDownToLastMessage(((MainGuiElementsInterface) mainFrame).getMainTextBackgroundScrollPane());
    }

    /**
     * Converts a JSON string to a MessageModel object.
     *
     * @param message the JSON string to be converted
     * @return the MessageModel object representing the converted JSON string
     * @throws IllegalArgumentException if the JSON string is malformed
     */
    private MessageModel getMessageModel(String message) {

        try {
            return convertJsonToMessageModel(message);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Malformed message", e);
        }
    }

    /**
     * Converts a JSON message into a MessageModel object.
     *
     * @param jsonMessage the JSON message to be converted
     * @return the converted MessageModel object
     * @throws JsonProcessingException if there is an error processing the JSON message
     */
    private MessageModel convertJsonToMessageModel(String jsonMessage) throws JsonProcessingException {

        return objectMapper.readValue(jsonMessage, MessageModel.class);
    }

    /**
     * Scrolls the main panel down to the last message in the given scroll pane.
     *
     * @param scrollPane the scroll pane containing the main panel
     */
    public void scrollMainPanelDownToLastMessage(JScrollPane scrollPane) {

        SwingUtilities.invokeLater(() -> scrollToMax(scrollPane.getVerticalScrollBar()));
    }

    /**
     * Scrolls the given scroll bar to its maximum value.
     *
     * @param scrollBar the scroll bar to be scrolled
     */
    private void scrollToMax(JScrollBar scrollBar) {

        scrollBar.setValue(scrollBar.getMaximum());
    }
}
