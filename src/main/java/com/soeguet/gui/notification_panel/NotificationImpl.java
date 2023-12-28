package com.soeguet.gui.notification_panel;

import com.soeguet.cache.factory.CacheManagerFactory;
import com.soeguet.cache.implementations.ActiveNotificationQueue;
import com.soeguet.cache.implementations.WaitingNotificationQueue;
import com.soeguet.cache.manager.CacheManager;
import com.soeguet.emoji.EmojiHandler;
import com.soeguet.emoji.interfaces.EmojiHandlerInterface;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.gui.notification_panel.generated.Notification;
import com.soeguet.gui.notification_panel.interfaces.NotificationInterface;
import com.soeguet.gui.reply.ReplyPanelImpl;
import com.soeguet.gui.reply.interfaces.ReplyInterface;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.MessageModel;
import com.soeguet.model.jackson.PictureModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class NotificationImpl extends Notification implements NotificationInterface {

    private final MainFrameGuiInterface mainFrame;
    private final int MARGIN_TOP = 10;
    private final BaseModel baseModel;
    private final CacheManager cacheManager = CacheManagerFactory.getCacheManager();
    private Timer timer;

    public NotificationImpl(final MainFrameGuiInterface mainFrame, final BaseModel baseModel) {

        super(null);
        this.baseModel = baseModel;
        this.mainFrame = mainFrame;
    }

    @Override
    protected void notificationAllPanelMouseClicked(final MouseEvent e) {

        SwingUtilities.invokeLater(this::bringChatGuiToFront);
    }

    private void bringChatGuiToFront() {

        if (mainFrame instanceof JFrame gui) {

            gui.setAlwaysOnTop(true);
            gui.toFront();
            gui.repaint();
            gui.setAlwaysOnTop(false);
            Toolkit.getDefaultToolkit().beep();
        }
    }

    @Override
    protected void notificationReplySendActionPerformed(final ActionEvent e) {

        SwingUtilities.invokeLater(
                () -> {
                    ReplyInterface replyPanel = new ReplyPanelImpl(this.mainFrame, this.baseModel);
                    replyPanel.populatePanel();
                    replyPanel.setPosition();
                    replyPanel.requestAllFocus();
                    replyPanel.addPanelToMainFrame();
                });

        bringChatGuiToFront();
    }

    @Override
    protected void closeAllNotificationsActionPerformed(final ActionEvent e) {

        this.mainFrame
                .getNotificationList()
                .forEach(
                        notification -> {
                            if (notification.getTimer() != null
                                    && notification.getTimer().isRunning()) {

                                notification.getTimer().stop();
                            }

                            notification.setVisible(false);
                            notification.dispose();
                        });
    }

    public Timer getTimer() {

        return this.timer;
    }

    @Override
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

        replaceEmojiTextToIconOnTextPane(messageModel.getMessage());
    }

    private int determineScreenWidth() {

        final Dimension primaryScreenSize = Toolkit.getDefaultToolkit().getScreenSize();

        return (int) primaryScreenSize.getWidth();
    }

    private void modifyNotificationPanel(final int screenResolutionWidth, final int newYPosition) {

        int MARGIN_RIGHT = 10;

        SwingUtilities.invokeLater(
                () -> {
                    setBounds(
                            screenResolutionWidth - getWidth() - MARGIN_RIGHT,
                            newYPosition + MARGIN_TOP,
                            getWidth(),
                            getHeight());
                    setAlwaysOnTop(true);
                    getScrollPane1().getVerticalScrollBar().setValue(0);
                    setVisible(true);
                });
    }

    private void retainInformationAboutThisNotification(final int newYPosition) {

        this.mainFrame.setNotificationPositionY(newYPosition + getHeight() - MARGIN_TOP);
        this.mainFrame.getNotificationList().add(this);
    }

    private void addNotificationTimer() {

        this.timer = new Timer(5000, e -> processNotificationQueue());
        this.timer.setRepeats(false);
        this.timer.start();
    }

    private void replaceEmojiTextToIconOnTextPane(final String message) {

        EmojiHandlerInterface emojiHandler = new EmojiHandler(mainFrame);
        emojiHandler.replaceEmojiDescriptionWithActualImageIcon(
                this.form_notificationMainMessage, message);
    }

    private void processNotificationQueue() {

        setVisible(false);
        dispose();

        ActiveNotificationQueue activeNotificationsCache =
                (ActiveNotificationQueue) cacheManager.getCache("ActiveNotificationQueue");
        WaitingNotificationQueue waitingNotificationsCache =
                (WaitingNotificationQueue) cacheManager.getCache("WaitingNotificationQueue");

        final boolean remove = activeNotificationsCache.remove(this.baseModel);

        if (remove && activeNotificationsCache.isEmpty() && !waitingNotificationsCache.isEmpty()) {
            this.mainFrame.setNotificationPositionY(0);
            final String queuedNotification = waitingNotificationsCache.pollFirst();
            this.mainFrame.getGuiFunctionality().internalNotificationHandling(queuedNotification);
        }

        if (activeNotificationsCache.isEmpty()) {

            this.mainFrame.setNotificationPositionY(0);
        }

        if (waitingNotificationsCache.isEmpty()) {

            this.mainFrame.setNotificationPositionY(0);
        }
    }

    @Override
    public void setNotificationPicture() {

        addPictureToNotificationPanel();
        final int screenResolutionWidth = determineScreenWidth();

        int newYPosition = this.mainFrame.getNotificationPositionY() + 25;

        modifyNotificationPanel(screenResolutionWidth, newYPosition);

        retainInformationAboutThisNotification(newYPosition);

        addNotificationTimer();
    }

    private void addPictureToNotificationPanel() {

        PictureModel messageModel = (PictureModel) this.baseModel;

        this.form_nameLabel.setText(messageModel.getSender() + " sent a message");

        form_notificationMainMessage.setText("[picture]\n");

        replaceEmojiTextToIconOnTextPane(messageModel.getDescription());
    }
}
