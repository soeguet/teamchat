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

    }

    @Override
    protected void thisWindowGainedFocus(final WindowEvent e) {

    }

    public void setNotificationText(String text) {

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
        final int height = (int) primaryScreenSize.getHeight();

        setBounds(width - getWidth() - 10, height - getHeight() - 10, getWidth(), getHeight());

        setAlwaysOnTop(true);
        setVisible(true);

        Timer timer = new Timer(5000, e -> {
            setVisible(false);
            dispose();
        });
        timer.setRepeats(false);
        timer.start();
    }
}