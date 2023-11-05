package com.soeguet.gui.comments.generic_comment.gui_elements.menu_items;

import com.soeguet.model.jackson.PictureModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class CustomPictureMaximizeMenuItem extends JMenuItem implements MouseListener {

    // variables -- start
    private final PictureModel pictureModel;
    // variables -- end

    // constructors -- start
    public CustomPictureMaximizeMenuItem(String title, PictureModel pictureModel) {

        super(title);

        this.pictureModel = pictureModel;

        addMouseListener(this);
    }
    // constructors -- end

    /**
     Opens the given image in an external image viewer.

     @param image
     The image to be opened.
     */
    private void openImageInExternalImageViewer() {

        try {

            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(pictureModel.getPicture()));

            File tempFile = File.createTempFile("tempImage", ".png");
            tempFile.deleteOnExit();

            ImageIO.write(bufferedImage, "png", tempFile);

            Desktop.getDesktop().open(tempFile);

        } catch (IOException ex) {

            JOptionPane.showMessageDialog(null, "Error while opening image in external viewer: " + ex.getMessage());
        }
    }

    @Override
    public void mouseClicked(final MouseEvent e) {

    }

    @Override
    public void mousePressed(final MouseEvent e) {

        Thread.ofVirtual().start(this::openImageInExternalImageViewer);
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
}