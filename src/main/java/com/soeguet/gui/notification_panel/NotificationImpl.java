package com.soeguet.gui.notification_panel;

import com.soeguet.gui.interaction.ReplyPanelImpl;
import com.soeguet.gui.main_frame.MainFrameInterface;
import com.soeguet.gui.newcomment.helper.CommentInterface;
import com.soeguet.gui.notification_panel.generated.Notification;
import com.soeguet.model.jackson.BaseModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

public class NotificationImpl extends Notification {

    private final MainFrameInterface mainFrame;
    private final int MARGIN_TOP = 10;
    private Timer timer;

    private final BaseModel baseModel;

    public NotificationImpl(final MainFrameInterface mainFrame, final BaseModel baseModel) {

        super(null);
        this.baseModel = baseModel;
        super.setMaximumSize(new Dimension(400, 400));
        this.mainFrame = mainFrame;
    }

    @Override
    protected void thisWindowGainedFocus(final WindowEvent e) {

        SwingUtilities.invokeLater(() -> {

            ((JFrame) mainFrame).setAlwaysOnTop(true);
            ((JFrame) mainFrame).toFront();
            ((JFrame) mainFrame).repaint();
            ((JFrame) mainFrame).setAlwaysOnTop(false);
        });
    }

    @Override
    protected void notificationAllPanelMouseClicked(final MouseEvent e) {

        System.out.println("Clicked on notification all panel");
    }

    @Override
    protected void notificationReplySendActionPerformed(final ActionEvent e) {

        SwingUtilities.invokeLater(() -> {

            ((JFrame) mainFrame).setAlwaysOnTop(true);
            ((JFrame) mainFrame).toFront();
            ((JFrame) mainFrame).repaint();
            ((JFrame) mainFrame).setAlwaysOnTop(false);

            ReplyPanelImpl replyPanel = new ReplyPanelImpl(mainFrame, baseModel);
            mainFrame.getMainTextPanelLayeredPane().add(replyPanel, JLayeredPane.MODAL_LAYER);
        });
    }

    @Override
    protected void closeAllNotificationsActionPerformed(final ActionEvent e) {

        mainFrame.getNotificationList().forEach(notification -> {

            if (notification.getTimer() != null && notification.getTimer().isRunning()) {

                notification.getTimer().stop();
            }

            notification.setVisible(false);
            notification.dispose();
        });
    }

    public int relocateNotification(int moveUpByDeltaPixel) {

        int locationY = getY() - moveUpByDeltaPixel;

        if (locationY < 0) {

            locationY = 0;
        }

        setBounds(getX(), locationY + MARGIN_TOP, getWidth(), getHeight());
        repaint();

        return locationY + MARGIN_TOP + getHeight();
    }

    public void setNotificationText(String text, String message) {

        addMessageToNotificationPanel(text, message);
        final int screenResolutionWidth = determineScreenWidth();

        int newYPosition = mainFrame.getNotificationPositionY();

        modifyNotificationPanel(screenResolutionWidth, newYPosition);

        retainInformationAboutThisNotification(newYPosition);

        addNotificationTimer();
    }

    private void addMessageToNotificationPanel(String sender, String message) {

        form_nameLabel.setText(sender);
        form_notificationMainMessage.setText(message);

        final int messageLength = message.length();

        if (messageLength < 30) {

            adjustNotificationPanelHeight(1);

        } else if (messageLength > 90) {

            adjustNotificationPanelHeight(2);

        } else if (messageLength > 60) {

            adjustNotificationPanelHeight(3);
        }
    }

    private static int determineScreenWidth() {

        final Dimension primaryScreenSize = Toolkit.getDefaultToolkit().getScreenSize();

        return (int) primaryScreenSize.getWidth();
    }

    private void modifyNotificationPanel(final int screenResolutionWidth, final int newYPosition) {

        int MARGIN_RIGHT = 10;
        setBounds(screenResolutionWidth - getWidth() - MARGIN_RIGHT, newYPosition + MARGIN_TOP, getWidth(), getHeight());
        setAlwaysOnTop(true);
        setVisible(true);
    }

    private void retainInformationAboutThisNotification(final int newYPosition) {

        mainFrame.setNotificationPositionY(newYPosition + getHeight() - MARGIN_TOP);
        mainFrame.getNotificationList().add(this);
    }

    private void addNotificationTimer() {

        timer = new Timer(5000, e -> {

            mainFrame.getNotificationList().remove(this);
            final int notificationTextHeight = getHeight();
            setVisible(false);
            dispose();

            if (mainFrame.getNotificationList().isEmpty()) {

                mainFrame.setNotificationPositionY(0);
                return;
            }

            mainFrame.triggerRelocationActiveNotification(notificationTextHeight);
        });
        timer.setRepeats(false);
        timer.start();

    }

    private void adjustNotificationPanelHeight(final int factor) {

        //some very ugly manual adjustments..
        final int notificationTextHeight = 25 * factor;

        //components related stuff
        final Dimension notificationPanelSize = new Dimension(form_notificationMainPanel.getWidth(), notificationTextHeight - 50);
        form_notificationMainPanel.setPreferredSize(notificationPanelSize);
        form_notificationMainPanel.setSize(notificationPanelSize);
        form_notificationMainPanel.setMinimumSize(notificationPanelSize);
        form_notificationMainPanel.setMaximumSize(notificationPanelSize);

        final Dimension scrollPaneSize = new Dimension(form_scrollPane1.getWidth(), notificationTextHeight + 5);
        form_scrollPane1.setPreferredSize(scrollPaneSize);
        form_scrollPane1.setSize(scrollPaneSize);
        form_scrollPane1.setMinimumSize(scrollPaneSize);
        form_scrollPane1.setMaximumSize(scrollPaneSize);

        final Dimension notificationTextSize = new Dimension(form_notificationMainMessage.getWidth(), notificationTextHeight + 5);
        form_notificationMainMessage.setPreferredSize(notificationTextSize);
        form_notificationMainMessage.setSize(notificationTextSize);
        form_notificationMainMessage.setMinimumSize(notificationTextSize);
        form_notificationMainMessage.setMaximumSize(notificationTextSize);

        //panel related stuff
        final int adjustmentInHeight = 80;

        final Dimension thisSize = new Dimension(getWidth(), adjustmentInHeight + notificationTextHeight);
        this.setPreferredSize(thisSize);
        this.setSize(thisSize);
        this.setMinimumSize(thisSize);
        this.setMaximumSize(thisSize);

        final Dimension panelSize = new Dimension(form_notificationAllPanel.getWidth(), adjustmentInHeight + notificationTextHeight);
        form_notificationAllPanel.setPreferredSize(panelSize);
        form_notificationAllPanel.setSize(panelSize);
        form_notificationAllPanel.setMinimumSize(panelSize);
        form_notificationAllPanel.setMaximumSize(panelSize);

        final Dimension mainPanelSize = new Dimension(form_notificationMainPanel.getWidth(), adjustmentInHeight + notificationTextHeight);
        form_notificationMainPanel.setPreferredSize(mainPanelSize);
        form_notificationMainPanel.setSize(mainPanelSize);
        form_notificationMainPanel.setMinimumSize(mainPanelSize);
        form_notificationMainPanel.setMaximumSize(mainPanelSize);

        pack();
        revalidate();
        repaint();
    }

    public Timer getTimer() {

        return timer;
    }

    public void addBubble(CommentInterface commentInterface) {

        form_notificationMainPanel.add((JPanel) commentInterface, "cell 0 1, wrap");
        setVisible(true);
        revalidate();
        repaint();
    }
}