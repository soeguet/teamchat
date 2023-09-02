package com.soeguet.gui.behaviour;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pre.model.MessageModel;
import com.soeguet.gui.main_frame.MainGuiInterface;

import javax.swing.*;

public class GuiFunctionality {

    private final JFrame mainFrame;

    public GuiFunctionality(JFrame mainFrame) {

        this.mainFrame = mainFrame;
    }

    public void clearTextPaneAndSendMessageToSocket() {

        if (mainFrame instanceof MainGuiInterface) {

            String userTextInput = ((MainGuiInterface) mainFrame).getTextEditorPane().getText();
            ((MainGuiInterface) mainFrame).getTextEditorPane().setText("");

            MessageModel messageModel = textToMessageModel(userTextInput);

            try {

                ((MainGuiInterface) mainFrame).getWebsocketClient().send(((MainGuiInterface) mainFrame).getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(messageModel));

            } catch (JsonProcessingException e) {

                throw new RuntimeException(e);
            }
        }
    }

    private MessageModel textToMessageModel(String userTextInput) {

        return new MessageModel("osman", userTextInput);
    }

    public void scrollMainPanelDownToLastMessage(JScrollPane scrollPane) {

        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(scrollPane.getMaximumSize().height));
    }
}
