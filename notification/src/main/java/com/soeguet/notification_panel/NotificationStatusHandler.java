package com.soeguet.notification_panel;

import com.soeguet.notification_panel.enums.NotificationStatus;
import com.soeguet.notification_panel.interfaces.NotificationStatusHandlerInterface;

public class NotificationStatusHandler implements NotificationStatusHandlerInterface {

    public NotificationStatusHandler() {

    }

    @Override
    public NotificationStatus getNotificationStatus() {

        // TODO 1
        return NotificationStatus.ALL_ALLOWED;
    }
//
//    /**
//     * Retrieves the current notification status based on the selected menu items in the main frame.
//     *
//     * @return the current notification status
//     */
//    @Override
//    public NotificationStatus getNotificationStatus() {
//
//        ClientRegister clientRegister = ClientRegister.getWebSocketClientInstance();
//
//        // on program startup
//        if (clientRegister.isStartup()) {
//
//            return NotificationStatus.ALL_DENIED;
//        }
//
//        // all = no -- needs to be first since it will reset after 5 minutes
//        if (mainFrame.getAllNotificationMenuItem().isSelected()) {
//
//            return NotificationStatus.ALL_DENIED;
//        }
//
//        // external = yes && internal = yes
//        if (mainFrame.getInternalNotificationsMenuItem().isSelected()
//                && mainFrame.getExternalNotificationsMenuItem().isSelected()) {
//
//            return NotificationStatus.ALL_ALLOWED;
//        }
//
//        // external = yes && internal = no
//        if (mainFrame.getExternalNotificationsMenuItem().isSelected()
//                && !mainFrame.getInternalNotificationsMenuItem().isSelected()) {
//
//            return NotificationStatus.EXTERNAL_ONLY;
//        }
//
//        // external = no && internal = yes
//        if (!mainFrame.getExternalNotificationsMenuItem().isSelected()
//                && mainFrame.getInternalNotificationsMenuItem().isSelected()) {
//
//            return NotificationStatus.INTERNAL_ONLY;
//        }
//
//        return NotificationStatus.ALL_DENIED;
//    }
}