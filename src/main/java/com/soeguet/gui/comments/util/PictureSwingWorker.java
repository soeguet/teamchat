package com.soeguet.gui.comments.util;

import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.model.jackson.PictureModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

public class PictureSwingWorker extends SwingWorker<Void, Object> {

    // variables -- start
    private final MainFrameGuiInterface mainFrame;
    private final PictureModel pictureModel;
    private final JLabel pictureLabel;
    // variables -- end

    // constructors -- start
    public PictureSwingWorker(MainFrameGuiInterface mainFrame, PictureModel pictureModel, JLabel pictureLabel) {

        this.mainFrame = mainFrame;

        this.pictureModel = pictureModel;
        this.pictureLabel = pictureLabel;
    }
    // constructors -- end

    private ImageIcon scaleImageIfTooBig() {

        ImageIcon icon = new ImageIcon(pictureModel.getPicture());

        final ImageIcon resizedIcon = resizeImage(icon);
        if (resizedIcon != null) {

            return resizedIcon;
        }

        return icon;
    }

    private ImageIcon resizeImage(final ImageIcon icon) {

        if (icon.getIconWidth() > 500 || icon.getIconHeight() > 350) {

            BufferedImage bufferedImage;

            try {

                bufferedImage = ImageIO.read(new ByteArrayInputStream(pictureModel.getPicture()));

                if (icon.getIconWidth() > 500) {

                    return new ImageIcon(bufferedImage.getScaledInstance(500, -1, Image.SCALE_SMOOTH));

                } else {

                    return new ImageIcon(bufferedImage.getScaledInstance(-1, 350, Image.SCALE_SMOOTH));
                }

            } catch (Exception ex) {

                throw new RuntimeException(ex);
            }
        }
        return null;
    }

    @Override
    protected Void doInBackground() {

        ImageIcon icon = scaleImageIfTooBig();
        pictureLabel.setIcon(icon);

        return null;
    }

    @Override
    protected void done() {

        //scroll to end after finishing the loading
        final JScrollBar verticalScrollBar = mainFrame.getMainTextBackgroundScrollPane().getVerticalScrollBar();
        verticalScrollBar.setValue(verticalScrollBar.getMaximum());

        super.done();
    }
}