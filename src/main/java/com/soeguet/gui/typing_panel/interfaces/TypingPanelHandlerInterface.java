package com.soeguet.gui.typing_panel.interfaces;

import com.fasterxml.jackson.databind.JsonNode;

public interface TypingPanelHandlerInterface {

    String retrieveTextOnTypingLabel();

    StringBuilder generateTypingLabel(String textOnTypingLabel, JsonNode parsedJson);

    void displayUpdatedTypingLabel(StringBuilder stringBuilder);
}