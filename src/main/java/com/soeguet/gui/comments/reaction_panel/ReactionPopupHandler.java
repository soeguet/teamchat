package com.soeguet.gui.comments.reaction_panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class ReactionPopupHandler {

    private final ReactionPopupMenuImpl reactionPopupMenu;
    private final JLayeredPane layeredPane;
    private Timer popupTimer;

    public ReactionPopupHandler(final ReactionPopupMenuImpl reactionPopupMenu, final JLayeredPane layeredPane) {

        this.layeredPane = layeredPane;
        this.reactionPopupMenu = reactionPopupMenu;
    }

    public void initializePopupTimer() {

        //this should not be possible
        if (popupTimer != null && popupTimer.isRunning()) {
            return;
        }

        popupTimer = new Timer(2000, timer -> {

            final Point location = MouseInfo.getPointerInfo().getLocation();
            reactionPopupMenu.show(layeredPane, (int) location.getX(), (int) location.getY());
        });

        popupTimer.setRepeats(false);
        popupTimer.start();
    }

    public void stopPopupTimer() {

        if (popupTimer != null && popupTimer.isRunning()) {

            popupTimer.stop();
        }

        popupTimer = null;
    }
}