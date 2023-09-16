package com.soeguet.gui.popups;

import com.soeguet.gui.main_frame.MainGuiElementsInterface;
import com.soeguet.gui.popups.generated.PopupPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class PopupPanelImpl extends PopupPanel {

    private static final int TIMER_DELAY = 3000;
    private static final int TIMER_PERIOD = 1;
    private static final int POPUP_MOVE_SPEED = 3;
    private final JFrame mainFrame;

    public PopupPanelImpl(JFrame mainFrame, String message) {

        this.mainFrame = mainFrame;
        this.getMessageTextField().setText(message);
    }

    /**
     * Validates the main frame object and returns an instance of MainGuiElementsInterface.
     *
     * @return An instance of MainGuiElementsInterface if the main frame is an instance of MainGuiElementsInterface, null otherwise.
     */
    private MainGuiElementsInterface validateMainFrame() {
        if (!(mainFrame instanceof MainGuiElementsInterface)) {
            return null;
        }
        return (MainGuiElementsInterface) mainFrame;
    }

    /**
     * Implements a popup by configuring its panel placement and initiating the popup timer.
     */
    public void implementPopup() {

        MainGuiElementsInterface gui = validateMainFrame();

        assert gui != null;

        configurePopupPanelPlacement(gui);

        initiatePopupTimer(gui);
    }

    /**
     * Initiates a timer for the popup.
     *
     * @param gui The MainGuiElementsInterface object representing the main GUI elements.
     */
    private void initiatePopupTimer(MainGuiElementsInterface gui) {

        Timer swingTimer = new Timer(TIMER_PERIOD, event -> performTimedActions(gui, event));
        swingTimer.setInitialDelay(TIMER_DELAY);
        swingTimer.start();
    }

    /**
     * Performs timed actions for the popup.
     *
     * @param gui   The MainGuiElementsInterface object representing the main GUI elements.
     * @param event The ActionEvent object triggered by the timer.
     */
    private void performTimedActions(MainGuiElementsInterface gui, ActionEvent event) {

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
     * Checks if the popup is off the screen.
     *
     * @return true if the popup is off the screen, false otherwise.
     */
    private boolean isPopupOffScreen() {
        return getY() + getHeight() < 0;
    }

    /**
     * Removes the current popup from the GUI.
     *
     * @param gui the main GUI elements interface.
     */
    private void removeCurrentPopup(MainGuiElementsInterface gui) {

        gui.getMainTextPanelLayeredPane().remove(PopupPanelImpl.this);
        setVisible(false);
        gui.setMessagePanel(null);
    }

    /**
     * Recursively processes strings in the queue by displaying them in pop-up panels.
     *
     * @param gui the main GUI elements interface.
     * @throws InterruptedException if the thread sleep is interrupted.
     */
    private void recursivelyProcessStringsInQueue(MainGuiElementsInterface gui) throws InterruptedException {

        Thread.sleep(500);

        String message = gui.getMessageQueue().removeFirst();
        PopupPanelImpl popupPanel = new PopupPanelImpl(mainFrame, message);
        popupPanel.implementPopup();
    }

    /**
     * Relocates the pop-up panel to a new position by decreasing its Y-coordinate by 1.
     */
    private void relocatedPopup() {
        this.setLocation(getX(), getY() - POPUP_MOVE_SPEED);
    }

    /**
     * Configures the placement of the popup panel within the main GUI.
     *
     * @param gui The interface that provides access to the main GUI elements.
     */
    private void configurePopupPanelPlacement(MainGuiElementsInterface gui) {
        this.setBounds((gui.getMainTextPanelLayeredPane().getWidth() - 250) / 2, 100, 250, 100);
        gui.getMainTextPanelLayeredPane().add(this, JLayeredPane.MODAL_LAYER);
    }


}
