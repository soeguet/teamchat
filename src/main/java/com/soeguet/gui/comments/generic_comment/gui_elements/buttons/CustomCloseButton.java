package com.soeguet.gui.comments.generic_comment.gui_elements.buttons;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CustomCloseButton extends JButton implements MouseListener {

    private final JComponent component;

    public CustomCloseButton(JComponent component, String text) {

        this.component = component;
        this.addMouseListener(this);
        super.setText(text);
    }

    @Override
    public void mouseClicked(final MouseEvent e) {

        component.removeAll();
        component.setVisible(false);
    }

    @Override
    public void mousePressed(final MouseEvent e) {

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
}