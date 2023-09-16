package com.soeguet.gui.properties;

import com.soeguet.gui.main_frame.ChatMainGuiElementsImpl;
import com.soeguet.gui.main_frame.MainGuiElementsInterface;
import com.soeguet.gui.properties.generated.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;

public class PropertiesPanelImpl extends PropertiesPanel {

    private final JFrame mainFrame;
    private final Point offset = new Point();


    public PropertiesPanelImpl(JFrame mainFrame) {

        this.mainFrame = mainFrame;

        setPosition();
        this.setVisible(true);
    }

    private void setPosition() {

        if (!(mainFrame instanceof MainGuiElementsInterface)) {
            return;
        }

        MainGuiElementsInterface gui = (MainGuiElementsInterface) mainFrame;

        int textPaneWidth = gui.getMainTextPanelLayeredPane().getWidth();
        int textPaneHeight = gui.getMainTextPanelLayeredPane().getHeight();

        this.setBounds(20, 20, textPaneWidth-40, textPaneHeight-40);

        gui.getMainTextPanelLayeredPane().add(this, JLayeredPane.MODAL_LAYER);
    }

    @Override
    protected void thisMousePressed(MouseEvent e) {

    }

    @Override
    protected void thisMouseDragged(MouseEvent e) {

        int x = e.getX() - offset.x + this.getX();
        int y = e.getY() - offset.y + this.getY();
        this.setLocation(x, y);
    }

    @Override
    protected void thisMouseEntered(MouseEvent e) {

    }

    @Override
    protected void thisMouseExited(MouseEvent e) {

    }

    @Override
    protected void thisFocusLost(FocusEvent e) {

    }

    @Override
    protected void closeReplyPanelButtonMouseReleased(MouseEvent e) {

        this.removeAll();
        this.setVisible(false);
    }

    @Override
    protected void replyTextPaneFocusLost(FocusEvent e) {

    }

    @Override
    protected void quotePanelSenButtonMouseReleased(MouseEvent e) {

    }
}
