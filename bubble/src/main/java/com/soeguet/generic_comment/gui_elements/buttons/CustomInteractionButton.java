package com.soeguet.generic_comment.gui_elements.buttons;

import com.soeguet.generic_comment.factories.InteractionPopupMenuFactory;
import com.soeguet.generic_comment.gui_elements.CustomInteractionPopupMenu;
import com.soeguet.model.jackson.BaseModel;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CustomInteractionButton extends JButton implements MouseListener {

    // variables -- start
    private final BaseModel baseModel;

    // variables -- end

    // constructors -- start
    public CustomInteractionButton( final BaseModel baseModel) {
        this.baseModel = baseModel;

        this.addMouseListener(this);
        this.setFocusable(false);
    }

    // constructors -- end

    @Override
    public void mouseClicked(final MouseEvent e) {}

    @Override
    public void mousePressed(final MouseEvent e) {

        CustomInteractionPopupMenu customInteractionPopupMenu =
                new InteractionPopupMenuFactory(baseModel).create();
        customInteractionPopupMenu.show(this, e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(final MouseEvent e) {}

    @Override
    public void mouseEntered(final MouseEvent e) {}

    @Override
    public void mouseExited(final MouseEvent e) {}
}