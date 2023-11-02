package com.soeguet.gui.comments.generic_comment.gui_elements.menu_item;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CustomPictureMaximizeMenuItem extends JMenuItem implements MouseListener {

    // variables -- start
    private final BufferedImage bufferedImage;
    // variables -- end

    // constructors -- start
    public CustomPictureMaximizeMenuItem(String title, BufferedImage bufferedImage) {

        super(title);
        this.bufferedImage = bufferedImage;
        addMouseListener(this);
    }
    // constructors -- end

    /**
     Opens the given image in an external image viewer.

     @param image
     The image to be opened.
     */
    private void openImageInExternalImageViewer(BufferedImage image) {

        try {

            File tempFile = File.createTempFile("tempImage", ".png");
            tempFile.deleteOnExit();
            ImageIO.write(image, "png", tempFile);
            Desktop.getDesktop().open(tempFile);

        } catch (IOException ex) {

            JOptionPane.showMessageDialog(null, "Error while opening image in external viewer: " + ex.getMessage());
        }
    }

    // overrides -- start
    @Override
    public void mouseClicked(final MouseEvent e) {

    }

    @Override
    public void mousePressed(final MouseEvent e) {

        new Thread(() -> openImageInExternalImageViewer(bufferedImage)).start();
    }

    @Override
    public void mouseReleased(final MouseEvent e) {

    }

    @Override
    public void mouseEntered(final MouseEvent e) {

        System.out.println("mouse entered");
    }

    @Override
    public void mouseExited(final MouseEvent e) {

    }
    // overrides -- end
}