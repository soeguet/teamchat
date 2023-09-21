package com.soeguet.gui.newcomment.images;

import com.soeguet.gui.main_frame.MainFrameInterface;
import com.soeguet.gui.newcomment.helper.CommentInterface;
import com.soeguet.model.jackson.PictureModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class CustomImagePanel {

    private final Logger LOGGER = Logger.getLogger(CustomImagePanel.class.getName());
    private final MainFrameInterface mainFrame;
    private final PictureModel baseModel;
    private final CommentInterface commentPanel;
    private BufferedImage image;

    public CustomImagePanel(MainFrameInterface mainFrame, CommentInterface commentPanel, PictureModel baseModel) {

        this.mainFrame = mainFrame;
        this.baseModel = baseModel;
        this.commentPanel = commentPanel;

        extractImageFromModel();
    }

    private void extractImageFromModel() {

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(baseModel.getPicture())) {

            image = ImageIO.read(byteArrayInputStream);

        } catch (IOException e) {

            LOGGER.log(java.util.logging.Level.SEVERE, "Error reading image", e);
        }

    }

    public void addImageLabelToPanel(String instruction) {

        // EDT check done!

        JLabel imageLabel = new JLabel(scaleImageIfTooBig(image));
        commentPanel.getPanel1().add(imageLabel, instruction);

        addMaximizePictureOnClick(imageLabel);
    }

    private ImageIcon scaleImageIfTooBig(BufferedImage bufferedImage) {

        if (bufferedImage == null) {

            LOGGER.log(java.util.logging.Level.SEVERE, "Buffered image is null");
            return null;
        }

        ImageIcon imageIcon;

        if (image.getWidth() > 500) {

            imageIcon = new ImageIcon(image.getScaledInstance(500, -1, Image.SCALE_AREA_AVERAGING));

        } else if (image.getHeight() > 350) {

            imageIcon = new ImageIcon(image.getScaledInstance(-1, 350, Image.SCALE_AREA_AVERAGING));

        } else {

            imageIcon = new ImageIcon(image);
        }

        return imageIcon;
    }


    private void addMaximizePictureOnClick(JLabel imageLabel) {

        imageLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                new Thread(() -> {

                    File imgFile = new File("temp-image.jpg");

                    try {

                        ImageIO.write(image, "png", imgFile);

                    } catch (IOException ex) {

                        LOGGER.log(java.util.logging.Level.SEVERE, "Error writing image", ex);
                    }

                    if (Desktop.isDesktopSupported()) {

                        try {

                            if (imgFile.exists()) {

                                Desktop.getDesktop().open(imgFile);

                            } else {

                                LOGGER.log(java.util.logging.Level.SEVERE, "Image file does not exist");
                                throw new IOException();

                            }

                        } catch (IOException ex) {

                            LOGGER.log(java.util.logging.Level.SEVERE, "Error opening image", ex);
                        }

                    } else {

                        LOGGER.log(java.util.logging.Level.SEVERE, "Desktop not supported");
                    }

                    if (!imgFile.delete()) {

                        LOGGER.log(java.util.logging.Level.SEVERE, "Error deleting temp image file");
                    }
                });
            }
        });
    }
}