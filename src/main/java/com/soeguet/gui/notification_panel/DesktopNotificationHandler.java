package com.soeguet.gui.notification_panel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.cache.factory.CacheManagerFactory;
import com.soeguet.cache.implementations.ActiveNotificationQueue;
import com.soeguet.cache.implementations.WaitingNotificationQueue;
import com.soeguet.cache.manager.CacheManager;
import com.soeguet.gui.main_frame.interfaces.MainFrameInterface;
import com.soeguet.gui.notification_panel.interfaces.DesktopNotificationHandlerInterface;
import com.soeguet.gui.notification_panel.interfaces.NotificationDisplayInterface;
import com.soeguet.gui.notification_panel.interfaces.NotificationInterface;
import com.soeguet.gui.notification_panel.interfaces.NotificationStatusHandlerInterface;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.MessageModel;
import com.soeguet.model.jackson.PictureModel;
import com.soeguet.util.NotificationStatus;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.logging.Logger;

public class DesktopNotificationHandler implements DesktopNotificationHandlerInterface {

    private final Logger logger = Logger.getLogger(DesktopNotificationHandler.class.getName());
    private final MainFrameInterface mainFrame;
    CacheManager cacheManager = CacheManagerFactory.getCacheManager();
    public DesktopNotificationHandler(final MainFrameInterface mainFrame) {

        this.mainFrame = mainFrame;
    }

    @Override
    public NotificationStatus determineDesktopNotificationStatus() {

        NotificationStatusHandlerInterface notificationStatusHandler = new NotificationStatusHandler(this.mainFrame);

        return notificationStatusHandler.getNotificationStatus();
    }

    /**
     Handles the remaining capacity in the active notifications queue.

     @param activeNotificationsCache The active notifications cache.
     */
    private void handleRemainingCapacityInQueue(ActiveNotificationQueue activeNotificationsCache) {

        if (cacheManager.getCache("WaitingNotificationQueue") instanceof WaitingNotificationQueue waitingNotificationQueue) {

            //if less than three active notifications are present and there are waiting notifications
            if (activeNotificationsCache.getRemainingCapacity() > 0 && !waitingNotificationQueue.isEmpty()) {

                final String queuedNotification = waitingNotificationQueue.pollFirst();

                //TODO is this one right?
                Timer timer = new Timer(250, e -> internalNotificationHandling(queuedNotification));
                timer.setRepeats(false);
                timer.start();
            }
        }
    }

    /**
     Creates a notification based on the given BaseModel object.

     @param baseModel The BaseModel object representing the notification content.

     @throws IllegalArgumentException if the BaseModel object is of unknown type.
     */
    private void createNotification(final BaseModel baseModel) {

        //TODO does this need to be reactivated somewhere?
        switch (baseModel) {

            case MessageModel text -> {

                Timer timer = new Timer(500, e -> {
                    NotificationInterface notification = new NotificationImpl(this.mainFrame, text);
                    notification.setNotificationText();
                    notification.setMaximumSize(new Dimension(400, 300));
                });
                timer.setRepeats(false);
                timer.start();
            }

            case PictureModel picture -> {

                Timer timer = new Timer(500, e -> {
                    NotificationInterface notification = new NotificationImpl(this.mainFrame, picture);

                    notification.setNotificationPicture();
                    notification.setMaximumSize(new Dimension(400, 300));

                });
                timer.setRepeats(false);
                timer.start();
            }

            default -> {

                logger.info("Unknown message type");
                throw new IllegalArgumentException();
            }
        }
    }
    @Override
    public void createDesktopNotification(final String message, final NotificationStatus notificationStatus) {

        //check if notifications are even wanted
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

            case ALL_DENIED, STARTUP -> {
                //TODO check if message was even cached which in that case needs to be removed from cache
                //do nothing, intentionally left blank
                break;
            }
        }
    }

    /**
     Handles internal notifications by adding them to the active notification queue and creating notifications.

     @param message   The message to be handled.
     @param baseModel The base model associated with the notification.

     @throws RuntimeException if there is an IllegalStateException while adding to the active notification queue.
     */
    private synchronized void internalNotificationHandling(String message, BaseModel baseModel) {

        if (cacheManager.getCache("ActiveNotificationQueue") instanceof ActiveNotificationQueue activeNotificationQueue) {

            //max 3 notification at a time, cache message and bail out
            if (activeNotificationQueue.getRemainingCapacity() == 0) {

                if (cacheManager.getCache("WaitingNotificationQueue") instanceof WaitingNotificationQueue waitingNotificationQueue) {

                    //add to queue and skip the rest
                    waitingNotificationQueue.addLast(message);
                }

            } else {

                //if capacity is left
                try {

                    activeNotificationQueue.addLast(baseModel);

                } catch (IllegalStateException e) {

                    throw new RuntimeException(e);
                }

                createNotification(baseModel);
                handleRemainingCapacityInQueue(activeNotificationQueue);
            }
        }
    }
    /**
     Handles external notifications based on the given message.

     @param message The message to be handled.

     @throws RuntimeException if there is an IOException while handling the notification.
     */
    private void externalNotificationHandling(final String message) {

        final BaseModel baseModel = convertMessageToBaseModel(message);
        NotificationDisplayInterface notificationDisplayInterface;

        switch (baseModel) {

            case MessageModel text -> {

                switch (mainFrame.getOSName()) {

                    case "Linux" -> {

                            notificationDisplayInterface = new NotificationDisplayLinux();
                            notificationDisplayInterface.displayNotification(text.getSender(), text.getMessage());
                    }

                    case "Windows 10", "WINDOWS_NT" -> {

                            notificationDisplayInterface = new NotificationDisplayWindows();
                            notificationDisplayInterface.displayNotification(text.getSender(), text.getMessage());
                    }
                }
            }

            case PictureModel picture -> {

                switch (mainFrame.getOSName()) {

                    case "Linux" -> {

                        notificationDisplayInterface = new NotificationDisplayLinux();
                        notificationDisplayInterface.displayNotification(picture.getSender(), "[picture]" + System.lineSeparator() + picture.getMessage());
                    }

                    case "Windows 10", "WINDOWS_NT" -> {

                        notificationDisplayInterface = new NotificationDisplayWindows();
                        notificationDisplayInterface.displayNotification(picture.getSender(), "[picture]" + System.lineSeparator() + picture.getMessage());
                    }
                }
            }
        }
    }
    /**
     Handles internal notifications by converting a message into a base model and calling
     another internalNotificationHandling method.

     @param message The notification message to handle.
     */
    public synchronized void internalNotificationHandling(String message) {

        BaseModel baseModel = convertMessageToBaseModel(message);
        internalNotificationHandling(message, baseModel);
    }

    /**
     Converts a JSON string to a MessageModel object.

     @param message the JSON string to be converted

     @return the MessageModel object representing the converted JSON string

     @throws IllegalArgumentException if the JSON string is malformed
     */
    private BaseModel convertMessageToBaseModel(String message) {

        try {

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(message, BaseModel.class);

        } catch (JsonProcessingException e) {

            throw new IllegalArgumentException("Malformed message", e);
        }
    }
}