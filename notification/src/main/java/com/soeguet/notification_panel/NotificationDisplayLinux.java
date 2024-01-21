package com.soeguet.notification_panel;

import com.soeguet.gui.notification_panel.interfaces.NotificationDisplayInterface;
import java.io.IOException;

public class NotificationDisplayLinux implements NotificationDisplayInterface {

    /**
     * Displays a notification with the given sender and message using the `notify-send` command.
     *
     * @param sender The sender of the notification.
     * @param message The message of the notification.
     * @throws RuntimeException If an IO error occurs while executing the `notify-send` command.
     */
    @Override
    public void displayNotification(final String sender, final String message) {

        try {
            Runtime.getRuntime().exec(new String[] {"notify-send", sender, message});
        } catch (IOException e) {

            try {
                Runtime.getRuntime()
                        .exec(
                                new String[] {
                                    "kdialog", "--passivepopup", message, "5", "--title", sender
                                });
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}