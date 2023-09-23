package com.soeguet.gui.notification_panel;

import com.soeguet.gui.main_frame.MainFrameInterface;
import com.soeguet.gui.notification_panel.generated.Notification;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

public class NotificationImpl extends Notification {

    private final MainFrameInterface mainFrame;
    private final int MARGIN_TOP = 10;

    public NotificationImpl(final MainFrameInterface mainFrame) {

        super(null);
        this.mainFrame = mainFrame;
    }

    @Override
    protected void notificationReplySendActionPerformed(final ActionEvent e) {

        System.out.println("Clicked on notification reply send button");
    }

    @Override
    protected void notificationAllPanelMouseClicked(final MouseEvent e) {

        System.out.println("Clicked on notification all panel");
    }

    @Override
    protected void thisWindowGainedFocus(final WindowEvent e) {

        System.out.println("Notification gained focus");

        SwingUtilities.invokeLater(() -> {

            ((JFrame) mainFrame).setAlwaysOnTop(true);
            ((JFrame) mainFrame).toFront();
            ((JFrame) mainFrame).repaint();
            ((JFrame) mainFrame).setAlwaysOnTop(false);
        });
    }

    public void relocateNotification(int moveUpByDeltaPixel) {

        int locationY = getY() - moveUpByDeltaPixel;

        if (locationY < 0) {

            locationY = 0;
        }

        setBounds(getX(), locationY + MARGIN_TOP, getWidth(), getHeight());
        repaint();
    }

    public void setNotificationText(String text) {

        addMessageToNotificationPanel(text);

        pack();

        final int screenResolutionWidth = determineScreenWidth();

        int newYPosition = mainFrame.getNotificationPositionY();

        modifyNotificationPanel(screenResolutionWidth, newYPosition);

        retainInformationAboutThisNotification(newYPosition);

        addNotificationTimer();
    }

    private void addMessageToNotificationPanel(final String text) {

        final Label textLabel = new Label(text);
        this.form_notificationMainPanel.add(textLabel, BorderLayout.CENTER);
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

        Timer timer = new Timer(7500, e -> {

            mainFrame.getNotificationList().remove(this);
            final int notificationTextHeight = getHeight();
            setVisible(false);
            dispose();

            mainFrame.triggerRelocationActiveNotification(notificationTextHeight);
        });
        timer.setRepeats(false);
        timer.start();
    }
}