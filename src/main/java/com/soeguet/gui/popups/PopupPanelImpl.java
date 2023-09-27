package com.soeguet.gui.popups;

import com.soeguet.gui.main_frame.MainFrameInterface;
import com.soeguet.gui.popups.generated.PopupPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class PopupPanelImpl extends PopupPanel {

    private final MainFrameInterface mainFrame;

    public PopupPanelImpl(MainFrameInterface mainFrame, String message) {

        this.mainFrame = mainFrame;
        //TODO linebreaks are not working
        this.getMessageTextField().setText(message);
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
     * Initializes a timer for displaying a popup after a certain delay.
     *
     * @param gui               The MainFrameInterface object representing the main GUI.
     * @param delayMilliseconds The delay in milliseconds before the popup is displayed.
     */
    private void initiatePopupTimer(int delayMilliseconds) {

        Timer swingTimer = new Timer(delayMilliseconds, event -> {

            this.mainFrame.getMainTextPanelLayeredPane().remove(PopupPanelImpl.this);
            setVisible(false);
            this.mainFrame.setMessagePanel(null);

            if (!this.mainFrame.getMessageQueue().isEmpty()) {

                try {

                    recursivelyProcessStringsInQueue();

                } catch (InterruptedException e) {

                    throw new RuntimeException(e);
                }
            }
        });
//        swingTimer.setInitialDelay(delayMilliseconds);
        swingTimer.setRepeats(false);
        swingTimer.start();
    }

    private void recursivelyProcessStringsInQueue() throws InterruptedException {

        String message = this.mainFrame.getMessageQueue().removeFirst();
        PopupPanelImpl popupPanel = new PopupPanelImpl(mainFrame, message);
        popupPanel.implementPopup(1500);
    }

    /**
     Configures the placement of the popup panel within the main GUI.

     @param gui The interface that provides access to the main GUI elements.
     */
    private void configurePopupPanelPlacement() {

        this.setBounds((this.mainFrame.getMainTextPanelLayeredPane().getWidth() - 250) / 2, 100, 250, 100);
        this.mainFrame.getMainTextPanelLayeredPane().add(this, JLayeredPane.POPUP_LAYER);
    }
}