package com.soeguet.behaviour;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pre.model.MessageModel;
import com.soeguet.gui.main_frame.MainGuiElementsInterface;
import com.soeguet.gui.newcomment.right.PanelRightImpl;

import javax.swing.*;

public class GuiFunctionality implements SocketToGuiInterface {

    private final JFrame mainFrame;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public GuiFunctionality(JFrame mainFrame) {

        this.mainFrame = mainFrame;
        fixScrollpaneScrollSpeed();
    }

    private void fixScrollpaneScrollSpeed() {

        if (mainFrame instanceof MainGuiElementsInterface) {
            ((MainGuiElementsInterface) mainFrame).getMainTextBackgroundScrollPane().getVerticalScrollBar().setUnitIncrement(16);
        }
    }

    public void clearTextPaneAndSendMessageToSocket() {

        if (!(mainFrame instanceof MainGuiElementsInterface)) {
            return;
        }

        MainGuiElementsInterface gui = (MainGuiElementsInterface) mainFrame;
        String userTextInput = gui.getTextEditorPane().getText();
        clearTextPane(gui);
        String messageString = convertToJSON(textToMessageModel(userTextInput), gui);
        sendMessageToSocket(messageString, gui);
    }

    private void clearTextPane(MainGuiElementsInterface gui) {

        gui.getTextEditorPane().setText("");
    }

    private String convertToJSON(MessageModel messageModel, MainGuiElementsInterface gui) {

        try {
            return gui.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(messageModel);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMessageToSocket(String messageString, MainGuiElementsInterface gui) {

        gui.getWebsocketClient().send(messageString);
    }


    private MessageModel textToMessageModel(String userTextInput) {

        return new MessageModel("osman", userTextInput);
    }

    @Override
    public void onMessage(String message) {

//        writeMessageToChatPanel(message);
        writeGuiMessageToChatPanel(message);
    }

    private void writeGuiMessageToChatPanel(String message) {

        MessageModel messageModel = getMessageModel(message);
//        JPanel jPanel = createMessagePanel(messageModel.getMessage());

        PanelRightImpl panelRight = new PanelRightImpl(mainFrame, messageModel);
        if (mainFrame instanceof MainGuiElementsInterface) {
            ((MainGuiElementsInterface) mainFrame).getMainTextPanel().add(panelRight, "w 80%, trailing, wrap");

            mainFrame.revalidate();
            mainFrame.repaint();
            scrollMainPanelDownToLastMessage(((MainGuiElementsInterface) mainFrame).getMainTextBackgroundScrollPane());
        }
    }

    private void writeMessageToChatPanel(String message) {

        MessageModel messageModel = getMessageModel(message);
        JPanel jPanel = createMessagePanel(messageModel.getMessage());
        addMessagePanel(mainFrame, jPanel);
    }

    private MessageModel getMessageModel(String message) {

        try {
            return convertJsonToMessageModel(message);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Malformed message", e);
        }
    }

    private void addMessagePanel(JFrame mainFrame, JPanel jPanel) {

        if (mainFrame instanceof MainGuiElementsInterface) {
            MainGuiElementsInterface mainGuiElements = (MainGuiElementsInterface) mainFrame;
            addNewPanel(mainGuiElements, jPanel);
        }
    }


    // define further functions to make complex operations clearer
    private MessageModel convertJsonToMessageModel(String jsonMessage) throws JsonProcessingException {

        return objectMapper.readValue(jsonMessage, MessageModel.class);
    }

    private JPanel createMessagePanel(String message) {

        JPanel jPanel = new JPanel();
        JLabel jLabel = new JLabel(message);
        jPanel.add(jLabel);
        return jPanel;
    }

    private void addNewPanel(MainGuiElementsInterface mainGuiElements, JPanel jPanel) {

        JPanel mainPanel = mainGuiElements.getMainTextPanel();
        JScrollPane scrollPane = mainGuiElements.getMainTextBackgroundScrollPane();

        mainPanel.add(jPanel, "wrap");
        rerenderTextPane();
        scrollMainPanelDownToLastMessage(scrollPane);
    }

    private void rerenderTextPane() {

        if (mainFrame instanceof MainGuiElementsInterface) {
            MainGuiElementsInterface mainGui = (MainGuiElementsInterface) mainFrame;

            JPanel mainTextPanel = mainGui.getMainTextPanel();

            mainTextPanel.revalidate();
            mainTextPanel.repaint();
        }
    }

    public void scrollMainPanelDownToLastMessage(JScrollPane scrollPane) {

        SwingUtilities.invokeLater(() -> scrollToMax(scrollPane.getVerticalScrollBar()));
    }

    private void scrollToMax(JScrollBar scrollBar) {

        scrollBar.setValue(scrollBar.getMaximum());
    }

}
