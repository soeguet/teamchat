package com.soeguet.util;

import com.soeguet.model.jackson.PictureModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;
import javax.swing.*;

public class PictureSwingWorker extends SwingWorker<Void, Object> {

  // variables -- start
  private final PictureModel pictureModel;
  private final JLabel pictureLabel;

  // variables -- end

  // constructors -- start
  public PictureSwingWorker(PictureModel pictureModel, JLabel pictureLabel) {

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

    // TODO 1

    //        this.mainFrame.revalidate();
    //        this.mainFrame.repaint();
    //
    //        // scroll to end after finishing the loading
    //        final JScrollBar verticalScrollBar =
    //                mainFrame.getMainTextBackgroundScrollPane().getVerticalScrollBar();
    //        SwingUtilities.invokeLater(
    //                () -> verticalScrollBar.setValue(verticalScrollBar.getMaximum()));

    super.done();
  }
}
