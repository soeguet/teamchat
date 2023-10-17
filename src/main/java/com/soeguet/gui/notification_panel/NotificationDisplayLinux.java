package com.soeguet.gui.notification_panel;

import com.soeguet.gui.notification_panel.interfaces.NotificationDisplayInterface;

import java.io.IOException;

public class NotificationDisplayLinux implements NotificationDisplayInterface {

    @Override
    public void displayNotification(final String sender, final String message) {

        try {
            Runtime.getRuntime().exec(new String[]{"notify-send", sender, message});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}