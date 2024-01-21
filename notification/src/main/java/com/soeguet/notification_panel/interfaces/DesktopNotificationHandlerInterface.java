package com.soeguet.notification_panel.interfaces;

import com.soeguet.notification_panel.enums.NotificationStatus;

public interface DesktopNotificationHandlerInterface {

    NotificationStatus determineDesktopNotificationStatus();

    void createDesktopNotification(String message, NotificationStatus notificationStatus);
}