package com.soeguet.gui.notification_panel;

import com.soeguet.cache.factory.CacheManagerFactory;
import com.soeguet.cache.implementations.ActiveNotificationQueue;
import com.soeguet.cache.implementations.WaitingNotificationQueue;
import com.soeguet.cache.manager.CacheManager;
import com.soeguet.gui.reply.ReplyPanelImpl;
import com.soeguet.gui.main_frame.MainFrameInterface;
import com.soeguet.gui.notification_panel.generated.Notification;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.MessageModel;
import com.soeguet.model.jackson.PictureModel;
import com.soeguet.util.EmojiHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

public class NotificationImpl extends Notification {

    private final MainFrameInterface mainFrame;
    private final int MARGIN_TOP = 10;
    private final BaseModel baseModel;
    private Timer timer;

    public NotificationImpl(final MainFrameInterface mainFrame, final BaseModel baseModel) {

        super(null);
        this.baseModel = baseModel;
        super.setMaximumSize(new Dimension(400, 400));
        this.mainFrame = mainFrame;
    }

    @Override
    protected void thisWindowGainedFocus(final WindowEvent e) {

        SwingUtilities.invokeLater(() -> {

            ((JFrame) this.mainFrame).setAlwaysOnTop(true);
            ((JFrame) this.mainFrame).toFront();
            ((JFrame) this.mainFrame).repaint();
            ((JFrame) this.mainFrame).setAlwaysOnTop(false);
        });
    }

    @Override
    protected void notificationAllPanelMouseClicked(final MouseEvent e) {
        //TODO is this needed?
    }

    @Override
    protected void notificationReplySendActionPerformed(final ActionEvent e) {

        SwingUtilities.invokeLater(() -> {

            ((JFrame) this.mainFrame).setAlwaysOnTop(true);
            ((JFrame) this.mainFrame).toFront();
            ((JFrame) this.mainFrame).repaint();
            ((JFrame) this.mainFrame).setAlwaysOnTop(false);

            ReplyPanelImpl replyPanel = new ReplyPanelImpl(this.mainFrame, this.baseModel);
            this.mainFrame.getMainTextPanelLayeredPane().add(replyPanel, JLayeredPane.MODAL_LAYER);
        });
    }

    @Override
    protected void closeAllNotificationsActionPerformed(final ActionEvent e) {

        this.mainFrame.getNotificationList().forEach(notification -> {

            if (notification.getTimer() != null && notification.getTimer().isRunning()) {

                notification.getTimer().stop();
            }

            notification.setVisible(false);
            notification.dispose();
        });
    }

    public Timer getTimer() {

        return this.timer;
    }

    public void setNotificationText() {

        addMessageToNotificationPanel();
        final int screenResolutionWidth = determineScreenWidth();

        int newYPosition = this.mainFrame.getNotificationPositionY() + 25;

        modifyNotificationPanel(screenResolutionWidth, newYPosition);

        retainInformationAboutThisNotification(newYPosition);

        addNotificationTimer();
    }

    private void addMessageToNotificationPanel() {

        MessageModel messageModel = (MessageModel) this.baseModel;

        this.form_nameLabel.setText(messageModel.getSender() + " sent a message");

        form_notificationMainMessage.setText("");
        new EmojiHandler(mainFrame).replaceEmojiDescriptionWithActualImageIcon(this.form_notificationMainMessage, messageModel.getMessage());

        final int messageLength = messageModel.getMessage().length();

        if (messageLength < 30) {

            adjustNotificationPanelHeight(1);

        } else if (messageLength > 90) {

            adjustNotificationPanelHeight(2);

        } else if (messageLength > 60) {

            adjustNotificationPanelHeight(3);
        }
    }

    private void addPictureToNotificationPanel() {

        PictureModel messageModel = (PictureModel) this.baseModel;

        this.form_nameLabel.setText(messageModel.getSender() + " sent a message");

        form_notificationMainMessage.setText("[picture]\n");
        new EmojiHandler(mainFrame).replaceEmojiDescriptionWithActualImageIcon(this.form_notificationMainMessage, messageModel.getMessage());

        final int messageLength = messageModel.getMessage().length();

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

        this.mainFrame.setNotificationPositionY(newYPosition + getHeight() - MARGIN_TOP);
        this.mainFrame.getNotificationList().add(this);
    }

    private void addNotificationTimer() {

        this.timer = new Timer(5000, e -> {
            processNotificationQueue();
        });
        this.timer.setRepeats(false);
        this.timer.start();
    }

    private void adjustNotificationPanelHeight(final int factor) {

        //TODO check if this is even needed anymore
        //please don't ask..

        //some very ugly manual adjustments..
        final int notificationTextHeight = 25 * factor;

        //components related stuff
        final Dimension notificationPanelSize = new Dimension(this.form_notificationMainPanel.getWidth(), notificationTextHeight - 50);
        this.form_notificationMainPanel.setPreferredSize(notificationPanelSize);
        this.form_notificationMainPanel.setSize(notificationPanelSize);
        this.form_notificationMainPanel.setMinimumSize(notificationPanelSize);
        this.form_notificationMainPanel.setMaximumSize(notificationPanelSize);

        final Dimension scrollPaneSize = new Dimension(this.form_scrollPane1.getWidth(), notificationTextHeight + 5);
        this.form_scrollPane1.setPreferredSize(scrollPaneSize);
        this.form_scrollPane1.setSize(scrollPaneSize);
        this.form_scrollPane1.setMinimumSize(scrollPaneSize);
        this.form_scrollPane1.setMaximumSize(scrollPaneSize);

        final Dimension notificationTextSize = new Dimension(this.form_notificationMainMessage.getWidth(), notificationTextHeight + 5);
        this.form_notificationMainMessage.setPreferredSize(notificationTextSize);
        this.form_notificationMainMessage.setSize(notificationTextSize);
        this.form_notificationMainMessage.setMinimumSize(notificationTextSize);
        this.form_notificationMainMessage.setMaximumSize(notificationTextSize);

        //panel related stuff
        final int adjustmentInHeight = 80;

        final Dimension thisSize = new Dimension(this.getWidth(), adjustmentInHeight + notificationTextHeight);
        this.setPreferredSize(thisSize);
        this.setSize(thisSize);
        this.setMinimumSize(thisSize);
        this.setMaximumSize(thisSize);

        final Dimension panelSize = new Dimension(this.form_notificationAllPanel.getWidth(), adjustmentInHeight + notificationTextHeight);
        this.form_notificationAllPanel.setPreferredSize(panelSize);
        this.form_notificationAllPanel.setSize(panelSize);
        this.form_notificationAllPanel.setMinimumSize(panelSize);
        this.form_notificationAllPanel.setMaximumSize(panelSize);

        final Dimension mainPanelSize = new Dimension(this.form_notificationMainPanel.getWidth(), adjustmentInHeight + notificationTextHeight);
        this.form_notificationMainPanel.setPreferredSize(mainPanelSize);
        this.form_notificationMainPanel.setSize(mainPanelSize);
        this.form_notificationMainPanel.setMinimumSize(mainPanelSize);
        this.form_notificationMainPanel.setMaximumSize(mainPanelSize);

        this.pack();
        this.revalidate();
        this.repaint();
    }

    private final CacheManager cacheManager = CacheManagerFactory.getCacheManager();

    private void processNotificationQueue() {

        setVisible(false);
        dispose();

        ActiveNotificationQueue activeNotificationsCache = (ActiveNotificationQueue) cacheManager.getCache("ActiveNotificationQueue");
        WaitingNotificationQueue waitingNotificationsCache = (WaitingNotificationQueue) cacheManager.getCache("WaitingNotificationQueue");

        final boolean remove = activeNotificationsCache.remove(this.baseModel);

        if (remove && activeNotificationsCache.isEmpty() && !waitingNotificationsCache.isEmpty()) {
            this.mainFrame.setNotificationPositionY(0);
            final String queuedNotification = waitingNotificationsCache.pollFirst();
            this.mainFrame.getGuiFunctionality().internalNotificationHandling(queuedNotification);
        }
    }

    public void setNotificationPicture() {

        addPictureToNotificationPanel();
        final int screenResolutionWidth = determineScreenWidth();

        int newYPosition = this.mainFrame.getNotificationPositionY() + 25;

        modifyNotificationPanel(screenResolutionWidth, newYPosition);

        retainInformationAboutThisNotification(newYPosition);

        addNotificationTimer();
    }
}