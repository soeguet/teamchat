package com.soeguet.notification_panel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.cache.factory.CacheManagerFactory;
import com.soeguet.cache.implementations.ActiveNotificationQueue;
import com.soeguet.cache.implementations.WaitingNotificationQueue;
import com.soeguet.cache.manager.CacheManager;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.LinkModel;
import com.soeguet.model.jackson.MessageModel;
import com.soeguet.model.jackson.PictureModel;
import com.soeguet.notification_panel.enums.NotificationStatus;
import com.soeguet.notification_panel.interfaces.DesktopNotificationHandlerInterface;
import com.soeguet.notification_panel.interfaces.NotificationStatusHandlerInterface;
import com.soeguet.util.EnvironmentData;
import dorkbox.notify.Notify;
import dorkbox.notify.Position;
import java.awt.*;
import java.util.logging.Logger;
import javax.swing.*;

public class DesktopNotificationHandler implements DesktopNotificationHandlerInterface {

  // variables -- start
  private final Logger logger = Logger.getLogger(DesktopNotificationHandler.class.getName());
  private final CacheManager cacheManager = CacheManagerFactory.getCacheManager();
  // variables -- end

  // constructors -- start
  public DesktopNotificationHandler() {}

  // constructors -- end

  /**
   * Handles the remaining capacity in the active notifications queue.
   *
   * @param activeNotificationsCache The active notifications cache.
   */
  private void handleRemainingCapacityInQueue(ActiveNotificationQueue activeNotificationsCache) {

    if (cacheManager.getCache("WaitingNotificationQueue")
        instanceof WaitingNotificationQueue waitingNotificationQueue) {

      // if there is still space in the active notifications queue and there are still
      // notifications in the waiting queue
      if (activeNotificationsCache.getRemainingCapacity() > 0
          && !waitingNotificationQueue.isEmpty()) {

        final String nextNotification = waitingNotificationQueue.pollFirst();
        final BaseModel baseModel = convertMessageToBaseModel(nextNotification);

        displayUpToThreeNotifications(baseModel, activeNotificationsCache);
      }
    }
  }

  /**
   * Creates a notification based on the given BaseModel object.
   *
   * @param baseModel The BaseModel object representing the notification content.
   * @throws IllegalArgumentException if the BaseModel object is of unknown type.
   */
  private void createNotification(final BaseModel baseModel) {

    if (baseModel instanceof MessageModel messageModel) {

      Timer timer =
          new Timer(
              500,
              e -> {
                com.soeguet.gui.notification_panel.interfaces.NotificationInterface notification =
                    new NotificationImpl(messageModel);
                notification.setNotificationText();
                notification.setMaximumSize(new Dimension(400, 300));
              });
      timer.setRepeats(false);
      timer.start();

    } else if (baseModel instanceof PictureModel pictureModel) {

      Timer timer =
          new Timer(
              500,
              e -> {
                com.soeguet.gui.notification_panel.interfaces.NotificationInterface notification =
                    new NotificationImpl(pictureModel);

                notification.setNotificationPicture();
                notification.setMaximumSize(new Dimension(400, 300));
              });
      timer.setRepeats(false);
      timer.start();

    } else if (baseModel instanceof LinkModel linkModel) {

      // TODO 1 ?? something is missing?
      internalNotificationHandling(linkModel.getComment(), baseModel);

    } else {

      logger.info("Unknown message type");
      throw new IllegalArgumentException();
    }
  }

  /**
   * Handles internal notifications by adding them to the active notification queue and creating
   * notifications.
   *
   * @param message The message to be handled.
   * @param baseModel The base model associated with the notification.
   * @throws RuntimeException if there is an IllegalStateException while adding to the active
   *     notification queue.
   */
  private synchronized void internalNotificationHandling(String message, BaseModel baseModel) {

    Notify notify = Notify.Companion.create();
    notify.title(baseModel.getTime() + " " + baseModel.getSender());
    notify.position(Position.TOP_RIGHT);
    notify.setHideAfterDurationInMillis(3000);

    // let icon blink even with turned off notifications
    if (baseModel instanceof MessageModel messageModel) {
      notify.text(messageModel.getMessage());
    } else if (baseModel instanceof PictureModel pictureModel) {
      notify.text("[picture] " + pictureModel.getDescription());
    } else if (baseModel instanceof LinkModel linkModel) {
      notify.text("[link] " + linkModel.getComment());
    }

    notify.show();
  }

  /**
   * Displays up to three notifications.
   *
   * @param baseModel The BaseModel object representing the notification to display.
   * @param activeNotificationQueue The ActiveNotificationQueue object to add the notification to.
   * @throws RuntimeException if there is an IllegalStateException while adding to the active
   *     notification queue.
   */
  private void displayUpToThreeNotifications(
      final BaseModel baseModel, final ActiveNotificationQueue activeNotificationQueue) {

    activeNotificationQueue.addLast(baseModel);
    createNotification(baseModel);
    handleRemainingCapacityInQueue(activeNotificationQueue);
  }

  /**
   * Handles external notifications based on the given message.
   *
   * @param message The message to be handled.
   * @throws RuntimeException if there is an IOException while handling the notification.
   */
  private void externalNotificationHandling(final String message) {

    final BaseModel baseModel = convertMessageToBaseModel(message);
    com.soeguet.gui.notification_panel.interfaces.NotificationDisplayInterface
        notificationDisplayInterface;

    final String osName = EnvironmentData.getOSName();

    if (baseModel instanceof MessageModel messageModel) {

      switch (osName) {
          // TODO 1 -- change the way of sending notifications on Linux
        case "Linux" -> {
          // notificationDisplayInterface = new NotificationDisplayLinux();
          // notificationDisplayInterface.displayNotification(text.getSender(), text.getMessage());
        }

        case "Windows 10", "WINDOWS_NT" -> {
          notificationDisplayInterface = new NotificationDisplayWindows();
          notificationDisplayInterface.displayNotification(
              messageModel.getSender(), messageModel.getMessage());
        }
      }

    } else if (baseModel instanceof PictureModel pictureModel) {
      switch (osName) {
        case "Linux" -> {
          notificationDisplayInterface = new NotificationDisplayLinux();
          notificationDisplayInterface.displayNotification(
              pictureModel.getSender(),
              "[picture]" + System.lineSeparator() + pictureModel.getDescription());
        }

        case "Windows 10", "WINDOWS_NT" -> {
          notificationDisplayInterface = new NotificationDisplayWindows();
          notificationDisplayInterface.displayNotification(
              pictureModel.getSender(),
              "[picture]" + System.lineSeparator() + pictureModel.getDescription());
        }
      }

    } else if (baseModel instanceof LinkModel linkModel) {

      throw new RuntimeException("LinkModel not supported yet");
    }
  }

  /**
   * Adds an incoming notification to the waiting notification queue.
   *
   * @param message The message of the incoming notification.
   * @throws RuntimeException if there is an IllegalStateException while adding to the waiting
   *     notification queue.
   */
  // private void addIncomingNotificationToQueue(final String message) {
  //
  //     if (cacheManager.getCache(
  //             "WaitingNotificationQueue") instanceof WaitingNotificationQueue
  // waitingNotificationQueue) {
  //
  //         // add to queue and skip the rest
  //         waitingNotificationQueue.addLast(message);
  //     }
  // }

  /**
   * Handles internal notifications by converting a message into a base model and calling another
   * internalNotificationHandling method.
   *
   * @param message The notification message to handle.
   */
  public synchronized void internalNotificationHandling(String message) {

    BaseModel baseModel = convertMessageToBaseModel(message);

    // send to overloaded method
    internalNotificationHandling(message, baseModel);
  }

  /**
   * Converts a JSON string to a MessageModel object.
   *
   * @param message the JSON string to be converted
   * @return the MessageModel object representing the converted JSON string
   * @throws IllegalArgumentException if the JSON string is malformed
   */
  private BaseModel convertMessageToBaseModel(String message) {

    try {

      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(message, BaseModel.class);

    } catch (JsonProcessingException e) {

      throw new IllegalArgumentException("Malformed message", e);
    }
  }

  @Override
  public NotificationStatus determineDesktopNotificationStatus() {

    NotificationStatusHandlerInterface notificationStatusHandler = new NotificationStatusHandler();

    return notificationStatusHandler.getNotificationStatus();
  }

  /**
   * Creates a desktop notification with the given message and notification status.
   *
   * @param message The message to be displayed in the desktop notification.
   * @param notificationStatus The status of the notification, which determines how it is handled.
   *     Possible values are: - INTERNAL_ONLY: Only an internal notification is displayed. -
   *     EXTERNAL_ONLY: Only an external notification is displayed. - ALL_ALLOWED: Both internal and
   *     external notifications are displayed. - ALL_DENIED: No notification is displayed. -
   *     STARTUP: No notification is displayed.
   * @throws NullPointerException if the message is null.
   */
  @Override
  public void createDesktopNotification(
      final String message, final NotificationStatus notificationStatus)
      throws NullPointerException {

    // check if notifications are even wanted
    switch (notificationStatus) {
      case INTERNAL_ONLY -> {
        internalNotificationHandling(message);
        Toolkit.getDefaultToolkit().beep();
      }

      case EXTERNAL_ONLY -> {
        externalNotificationHandling(message);
        Toolkit.getDefaultToolkit().beep();
      }

      case ALL_ALLOWED -> {
        internalNotificationHandling(message);
        externalNotificationHandling(message);
        Toolkit.getDefaultToolkit().beep();
      }

      case ALL_DENIED, STARTUP -> {}

      default -> throw new IllegalStateException("Unexpected value: " + notificationStatus);
    }
  }
}
