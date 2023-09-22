package com.soeguet.gui.image_panel;

import com.soeguet.gui.image_panel.generated.ImagePanel;
import com.soeguet.gui.main_frame.MainFrameInterface;
import com.soeguet.model.jackson.PictureModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalTime;
import java.util.logging.Logger;

public class ImagePanelImpl extends ImagePanel {

    private final Logger LOGGER = Logger.getLogger(ImagePanelImpl.class.getName());
    private final MainFrameInterface mainFrame;
    private final Point offset = new Point();
    private double zoomFactor = 1.0;
    private BufferedImage image;

    public ImagePanelImpl(MainFrameInterface mainFrame) {

        ensureEDT();

        this.mainFrame = mainFrame;

        populateImagePanel();
        setPosition();
        setLayeredPaneLayerPositions();
        setupPictureScrollPaneScrollSpeed();
    }

    /**
     Ensures that the current code is running on the Event Dispatch Thread (EDT). If the current thread is not the EDT, an
     IllegalStateException is thrown.

     @throws IllegalStateException if the current code is not running on the EDT
     */
    private void ensureEDT() {

        if (!SwingUtilities.isEventDispatchThread()) {
            throw new IllegalStateException("This should run on the EDT");
        }
    }

    private void populateImagePanel() {

    }

    private void setPosition() {

        ensureEDT();

        int textPaneWidth = mainFrame.getMainTextPanelLayeredPane().getWidth();
        int textPaneHeight = mainFrame.getMainTextPanelLayeredPane().getHeight();

        this.setBounds(20, 20, textPaneWidth - 40, textPaneHeight - 40);

        mainFrame.getMainTextPanelLayeredPane().add(this, JLayeredPane.MODAL_LAYER);
    }

    private void setLayeredPaneLayerPositions() {

        ensureEDT();

        final int width = form_pictureMainPanel.getWidth();
        final int height = form_pictureMainPanel.getHeight();

        final int bottomMarginToTextPanel = 10;

        form_pictureScrollPane.setBounds(0, 0, width, height - form_pictureInteractionPanel.getHeight() - bottomMarginToTextPanel);

        form_zoomMotherPanel.setBounds(0, 0, width, height - form_pictureInteractionPanel.getHeight() - bottomMarginToTextPanel);
        redrawEverything();
    }

    private void setupPictureScrollPaneScrollSpeed() {

        ensureEDT();

        form_pictureScrollPane.getVerticalScrollBar().setUnitIncrement(50);
        form_pictureScrollPane.getHorizontalScrollBar().setUnitIncrement(50);
    }

    /**
     Redraws everything by revalidating the component and repainting it.
     */
    private void redrawEverything() {

        ensureEDT();

        revalidate();
        repaint();
    }

    public void populateImagePanelFromClipboard() {

        ensureEDT();

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = clipboard.getContents(null);

        if (contents != null && contents.isDataFlavorSupported(DataFlavor.imageFlavor)) {

            try {

                image = (BufferedImage) contents.getTransferData(DataFlavor.imageFlavor);

                JLabel imageLabel = createImageLabel(new ImageIcon(image));
                form_picturePanel.add(imageLabel);

                redrawEverything();

            } catch (UnsupportedFlavorException | IOException ex) {

                LOGGER.log(java.util.logging.Level.SEVERE, "Could not load image from clipboard", ex);

            }

        } else {

            LOGGER.log(java.util.logging.Level.SEVERE, "Clipboard does not contain an image");
        }
    }

    /**
     Creates a JLabel with the specified ImageIcon as its content.

     @param newImage the ImageIcon to set as the content of the label

     @return a new JLabel with the specified image as its content
     */
    private static JLabel createImageLabel(ImageIcon newImage) {

        return new JLabel(newImage);
    }

    /**
     This method is called when the mouse is pressed within a component.
     It updates the offset with the current mouse coordinates.

     @param e the MouseEvent representing the mouse press event
     */
    @Override
    protected void thisMousePressed(MouseEvent e) {

        offset.setLocation(e.getX(), e.getY());
    }

    /**
     Handles the dragging of the mouse on the component.
     Adjusts the location of the component based on the mouse movement.

     @param e the MouseEvent object representing the event
     */
    @Override
    protected void thisMouseDragged(MouseEvent e) {

        int x = e.getX() - offset.x + this.getX();
        int y = e.getY() - offset.y + this.getY();
        this.setLocation(x, y);
    }

    /**
     Removes all components from the panel and sets visibility to false.

     @param e the MouseEvent object representing the event
     */
    @Override
    protected void closeImagePanelButtonMouseReleased(MouseEvent e) {

        this.removeAll();
        this.setVisible(false);
    }

    /**
     Decreases the zoom factor by 0.1 and adds the image to the panel with the new zoom factor.

     @param e the MouseEvent object representing the event
     */
    @Override
    protected void zoomOutButtonMouseClicked(MouseEvent e) {

        zoomFactor -= 0.2;

        if (zoomFactor < 0.2) {
            zoomFactor = 0.2;
        }

        addImageToPanel(zoomFactor);
    }

    /**
     Removes all existing components from the picture panel, resizes the image based on the specified zoom factor,
     creates a new image label with the resized image, adds the image label to the picture panel, and triggers a revalidation
     and repaint of the panel.

     @param zoomFactor the zoom factor to be applied when resizing the image
     */
    private void addImageToPanel(double zoomFactor) {

        SwingUtilities.invokeLater(() -> {

            form_picturePanel.removeAll();

            Image newImage = resizeImage(zoomFactor);
            JLabel imageLabel = createImageLabel(new ImageIcon(newImage));
            form_picturePanel.add(imageLabel);

            redrawEverything();
        });
    }

    /**
     Resizes the image with the specified zoom factor.

     @param zoomFactor the zoom factor to apply to the image

     @return the resized image
     */
    private Image resizeImage(double zoomFactor) {

        ensureEDT();

        int width = (int) (image.getWidth(null) * zoomFactor);
        if (width < 100) {
            width = 100;
        }

        int height = (int) (image.getHeight(null) * zoomFactor);
        if (height < 100) {
            height = 100;
        }

        return image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    /**
     Increases the zoom factor by 0.1 and adds the image to the panel with the new zoom factor.

     @param e the MouseEvent object representing the event
     */
    @Override
    protected void zoomInButtonMouseClicked(MouseEvent e) {

        ensureEDT();

        zoomFactor += 0.2;

        addImageToPanel(zoomFactor);
    }

    @Override
    protected void sendPictureButtonMouseClicked(MouseEvent e) {

        ensureEDT();

        PictureModel pictureModel = new PictureModel();

        //convert image to byte array
        try (java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream()) {

            ImageIO.write(image, "png", baos);

            byte[] imageBytesArray = baos.toByteArray();

            pictureModel.setPicture(imageBytesArray);
            pictureModel.setSender(mainFrame.getUsername());
            pictureModel.setTime(LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")));
            pictureModel.setMessage(form_pictureDescriptionTextField.getText());

            final String imageObjectJson = mainFrame.getObjectMapper().writeValueAsString(pictureModel);

            mainFrame.getWebsocketClient().send(imageObjectJson);

            this.removeAll();
            this.setVisible(false);

        } catch (IOException ex) {

            LOGGER.log(java.util.logging.Level.SEVERE, "Could not convert image to byte array", ex);
        }
    }

    @Override
    protected void selectPictureButtonMouseClicked(MouseEvent e) {

        ensureEDT();

        JFileChooser jFileChooser = new JFileChooser(System.getProperty("user.home"));
        jFileChooser.setDialogTitle("Select a picture");
        jFileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {

            @Override
            public boolean accept(java.io.File f) {

                return f.getName().toLowerCase().endsWith(".png") || f.getName().toLowerCase().endsWith(".jpg") || f.isDirectory();
            }

            @Override
            public String getDescription() {

                return "PNG Images (*.png), JPG Images (*.jpg)";
            }
        });

        jFileChooser.setAcceptAllFileFilterUsed(false);
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jFileChooser.setMultiSelectionEnabled(false);

        int result = jFileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {

            java.io.File selectedFile = jFileChooser.getSelectedFile();

            try {

                form_picturePanel.removeAll();

                image = ImageIO.read(selectedFile);

                JLabel imageLabel = createImageLabel(new ImageIcon(image));

                form_picturePanel.add(imageLabel);

                redrawEverything();

            } catch (IOException ex) {
                LOGGER.log(java.util.logging.Level.SEVERE, "Could not load image from file", ex);
            }
        }
    }

    /**
     Zooms the mother panel based on the mouse wheel movement.

     @param e the MouseWheelEvent object representing the event
     */
    @Override
    protected void zoomMotherPanelMouseWheelMoved(MouseWheelEvent e) {

        ensureEDT();

        if (e.isControlDown()) {

            if (e.getWheelRotation() < 0) {

                zoomFactor += 0.1;

            } else {

                zoomFactor -= 0.1;

                if (zoomFactor < 0.2) {
                    zoomFactor = 0.2;
                }
            }

            addImageToPanel(zoomFactor);

        } else {

            if (e.isShiftDown()) {

                if (e.getWheelRotation() < 0) {

                    form_pictureScrollPane.getHorizontalScrollBar().setValue(form_pictureScrollPane.getHorizontalScrollBar().getValue() - 50);
                } else {

                    form_pictureScrollPane.getHorizontalScrollBar().setValue(form_pictureScrollPane.getHorizontalScrollBar().getValue() + 50);
                }

            } else {

                if (e.getWheelRotation() < 0) {

                    form_pictureScrollPane.getVerticalScrollBar().setValue(form_pictureScrollPane.getVerticalScrollBar().getValue() - 50);
                } else {

                    form_pictureScrollPane.getVerticalScrollBar().setValue(form_pictureScrollPane.getVerticalScrollBar().getValue() + 50);
                }
            }

            redrawEverything();
        }
    }
}