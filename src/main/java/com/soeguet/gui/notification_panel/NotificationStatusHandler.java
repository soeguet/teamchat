package com.soeguet.gui.notification_panel;

import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.gui.notification_panel.interfaces.NotificationStatusHandlerInterface;
import com.soeguet.util.NotificationStatus;

public class NotificationStatusHandler implements NotificationStatusHandlerInterface {
    private final MainFrameGuiInterface mainFrame;

    public NotificationStatusHandler(final MainFrameGuiInterface mainFrame) {

        this.mainFrame = mainFrame;
    }

    /**
     * Retrieves the current notification status based on the selected menu items in the main frame.
     *
     * @return the current notification status
     */
    @Override
    public NotificationStatus getNotificationStatus() {

        // on program startup
        if (mainFrame.isStartUp()) {

            return NotificationStatus.ALL_DENIED;
        }

        // all = no -- needs to be first since it will reset after 5 minutes
        if (mainFrame.getAllNotificationMenuItem().isSelected()) {

            return NotificationStatus.ALL_DENIED;
        }

        // external = yes && internal = yes
        if (mainFrame.getInternalNotificationsMenuItem().isSelected()
                && mainFrame.getExternalNotificationsMenuItem().isSelected()) {

            return NotificationStatus.ALL_ALLOWED;
        }

        // external = yes && internal = no
        if (mainFrame.getExternalNotificationsMenuItem().isSelected()
                && !mainFrame.getInternalNotificationsMenuItem().isSelected()) {

            return NotificationStatus.EXTERNAL_ONLY;
        }

        // external = no && internal = yes
        if (!mainFrame.getExternalNotificationsMenuItem().isSelected()
                && mainFrame.getInternalNotificationsMenuItem().isSelected()) {

            return NotificationStatus.INTERNAL_ONLY;
        }

        return NotificationStatus.ALL_DENIED;
    }
}
