package com.soeguet.gui.comments.generic_comment.gui_elements.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TransparentTopPanel extends JPanel implements MouseListener {

    // constructors -- start
    public TransparentTopPanel() {

        this.setBackground(new Color(0, 0, 0, 0));
        this.setOpaque(false);
    }
    // constructors -- end

    @Override
    protected void paintComponent(Graphics g) {

    }

    @Override
    public void mouseClicked(final MouseEvent e) {

        System.out.println("TransparentTopPanel clicked");
    }

    @Override
    public void mousePressed(final MouseEvent e) {

        System.out.println("TransparentTopPanel pressed");
    }

    @Override
    public void mouseReleased(final MouseEvent e) {

    }

    @Override
    public void mouseEntered(final MouseEvent e) {

        System.out.println("TransparentTopPanel entered");
    }

    @Override
    public void mouseExited(final MouseEvent e) {

    }
}