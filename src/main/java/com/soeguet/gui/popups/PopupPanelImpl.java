package com.soeguet.gui.popups;

import com.soeguet.gui.main_frame.MainFrameInterface;
import com.soeguet.gui.popups.generated.PopupPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class PopupPanelImpl extends PopupPanel {

    private static final int TIMER_PERIOD = 1;
    private static final int POPUP_MOVE_SPEED = 3;
    private final MainFrameInterface mainFrame;

    public PopupPanelImpl(MainFrameInterface mainFrame, String message) {

        this.mainFrame = mainFrame;
        this.getMessageTextField().setText(message);
    }

    /**
     Implements a popup with the given delay in milliseconds.

     @param delayMilliseconds The delay in milliseconds before the popup is displayed.
     */
    public void implementPopup(int delayMilliseconds) {

        configurePopupPanelPlacement(mainFrame);

        initiatePopupTimer(mainFrame, delayMilliseconds);
    }

    /**
     * Initializes a timer for displaying a popup after a certain delay.
     *
     * @param gui               The MainFrameInterface object representing the main GUI.
     * @param delayMilliseconds The delay in milliseconds before the popup is displayed.
     */
    private void initiatePopupTimer(MainFrameInterface gui, int delayMilliseconds) {

        Timer swingTimer = new Timer(TIMER_PERIOD, event -> performTimedActions(gui, event));
        swingTimer.setInitialDelay(delayMilliseconds);
        swingTimer.start();
    }

    /**
     * Performs timed actions for displaying popups on the main GUI.
     *
     * @param gui   The MainFrameInterface object representing the main GUI.
     * @param event The ActionEvent object triggering the timed actions.
     */
    private void performTimedActions(MainFrameInterface gui, ActionEvent event) {

        relocatedPopup();

        if (isPopupOffScreen()) {

            ((Timer) event.getSource()).stop();

            removeCurrentPopup(gui);

            if (!gui.getMessageQueue().isEmpty()) {

                try {

                    recursivelyProcessStringsInQueue(gui);

                } catch (InterruptedException e) {

                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     Checks if the popup is off the screen.

     @return true if the popup is off the screen, false otherwise.
     */
    private boolean isPopupOffScreen() {

        return getY() + getHeight() < 0;
    }

    /**
     Removes the current popup from the GUI.

     @param gui the main GUI elements interface.
     */
    private void removeCurrentPopup(MainFrameInterface gui) {

        gui.getMainTextPanelLayeredPane().remove(PopupPanelImpl.this);
        setVisible(false);
        gui.setMessagePanel(null);
    }

    /**
     Recursively processes strings in the queue by displaying them in pop-up panels.

     @param gui the main GUI elements interface.

     @throws InterruptedException if the thread sleep is interrupted.
     */
    private void recursivelyProcessStringsInQueue(MainFrameInterface gui) throws InterruptedException {

        Thread.sleep(500);

        String message = gui.getMessageQueue().removeFirst();
        PopupPanelImpl popupPanel = new PopupPanelImpl(mainFrame, message);
        popupPanel.implementPopup(1500);
    }

    /**
     Relocates the pop-up panel to a new position by decreasing its Y-coordinate by 1.
     */
    private void relocatedPopup() {

        this.setLocation(getX(), getY() - POPUP_MOVE_SPEED);
    }

    /**
     Configures the placement of the popup panel within the main GUI.

     @param gui The interface that provides access to the main GUI elements.
     */
    private void configurePopupPanelPlacement(MainFrameInterface gui) {

        this.setBounds((gui.getMainTextPanelLayeredPane().getWidth() - 250) / 2, 100, 250, 100);
        gui.getMainTextPanelLayeredPane().add(this, JLayeredPane.POPUP_LAYER);
    }
}