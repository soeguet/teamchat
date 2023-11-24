package com.soeguet.gui.comments.generic_comment.gui_elements.panels;

import javax.swing.*;
import java.awt.*;

public class CustomSimpleJPanel extends JPanel {

    // constructors -- start
    public CustomSimpleJPanel() {

        this(null);
    }

    public CustomSimpleJPanel(final LayoutManager layout) {

        super(layout);
        super.setOpaque(false);
        super.setBackground(null);
    }
    // constructors -- end
}