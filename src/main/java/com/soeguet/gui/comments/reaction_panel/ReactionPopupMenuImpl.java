package com.soeguet.gui.comments.reaction_panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.net.URL;

public class ReactionPopupMenuImpl extends JPopupMenu {

    private ReactionPopupHandler reactionPopupHandler;


    public ReactionPopupMenuImpl() {

    }

    public void setPopupMenuUp() {

        JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addItemToMenu(jPanel, "/emojis/$+1f4ac$+.png");
        addItemToMenu(jPanel, "/emojis/$+1f4b3$+.png");
        addItemToMenu(jPanel, "/emojis/$+1f4af$+.png");
        addItemToMenu(jPanel, "/emojis/$+1f4b8$+.png");
        add(jPanel);
    }

    private void addItemToMenu(final JPanel jPanel, final String item) {

        URL url = getClass().getResource(item);
        if (url != null) {
            ImageIcon imageIcon = new ImageIcon(url);
            JPanel panel = new JPanel();
            panel.add(new JLabel(imageIcon));
            jPanel.add(panel);
        }
    }

    public void startAnimation() {

        if (!isVisible() ) {

            reactionPopupHandler.initializePopupTimer();
        }
    }

    public void initializePopupHandler(final JLayeredPane layeredPane) {

        reactionPopupHandler = new ReactionPopupHandler(this, layeredPane);
    }

    public void stopAnimation() {

        reactionPopupHandler.stopPopupTimer();

    }

    public void dispose(final MouseEvent e) {

        if (isVisible()) {

            setVisible(false);
            dispose(e);
        }
    }

    public boolean mouseLeftContainerCompletely(final MouseEvent e) {

        return e.getX() <= 0 || e.getX() >= e.getComponent().getWidth() || e.getY() <= 0 || e.getY() >= e.getComponent().getHeight();
    }
}