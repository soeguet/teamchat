package com.soeguet.gui.behaviour;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pre.model.MessageModel;
import com.soeguet.gui.main_frame.MainGuiElementsInterface;

import javax.swing.*;

public class GuiFunctionality implements SocketToGuiInterface {

    private final JFrame mainFrame;

    public GuiFunctionality(JFrame mainFrame) {

        this.mainFrame = mainFrame;
    }

    public void clearTextPaneAndSendMessageToSocket() {

        if (mainFrame instanceof MainGuiElementsInterface) {

            String userTextInput = ((MainGuiElementsInterface) mainFrame).getTextEditorPane().getText();
            ((MainGuiElementsInterface) mainFrame).getTextEditorPane().setText("");

            MessageModel messageModel = textToMessageModel(userTextInput);

            try {

                ((MainGuiElementsInterface) mainFrame).getWebsocketClient().send(((MainGuiElementsInterface) mainFrame).getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(messageModel));

            } catch (JsonProcessingException e) {

                throw new RuntimeException(e);
            }
        }
    }

    private MessageModel textToMessageModel(String userTextInput) {

        return new MessageModel("osman", userTextInput);
    }

    @Override
    public void onMessage(String message) {

        writeMessageToChatPanel(message);

        System.out.println("MYMETHOD!!!!");
        System.out.println("message = " + message);
    }

    private void writeMessageToChatPanel(String message) {

        JPanel jPanel = new JPanel();
        JLabel jLabel = new JLabel(message);
        jPanel.add(jLabel);
        if (mainFrame instanceof MainGuiElementsInterface) {
            ((MainGuiElementsInterface) mainFrame).getMainTextPanel().add(jPanel, "wrap");
            scrollMainPanelDownToLastMessage(((MainGuiElementsInterface) mainFrame).getMainTextBackgroundScrollPane());
        }
    }

    public void scrollMainPanelDownToLastMessage(JScrollPane scrollPane) {

        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(scrollPane.getMaximumSize().height));
    }
}
