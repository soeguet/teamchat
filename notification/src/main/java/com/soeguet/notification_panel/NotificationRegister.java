package com.soeguet.notification_panel;

import com.soeguet.notification_panel.enums.NotificationStatus;
import com.soeguet.notification_panel.interfaces.DesktopNotificationHandlerInterface;
import java.util.ArrayList;
import java.util.List;

public class NotificationRegister {

  private final List<NotificationImpl> notificationList = new ArrayList<>();

  /**
   * Represents the Y position of a notification. Will be updated everytime a notification is
   * generated
   */
  private volatile int notificationPositionY = 0;

  public int getNotificationPositionY() {

    return notificationPositionY;
  }

  public void setNotificationPositionY(final int notificationPositionY) {

    this.notificationPositionY = notificationPositionY;
  }

  private static final NotificationRegister notificationRegister = new NotificationRegister();

  private NotificationRegister() {}

  public static NotificationRegister getInstance() {

    return notificationRegister;
  }

  /**
   * Gets the list of notifications.
   *
   * @return the list of notifications.
   */
  public List<NotificationImpl> getNotificationList() {

    return this.notificationList;
  }

  /**
   * Handles internal notifications by creating desktop notifications.
   *
   * @param message The message to be displayed in the desktop notification.
   * @throws NullPointerException if the message is null.
   */
  public void internalNotificationHandling(final String message) throws NullPointerException {

    // Create a desktop notification handler
    DesktopNotificationHandlerInterface desktopNotificationHandler =
        new DesktopNotificationHandler();

    // Determine the desktop notification status
    final NotificationStatus notificationStatus =
        desktopNotificationHandler.determineDesktopNotificationStatus();

    // Create a desktop notification with the given message and status
    desktopNotificationHandler.createDesktopNotification(message, notificationStatus);
  }
}
