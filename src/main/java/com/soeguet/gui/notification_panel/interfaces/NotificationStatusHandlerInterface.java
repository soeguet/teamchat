package com.soeguet.gui.notification_panel.interfaces;

import com.soeguet.util.NotificationStatus;

public interface NotificationStatusHandlerInterface {

    /**
     * Retrieves the current status of notifications.
     *
     * @return The current NotificationStatus.
     */
    NotificationStatus getNotificationStatus();
}