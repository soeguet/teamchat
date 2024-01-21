package com.soeguet.typing_panel;

import com.soeguet.main_frame.ChatMainFrameImpl;
import com.soeguet.typing_panel.interfaces.TypingPanelHandlerInterface;

import javax.swing.*;

public class TypingPanelHandler implements TypingPanelHandlerInterface {

    public TypingPanelHandler() {

    }

    @Override
    public String retrieveTextOnTypingLabel() {


        return ChatMainFrameImpl.getMainFrameInstance().getTypingLabel().getText();
    }

    @Override
    public StringBuilder generateTypingLabel(
            final String textOnTypingLabel, final String typingUsername) {

        StringBuilder stringBuilder = new StringBuilder();

        if (!textOnTypingLabel.isBlank()) {

            String textOnTypingLabelModified = textOnTypingLabel.replace(" is typing...", "");
            stringBuilder.append(textOnTypingLabelModified);
            stringBuilder.append(", ");

        } else {

            stringBuilder.append("  ");
        }

        stringBuilder.append(typingUsername);
        stringBuilder.append(" is typing...");

        return stringBuilder;
    }

    @Override
    public void displayUpdatedTypingLabel(final StringBuilder stringBuilder) {

        SwingUtilities.invokeLater(
                () -> ChatMainFrameImpl.getMainFrameInstance().getTypingLabel().setText(stringBuilder.toString()));
    }
}