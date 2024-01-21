package com.soeguet.comments.reaction_sticker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.BLACK);
        g2d.drawOval(0, 0, 30, 30);
        g2d.setColor(Color.WHITE);
        g2d.fillOval(1, 1, 29, 29);
    }

    public String getUsername() {
        return username;
    }
}