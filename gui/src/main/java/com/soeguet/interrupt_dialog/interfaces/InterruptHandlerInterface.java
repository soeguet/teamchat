package com.soeguet.interrupt_dialog.interfaces;

import com.fasterxml.jackson.databind.JsonNode;

public interface InterruptHandlerInterface {

  void forceChatGuiToFront(String text);

  JsonNode extractJsonNodeUsernames(byte[] message);
}
