package com.soeguet.notification_panel.interfaces;

import com.soeguet.notification_panel.enums.NotificationStatus;

public interface NotificationStatusHandlerInterface {

  /**
   * Retrieves the current status of notifications.
   *
   * @return The current NotificationStatus.
   */
  NotificationStatus getNotificationStatus();
}
