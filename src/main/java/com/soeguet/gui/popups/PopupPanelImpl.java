package com.soeguet.gui.popups;

import com.soeguet.cache.factory.CacheManagerFactory;
import com.soeguet.cache.implementations.MessageQueue;
import com.soeguet.cache.manager.CacheManager;
import com.soeguet.gui.main_frame.MainFrameInterface;
import com.soeguet.gui.popups.generated.PopupPanel;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class PopupPanelImpl extends PopupPanel {

    private final MainFrameInterface mainFrame;
    private final CacheManager cacheManager = CacheManagerFactory.getCacheManager();

    public PopupPanelImpl(MainFrameInterface mainFrame, String message) {

        this.mainFrame = mainFrame;
        //TODO linebreaks are not working
        this.getMessageTextField().setText(message);
    }

    @Override
    protected void messageTextFieldMouseClicked(final MouseEvent e) {

        System.out.println("PopupPanelImpl.messageTextFieldMouseClicked");
        removeThisPopup();
    }

    private void removeThisPopup() {

        this.mainFrame.getMainTextPanelLayeredPane().remove(PopupPanelImpl.this);
        setVisible(false);

        ((JFrame) mainFrame).revalidate();
        ((JFrame) mainFrame).repaint();
    }

    /**
     Implements a popup with the given delay in milliseconds.

     @param delayMilliseconds The delay in milliseconds before the popup is displayed.
     */
    public void implementPopup(int delayMilliseconds) {

        configurePopupPanelPlacement();

        initiatePopupTimer(delayMilliseconds);
    }

    /**
     Initiates a timer to trigger a popup with the given delay in milliseconds.

     @param delayMilliseconds The delay in milliseconds before the popup is displayed.
     */
    private void initiatePopupTimer(int delayMilliseconds) {

        Timer swingTimer = new Timer(delayMilliseconds, event -> {

            removeThisPopup();
            checkForMorePopupsInQueue();
        });

        swingTimer.setRepeats(false);
        swingTimer.start();
    }

    private void checkForMorePopupsInQueue() {

        MessageQueue messageQueue = (MessageQueue) cacheManager.getCache("messageQueue");

        if (!messageQueue.isEmpty()) {

            try {

                recursivelyProcessStringsInQueue();

            } catch (InterruptedException e) {

                throw new RuntimeException(e);
            }
        }
    }

    private void recursivelyProcessStringsInQueue() throws InterruptedException {

        MessageQueue messageQueue = (MessageQueue) cacheManager.getCache("messageQueue");
        String message = messageQueue.pollFirst();
        PopupPanelImpl popupPanel = new PopupPanelImpl(mainFrame, message);
        popupPanel.implementPopup(1500);
    }

    /**
     Configures the placement of the popup panel within the main GUI.
     */
    private void configurePopupPanelPlacement() {

        this.setBounds((this.mainFrame.getMainTextPanelLayeredPane().getWidth() - 250) / 2, 100, 250, 100);
        this.mainFrame.getMainTextPanelLayeredPane().add(this, JLayeredPane.POPUP_LAYER);
    }
}