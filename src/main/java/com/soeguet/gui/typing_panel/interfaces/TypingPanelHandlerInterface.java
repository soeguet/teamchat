package com.soeguet.gui.typing_panel.interfaces;

public interface TypingPanelHandlerInterface {

    String retrieveTextOnTypingLabel();

    StringBuilder generateTypingLabel(String textOnTypingLabel, String typingUsername);

    void displayUpdatedTypingLabel(StringBuilder stringBuilder);
}
