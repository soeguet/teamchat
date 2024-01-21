package com.soeguet.generic_comment.reaction_panel;

import java.awt.*;
import javax.swing.*;

/** This class handles the reaction popup menu functionality. */
public class ReactionPopupHandler {

    private final ReactionPopupMenuImpl reactionPopupMenu;
    private final JPanel topPanel;
    private Timer popupTimer;

    /**
     * Creates a new instance of ReactionPopupHandler.
     *
     * @param reactionPopupMenu the ReactionPopupMenuImpl object to handle
     * @param topPanel the JLayeredPane to which the reaction popup will be added
     */
    public ReactionPopupHandler(
            final ReactionPopupMenuImpl reactionPopupMenu, final JPanel topPanel) {

        this.topPanel = topPanel;
        this.reactionPopupMenu = reactionPopupMenu;
    }

    /**
     * Initializes a popup timer for showing a reaction popup menu. The popup timer is only
     * initialized if it is not already running. The popup timer will show the reaction popup menu
     * after a given delay.
     *
     * <p>The popup timer will be set with a delay of 2000 milliseconds (2 seconds). After the
     * delay, the reaction popup menu will be shown at the current mouse position within the layered
     * pane.
     *
     * <p>If the popup timer is already running, this method will do nothing and return.
     */
    public void initializePopupTimer() {

        // this should not be possible
        if (popupTimer != null && popupTimer.isRunning()) {
            return;
        }

        popupTimer =
                new Timer(
                        2000,
                        timer -> {
                            Point location = topPanel.getMousePosition();

                            if (location == null) {
                                location = new Point(0, 0);
                            }

                            reactionPopupMenu.show(
                                    topPanel, (int) location.getX(), (int) location.getY());
                        });

        popupTimer.setRepeats(false);
        popupTimer.start();
    }

    /**
     * Stops the popup timer for showing a reaction popup menu.
     *
     * <p>If the popup timer is currently running, it will be stopped. After stopping the popup
     * timer, the instance variable 'popupTimer' will be set to null.
     *
     * <p>If the popup timer is not running or 'popupTimer' is null, this method will do nothing.
     */
    public void stopPopupTimer() {

        if (popupTimer != null && popupTimer.isRunning()) {

            popupTimer.stop();
        }

        popupTimer = null;
    }
}