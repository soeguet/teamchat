package com.soeguet.gui.comments.reaction_sticker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CustomStickerContainer extends JPanel {

    private final String username;
    public CustomStickerContainer(ImageIcon imageIcon) {

        this.username = imageIcon.getDescription();
        setLayout(new BorderLayout());

        JLabel label = new JLabel(imageIcon);
        label.setToolTipText(imageIcon.getDescription());

        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);

        JPanel innerPanel = new JPanel(new GridBagLayout());
        innerPanel.add(label);
        innerPanel.setOpaque(false);

        add(innerPanel, BorderLayout.CENTER);
    }
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(31, 31);
    }

    @Override
    protected void paintComponent(final Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.BLACK);
        g2d.drawOval(0,0, 30, 30);
        g2d.setColor(Color.WHITE);
        g2d.fillOval(1,1, 29, 29);
    }
}