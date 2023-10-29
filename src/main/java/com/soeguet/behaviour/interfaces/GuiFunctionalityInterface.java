package com.soeguet.behaviour.interfaces;

public interface GuiFunctionalityInterface {


    void overrideTransferHandlerOfTextPane();

    void clearTextPane();

    void internalNotificationHandling(String message);

    String getTextFromInput();

    String convertUserTextToJSON(String textFromInput);

    void sendMessageToSocket(String serializedTextFromInput);
}