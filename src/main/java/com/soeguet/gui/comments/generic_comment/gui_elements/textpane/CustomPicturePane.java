package com.soeguet.gui.comments.generic_comment.gui_elements.textpane;

import com.soeguet.gui.comments.generic_comment.gui_elements.interfaces.ContentInterface;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static javax.imageio.ImageIO.*;

public class CustomPicturePane extends JLabel implements ContentInterface, ComponentListener {

    private final byte[] picture;
// constructors -- start
    public CustomPicturePane(byte[] picture) {

        this.picture = picture;
        setOpaque(true);
        setPictureToLabel();
    }
// constructors -- end

// overrides -- start
    @Override
    public void componentResized(final ComponentEvent e) {

        setPictureToLabel();
    }

    private void setPictureToLabel() {

        BufferedImage img = null;
        try {
            img = read(new ByteArrayInputStream(this.picture));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        Image scaledImg = img.getScaledInstance(this.getWidth() != 0 ? this.getWidth() : 100, this.getHeight() != 0 ? this.getHeight() : 100,
                                                Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(scaledImg);
        this.setIcon(icon);
    }

    @Override
    public void componentMoved(final ComponentEvent e) {

    }

    @Override
    public void componentShown(final ComponentEvent e) {

    }

    @Override
    public void componentHidden(final ComponentEvent e) {

    }

    public void setIcon(Icon icon) {

        super.setIcon(icon);
        if (icon != null) {
            setSize(icon.getIconWidth(), icon.getIconHeight());
        }
    }

}