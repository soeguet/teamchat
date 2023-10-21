package com.soeguet.gui.typing_panel;

import com.fasterxml.jackson.databind.JsonNode;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.gui.typing_panel.interfaces.TypingPanelHandlerInterface;

import javax.swing.*;

public class TypingPanelHandler implements TypingPanelHandlerInterface {

    private final MainFrameGuiInterface mainFrame;

    public TypingPanelHandler(final MainFrameGuiInterface mainFrame) {

        this.mainFrame = mainFrame;
    }

    @Override
    public String retrieveTextOnTypingLabel() {

        return mainFrame.getTypingLabel().getText();
    }

    /**
     * Generate a typing label based on the given text and parsed JSON object.
     *
     * @param textOnTypingLabel The text to be displayed on the typing label.
     * @param parsedJson The parsed JSON object containing additional information.
     * @return A StringBuilder object representing the generated typing label.
     */
    @Override
    public StringBuilder generateTypingLabel(final String textOnTypingLabel, final JsonNode parsedJson) {

        StringBuilder stringBuilder = new StringBuilder();

        if (!textOnTypingLabel.isBlank()) {

            String textOnTypingLabelModified = textOnTypingLabel.replace(" is typing...", "");
            stringBuilder.append(textOnTypingLabelModified);
            stringBuilder.append(", ");

        } else {

            stringBuilder.append("  ");
        }

        stringBuilder.append(parsedJson.get("username").asText());
        stringBuilder.append(" is typing...");

        return stringBuilder;
    }

    @Override
    public void displayUpdatedTypingLabel(final StringBuilder stringBuilder) {

        SwingUtilities.invokeLater(() -> mainFrame.getTypingLabel().setText(stringBuilder.toString()));
    }
}