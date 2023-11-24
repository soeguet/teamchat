package com.soeguet.behaviour.interfaces;

import com.soeguet.model.jackson.MessageModel;

public interface GuiFunctionalityInterface {


    void overrideTransferHandlerOfTextPane();

    void internalNotificationHandling(String message);

    String getTextFromInput();

    String convertUserTextToJSON(MessageModel messageModel);

    void sendMessageToSocket(String serializedTextFromInput);

    void notifyClientsSendStatus();

    void clearTextPane();
}