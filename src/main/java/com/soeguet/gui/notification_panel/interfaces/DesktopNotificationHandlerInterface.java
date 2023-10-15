package com.soeguet.gui.notification_panel.interfaces;

import com.soeguet.util.NotificationStatus;

public interface DesktopNotificationHandlerInterface {

    NotificationStatus determineDesktopNotificationStatus();

    void createDesktopNotification(String message, NotificationStatus notificationStatus);
}