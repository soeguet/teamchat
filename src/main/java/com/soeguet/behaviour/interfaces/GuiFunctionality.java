package com.soeguet.behaviour.interfaces;

public interface GuiFunctionality {

    void fixScrollPaneScrollSpeed();

    void overrideTransferHandlerOfTextPane();

    void clearTextPaneAndSendMessageToSocket();

    void internalNotificationHandling(String message);
}