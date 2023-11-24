package com.soeguet.gui.comments.generic_comment.gui_elements.labels;

import com.soeguet.gui.comments.generic_comment.gui_elements.interfaces.ContentInterface;
import com.soeguet.gui.comments.generic_comment.gui_elements.menu_items.CustomPictureMaximizeMenuItem;
import com.soeguet.gui.comments.util.PictureSwingWorker;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
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

public class CustomPictureLabel extends JLabel implements ContentInterface, MouseListener {

    // variables -- start
    private final PictureModel pictureModel;
//    private final BufferedImage bufferedImage;
    // variables -- end

    // constructors -- start
    public CustomPictureLabel(MainFrameGuiInterface mainFrame, PictureModel pictureModel) {

        this.pictureModel = pictureModel;
        new PictureSwingWorker(mainFrame, pictureModel, this).execute();

        addMouseListener(this);
    }
    // constructors -- end

    private void buildPopupMenu(final MouseEvent e) {

        JPopupMenu popupMenu = new JPopupMenu();
        CustomPictureMaximizeMenuItem menuItem = new CustomPictureMaximizeMenuItem("maximize",
                                                                                   pictureModel);
        popupMenu.add(menuItem);
        popupMenu.show(this, e.getX(), e.getY());
    }

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
}