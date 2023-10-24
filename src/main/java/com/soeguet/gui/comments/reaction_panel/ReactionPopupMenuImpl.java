package com.soeguet.gui.comments.reaction_panel;

import javax.swing.*;
import java.net.URL;

public class ReactionPopupMenuImpl extends JPopupMenu {

    public ReactionPopupMenuImpl() {
    }

    public void addItemToMenu(final JPanel jPanel, final String item) {

        URL url = getClass().getResource(item);
        if (url != null) {
            ImageIcon imageIcon = new ImageIcon(url);
            JPanel panel = new JPanel();
            panel.add(new JLabel(imageIcon));
            jPanel.add(panel);
        }
    }

    public void addImageIconToMenu(final ImageIcon imageIcon) {

        add(new JMenuItem(imageIcon));
    }

    public void show(final JComponent component, final int x, final int y) {

        super.show(component, x, y);
    }
}