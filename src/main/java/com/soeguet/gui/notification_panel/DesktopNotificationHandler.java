package com.soeguet.gui.notification_panel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.cache.factory.CacheManagerFactory;
import com.soeguet.cache.implementations.ActiveNotificationQueue;
import com.soeguet.cache.implementations.WaitingNotificationQueue;
import com.soeguet.cache.manager.CacheManager;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.gui.notification_panel.interfaces.DesktopNotificationHandlerInterface;
import com.soeguet.gui.notification_panel.interfaces.NotificationDisplayInterface;
import com.soeguet.gui.notification_panel.interfaces.NotificationInterface;
import com.soeguet.gui.notification_panel.interfaces.NotificationStatusHandlerInterface;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.LinkModel;
import com.soeguet.model.jackson.MessageModel;
import com.soeguet.model.jackson.PictureModel;
import com.soeguet.util.NotificationStatus;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;
import javax.swing.Timer;

public class DesktopNotificationHandler implements DesktopNotificationHandlerInterface {

    private final Logger logger = Logger.getLogger(DesktopNotificationHandler.class.getName());
    private final MainFrameGuiInterface mainFrame;
    CacheManager cacheManager = CacheManagerFactory.getCacheManager();

    public DesktopNotificationHandler(final MainFrameGuiInterface mainFrame) {

        this.mainFrame = mainFrame;
    }

    @Override
    public NotificationStatus determineDesktopNotificationStatus() {

        NotificationStatusHandlerInterface notificationStatusHandler =
                new NotificationStatusHandler(this.mainFrame);

        return notificationStatusHandler.getNotificationStatus();
    }

    @Override
    public void createDesktopNotification(final String message,
            final NotificationStatus notificationStatus) {

        // force focus of gui on program
        Toolkit.getDefaultToolkit().getSystemEventQueue()
                .postEvent(new FocusEvent((Component) mainFrame, FocusEvent.FOCUS_GAINED));
        // let symbol on system task bar blink
        Toolkit.getDefaultToolkit().getSystemEventQueue()
                .postEvent(new WindowEvent((Window) mainFrame, WindowEvent.WINDOW_ACTIVATED));
        // ((Frame) this.mainFrame).setState(java.awt.Frame.ICONIFIED);
        ((Window) this.mainFrame).toFront();

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

            case ALL_DENIED, STARTUP -> {

                return;
            }
        }
    }

    /**
     * Handles the remaining capacity in the active notifications queue.
     * 
     * @param activeNotificationsCache The active notifications cache.
     */
    private void handleRemainingCapacityInQueue(ActiveNotificationQueue activeNotificationsCache) {

        if (cacheManager.getCache(
                "WaitingNotificationQueue") instanceof WaitingNotificationQueue waitingNotificationQueue) {

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
     * 
     * @throws IllegalArgumentException if the BaseModel object is of unknown type.
     */
    private void createNotification(final BaseModel baseModel) {

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
                    NotificationInterface notification =
                            new NotificationImpl(this.mainFrame, picture);

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

    /**
     * Handles internal notifications by adding them to the active notification queue and creating
     * notifications.
     * 
     * @param message The message to be handled.
     * @param baseModel The base model associated with the notification.
     * 
     * @throws RuntimeException if there is an IllegalStateException while adding to the active
     *         notification queue.
     */
    private synchronized void internalNotificationHandling(String message, BaseModel baseModel) {

        if (cacheManager.getCache(
                "ActiveNotificationQueue") instanceof ActiveNotificationQueue activeNotificationQueue) {

            // only post notification if there is no active notification -> trying to fix the wonky
            // behavior
            if (activeNotificationQueue.getRemainingCapacity() < 3) {

                addIncomingNotificationToQueue(message);

            } else {

                displayUpToThreeNotifications(baseModel, activeNotificationQueue);
            }
        }
    }

    /**
     * Adds an incoming notification to the waiting notification queue.
     * 
     * @param message The message of the incoming notification.
     * 
     * @throws RuntimeException if there is an IllegalStateException while adding to the waiting
     *         notification queue.
     */
    private void addIncomingNotificationToQueue(final String message) {

        if (cacheManager.getCache(
                "WaitingNotificationQueue") instanceof WaitingNotificationQueue waitingNotificationQueue) {

            // add to queue and skip the rest
            waitingNotificationQueue.addLast(message);
        }
    }

    /**
     * Displays up to three notifications.
     * 
     * @param baseModel The BaseModel object representing the notification to display.
     * @param activeNotificationQueue The ActiveNotificationQueue object to add the notification to.
     * 
     * @throws RuntimeException if there is an IllegalStateException while adding to the active
     *         notification queue.
     */
    private void displayUpToThreeNotifications(final BaseModel baseModel,
            final ActiveNotificationQueue activeNotificationQueue) {

        activeNotificationQueue.addLast(baseModel);
        createNotification(baseModel);
        handleRemainingCapacityInQueue(activeNotificationQueue);
    }

    /**
     * Handles external notifications based on the given message.
     * 
     * @param message The message to be handled.
     * 
     * @throws RuntimeException if there is an IOException while handling the notification.
     */
    private void externalNotificationHandling(final String message) {

        final BaseModel baseModel = convertMessageToBaseModel(message);
        NotificationDisplayInterface notificationDisplayInterface;

        switch (baseModel) {

            case MessageModel text -> {

                switch (mainFrame.getOSName()) {

                    case "Linux" -> {

                        notificationDisplayInterface = new NotificationDisplayLinux();
                        notificationDisplayInterface.displayNotification(text.getSender(),
                                text.getMessage());
                    }

                    case "Windows 10", "WINDOWS_NT" -> {

                        notificationDisplayInterface = new NotificationDisplayWindows();
                        notificationDisplayInterface.displayNotification(text.getSender(),
                                text.getMessage());
                    }
                }
            }

            case PictureModel picture -> {

                switch (mainFrame.getOSName()) {

                    case "Linux" -> {

                        notificationDisplayInterface = new NotificationDisplayLinux();
                        notificationDisplayInterface.displayNotification(picture.getSender(),
                                "[picture]" + System.lineSeparator() + picture.getDescription());
                    }

                    case "Windows 10", "WINDOWS_NT" -> {

                        notificationDisplayInterface = new NotificationDisplayWindows();
                        notificationDisplayInterface.displayNotification(picture.getSender(),
                                "[picture]" + System.lineSeparator() + picture.getDescription());
                    }
                }
            }

            case LinkModel link -> {

                throw new RuntimeException("LinkModel not supported yet");
            }
        }
    }

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
     * 
     * @return the MessageModel object representing the converted JSON string
     * 
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
}

