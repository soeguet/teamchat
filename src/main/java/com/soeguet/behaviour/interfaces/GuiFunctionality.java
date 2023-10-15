package com.soeguet.behaviour.interfaces;

import com.soeguet.util.NotificationStatus;

public interface GuiFunctionality {

    void fixScrollPaneScrollSpeed();

    void overrideTransferHandlerOfTextPane();

    void sendMessageToSocket();
    void clearTextPane();

    void internalNotificationHandling(String message);
}