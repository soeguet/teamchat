package com.soeguet.generic_comment.gui_elements.labels;

import com.soeguet.generic_comment.gui_elements.interfaces.ContentInterface;
import com.soeguet.generic_comment.gui_elements.menu_items.CustomPictureMaximizeMenuItem;
import com.soeguet.model.jackson.PictureModel;
import com.soeguet.util.PictureSwingWorker;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CustomPictureLabel extends JLabel implements ContentInterface, MouseListener {

    // variables -- start
    private final PictureModel pictureModel;

    //    private final BufferedImage bufferedImage;
    // variables -- end

    // constructors -- start
    public CustomPictureLabel(PictureModel pictureModel) {

        this.pictureModel = pictureModel;
        new PictureSwingWorker(pictureModel, this).execute();

        addMouseListener(this);
    }

    // constructors -- end

    private void buildPopupMenu(final MouseEvent e) {

        JPopupMenu popupMenu = new JPopupMenu();
        CustomPictureMaximizeMenuItem menuItem =
                new CustomPictureMaximizeMenuItem("maximize", pictureModel);
        popupMenu.add(menuItem);
        popupMenu.show(this, e.getX(), e.getY());
    }

    @Override
    public void mouseClicked(final MouseEvent e) {

        buildPopupMenu(e);
    }

    @Override
    public void mousePressed(final MouseEvent e) {}

    @Override
    public void mouseReleased(final MouseEvent e) {}

    @Override
    public void mouseEntered(final MouseEvent e) {}

    @Override
    public void mouseExited(final MouseEvent e) {}
}