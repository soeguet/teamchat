package com.soeguet.gui.comments.generic_comment.gui_elements.labels;

import com.soeguet.gui.comments.generic_comment.gui_elements.interfaces.ContentInterface;
import com.soeguet.gui.comments.generic_comment.gui_elements.menu_items.CustomPictureMaximizeMenuItem;
import com.soeguet.model.jackson.PictureModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static javax.imageio.ImageIO.read;

public class CustomPictureLabel extends JLabel implements ContentInterface, ComponentListener, MouseListener {

    // variables -- start
    private final PictureModel pictureModel;
    private final BufferedImage bufferedImage;
    // variables -- end

    // constructors -- start
    public CustomPictureLabel(PictureModel pictureModel) {

        this.pictureModel = pictureModel;

        try {

            bufferedImage = read(new ByteArrayInputStream(pictureModel.getPicture()));

        } catch (IOException ex) {

            throw new RuntimeException(ex);
        }

        addMouseListener(this);
    }
    // constructors -- end

    private void buildPopupMenu(final MouseEvent e) {

        JPopupMenu popupMenu = new JPopupMenu();
        CustomPictureMaximizeMenuItem menuItem = new CustomPictureMaximizeMenuItem("maximize", bufferedImage);
        popupMenu.add(menuItem);
        popupMenu.show(this, e.getX(), e.getY());
    }

    public void addPictureAsIconToLabel() {

        ImageIcon icon = scaleImageIfTooBig(bufferedImage);
        super.setSize(icon.getIconWidth() + 300, icon.getIconHeight());
        super.setIcon(icon);
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

    // overrides -- start
    @Override
    public void mouseClicked(final MouseEvent e) {

        buildPopupMenu(e);
    }

    @Override
    public void mousePressed(final MouseEvent e) {

    }

    @Override
    public void mouseReleased(final MouseEvent e) {

    }

    @Override
    public void mouseEntered(final MouseEvent e) {

    }

    @Override
    public void mouseExited(final MouseEvent e) {

    }

    @Override
    public void componentResized(final ComponentEvent e) {

        addPictureAsIconToLabel();
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
    // overrides -- end
}