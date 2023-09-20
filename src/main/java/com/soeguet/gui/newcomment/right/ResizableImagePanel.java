package com.soeguet.gui.newcomment.right;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ResizableImagePanel extends JPanel {

    private final BufferedImage image;
    public ResizableImagePanel(BufferedImage image) {

        this.image = image;

        setSize(image.getWidth(), image.getHeight());
    }

    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(image, 0, 0, null);
    }
}