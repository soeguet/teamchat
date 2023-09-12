package com.soeguet.gui.popups;

import com.soeguet.gui.main_frame.MainGuiElementsInterface;
import com.soeguet.gui.popups.generated.PopupPanel;

import javax.swing.*;
import java.util.Timer;

public class PopupPanelImpl extends PopupPanel {

    private final JFrame mainFrame;

    public PopupPanelImpl(JFrame mainFrame, String message) {

        this.mainFrame = mainFrame;
        this.getMessageTextField().setText(message);
    }

    public void implementPopup() {
        if (!(mainFrame instanceof MainGuiElementsInterface)) {
            return;
        }

        MainGuiElementsInterface gui = (MainGuiElementsInterface) mainFrame;

        this.setBounds((gui.getMainTextPanelLayeredPane().getWidth() - 250) / 2, 100, 250, 100);
        gui.getMainTextPanelLayeredPane().add(this, JLayeredPane.MODAL_LAYER);

        //start moving the messagePanel slowly up after 3 seconds
        java.util.Timer timer = new Timer();
        timer.schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                setLocation(getX(), getY() - 1);

                if (getY() + getHeight() < 0) {
                    timer.cancel();
                    gui.getMainTextPanelLayeredPane().remove(PopupPanelImpl.this);
                    setVisible(false);
                }
            }
        }, 3000, 1);

    }
}
