package com.soeguet.behaviour.interfaces;

public interface GuiFunctionality {

    void fixScrollPaneScrollSpeed();

    void overrideTransferHandlerOfTextPane();

    void sendMessageToSocket();
    void clearTextPane();

    void internalNotificationHandling(String message);
}