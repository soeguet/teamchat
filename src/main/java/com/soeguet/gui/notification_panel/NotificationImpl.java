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
    private final int MARGIN_RIGHT = 10;

    public NotificationImpl(final MainFrameInterface mainFrame) {

        super(null);
        this.mainFrame = mainFrame;

        form_notificationMainPanel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(final MouseEvent e) {

                SwingUtilities.invokeLater(() -> {

                    ((JFrame) mainFrame).setAlwaysOnTop(true);
                    ((JFrame) mainFrame).toFront();
                    ((JFrame) mainFrame).repaint();
                    ((JFrame) mainFrame).setAlwaysOnTop(false);
                });

                System.out.println("Clicked on notification main panel");
            }
        });
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

    public void relocateNotification(int positionY) {

        int locationY = getY() - positionY;

        if (locationY < 0) {

            locationY = 0;
        }

        setBounds(getX(), locationY + MARGIN_TOP, getWidth(), getHeight());
        repaint();
    }

    public void setNotificationText(String text) {

        //TODO clean this mess up
        final Label textLabel = new Label(text);
        textLabel.addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseClicked(final MouseEvent e) {

                SwingUtilities.invokeLater(() -> {

                    ((JFrame) mainFrame).setAlwaysOnTop(true);
                    ((JFrame) mainFrame).toFront();
                    ((JFrame) mainFrame).repaint();
                    ((JFrame) mainFrame).setAlwaysOnTop(false);
                });

                System.out.println("Clicked on notification text label");

            }
        });

        this.form_notificationMainPanel.add(textLabel, BorderLayout.CENTER);

        pack();

        final Dimension primaryScreenSize = Toolkit.getDefaultToolkit().getScreenSize();

        final int width = (int) primaryScreenSize.getWidth();

        int newYPosition = mainFrame.getNotificationPositionY();

        if (newYPosition < 0) {

            newYPosition = 0;
        }

        setBounds(width - getWidth() - MARGIN_RIGHT, newYPosition + MARGIN_TOP, getWidth(), getHeight());

        mainFrame.setNotificationPositionY(newYPosition + getHeight() - MARGIN_TOP);

        setAlwaysOnTop(true);
        setVisible(true);

        mainFrame.getNotificationList().add(this);

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