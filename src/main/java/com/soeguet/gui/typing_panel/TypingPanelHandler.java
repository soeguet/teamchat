package com.soeguet.gui.typing_panel;

import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.gui.typing_panel.interfaces.TypingPanelHandlerInterface;
import javax.swing.SwingUtilities;

public class TypingPanelHandler implements TypingPanelHandlerInterface {

    private final MainFrameGuiInterface mainFrame;

    public TypingPanelHandler(final MainFrameGuiInterface mainFrame) {

        this.mainFrame = mainFrame;
    }

    @Override
    public String retrieveTextOnTypingLabel() {

        return mainFrame.getTypingLabel().getText();
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
                () -> mainFrame.getTypingLabel().setText(stringBuilder.toString()));
    }
}
