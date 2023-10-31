package com.soeguet.gui.comments.generic_comment.gui_elements.textpane;

import com.soeguet.gui.comments.generic_comment.gui_elements.interfaces.ContentInterface;
import com.soeguet.gui.comments.generic_comment.util.Side;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;

import static javax.imageio.ImageIO.read;

public class CustomPicturePane extends JLabel implements ContentInterface, ComponentListener {

    // variables -- start
    private BufferedImage bufferedImage;
    // variables -- end

    // constructors -- start
    public CustomPicturePane(byte[] picture) {

        try {

            bufferedImage = read(new ByteArrayInputStream(picture));

        } catch (IOException ex) {

            throw new RuntimeException(ex);
        }

        setOpaque(true);
        setPictureToLabel();
        this.addMaximizePictureOnClick(this, bufferedImage);
    }

    private void setPictureToLabel() {

        ImageIcon icon = scaleImageIfTooBig(bufferedImage);
        this.setSize(icon.getIconWidth() + 300, icon.getIconHeight());
        this.setIcon(icon);
    }

    protected void addMaximizePictureOnClick(JLabel imageLabel, BufferedImage image) {

        imageLabel.addMouseListener(new MouseAdapter() {

            // overrides -- start
            @Override
            public void mouseClicked(MouseEvent e) {

                final JMenuItem menuItem = buildPopupMenu(e, imageLabel);

                addMenuItemClickListener(menuItem, image);
            }
        });
    }

    /**
     Scales an image if it is too big.

     @param bufferedImage
     The image to be scaled.

     @return The scaled image as an ImageIcon.
     */
    private ImageIcon scaleImageIfTooBig(BufferedImage bufferedImage) {

        if (bufferedImage.getWidth() > 500) {

            return new ImageIcon(bufferedImage.getScaledInstance(500, -1, Image.SCALE_AREA_AVERAGING));

        } else if (bufferedImage.getHeight() > 350) {

            return new ImageIcon(bufferedImage.getScaledInstance(-1, 350, Image.SCALE_AREA_AVERAGING));

        } else {

            return new ImageIcon(bufferedImage);
        }
    }

    public void setIcon(Icon icon) {

        super.setIcon(icon);
        if (icon != null) {
            setSize(icon.getIconWidth(), icon.getIconHeight());
        }
    }

    private JMenuItem buildPopupMenu(final MouseEvent e, final JLabel imageLabel) {

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("maximize");
        popupMenu.add(menuItem);
        popupMenu.show(imageLabel, e.getX(), e.getY());
        return menuItem;
    }

    private void addMenuItemClickListener(final JMenuItem menuItem, final BufferedImage image) {

        menuItem.addMouseListener(new MouseAdapter() {

            // overrides -- start
            @Override
            public void mousePressed(final MouseEvent e) {

                try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

                    executor.submit(() -> openImageInExternalImageViewer(image));
                }
            }
        });
    }

    private void openImageInExternalImageViewer(BufferedImage image) {

        try {

            File tempFile = File.createTempFile("tempImage", ".png");
            ImageIO.write(image, "png", tempFile);
            Desktop.getDesktop().open(tempFile);

        } catch (IOException ex) {

            throw new RuntimeException(ex);
        }
    }
    // constructors -- end

    // overrides -- start
    @Override
    public void componentResized(final ComponentEvent e) {

        setPictureToLabel();
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

}