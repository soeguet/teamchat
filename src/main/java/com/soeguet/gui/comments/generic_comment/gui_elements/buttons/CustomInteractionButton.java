package com.soeguet.gui.comments.generic_comment.gui_elements.buttons;

import com.soeguet.gui.comments.generic_comment.factories.InteractionPopupMenuFactory;
import com.soeguet.gui.comments.generic_comment.gui_elements.CustomInteractionPopupMenu;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.model.jackson.BaseModel;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CustomInteractionButton extends JButton implements MouseListener {

    // variables -- start
    private final MainFrameGuiInterface mainFrame;
    private final BaseModel baseModel;
    // variables -- end

    // constructors -- start
    public CustomInteractionButton(final MainFrameGuiInterface mainFrame, final BaseModel baseModel) {

        this.mainFrame = mainFrame;
        this.baseModel = baseModel;

        this.addMouseListener(this);
        this.setFocusable(false);
    }
    // constructors -- end

    // overrides -- start
    @Override
    public void mouseClicked(final MouseEvent e) {

    }

    @Override
    public void mousePressed(final MouseEvent e) {

        CustomInteractionPopupMenu customInteractionPopupMenu =
                new InteractionPopupMenuFactory(mainFrame, baseModel).create();
        customInteractionPopupMenu.show(this, e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(final MouseEvent e) {

    }

    @Override
    public void mouseEntered(final MouseEvent e) {

    }

    @Override
    public void mouseExited(final MouseEvent e) {

    }
    // overrides -- end
}