package com.soeguet.gui.comments.generic_comment.gui_elements;

import javax.swing.*;
import java.awt.*;

public class TransparentPanel extends JPanel {

    public TransparentPanel() {

        this.setBackground(new Color(0, 0, 0, 0));
        this.setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {

    }
}