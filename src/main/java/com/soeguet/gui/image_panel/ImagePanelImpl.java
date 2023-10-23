package com.soeguet.gui.image_panel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.soeguet.gui.image_panel.generated.ImagePanel;
import com.soeguet.gui.image_panel.interfaces.ImageInterface;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalTime;
import java.util.logging.Logger;

public class ImagePanelImpl extends ImagePanel implements ImageInterface {

    private final Logger LOGGER = Logger.getLogger(ImagePanelImpl.class.getName());
    private final MainFrameGuiInterface mainFrame;
    private final Point offset = new Point();
    private double zoomFactor = 1.0;
    private BufferedImage image;

    /**
     * Constructs a new instance of ImagePanelImpl.
     *
     * @param mainFrame the main frame interface that is used as a reference
     *                  to the main frame of the application.
     */
    public ImagePanelImpl(MainFrameGuiInterface mainFrame) {

        this.mainFrame = mainFrame;
    }

    /**
     * Sets the position of the ImagePanelImpl within the MainFrameInterface.
     *
     * This method calculates the dimensions of the MainTextPanelLayeredPane and
     * sets the bounds of the ImagePanelImpl accordingly. It then adds the ImagePanelImpl
     * to the MainTextPanelLayeredPane.
     */
    @Override
    public void setPosition() {

        int textPaneWidth = mainFrame.getMainTextPanelLayeredPane().getWidth();
        int textPaneHeight = mainFrame.getMainTextPanelLayeredPane().getHeight();

        this.setBounds(20, 20, textPaneWidth - 40, textPaneHeight - 40);

        mainFrame.getMainTextPanelLayeredPane().add(this, JLayeredPane.MODAL_LAYER);
    }

    /**
     * Sets the layer positions within the LayeredPane of the ImagePanelImpl.
     *
     * This method calculates the dimensions of the form_pictureMainPanel and sets
     * the bounds of the form_pictureScrollPane and form_zoomMotherPanel accordingly.
     * It then triggers the redrawEverything() method to update the display.
     */
    @Override
    public void setLayeredPaneLayerPositions() {

        final int width = form_pictureMainPanel.getWidth();
        final int height = form_pictureMainPanel.getHeight();

        final int bottomMarginToTextPanel = 10;

        form_pictureScrollPane.setBounds(0, 0, width, height - form_pictureInteractionPanel.getHeight() - bottomMarginToTextPanel);

        form_zoomMotherPanel.setBounds(0, 0, width, height - form_pictureInteractionPanel.getHeight() - bottomMarginToTextPanel);
        redrawEverything();
    }

    /**
     * Sets the scroll speed of the vertical and horizontal scrollbars in the picture scroll pane.
     *
     * This method sets the unit increment of both the vertical and horizontal scrollbars in the
     * form_pictureScrollPane to 50.
     * This determines how far the scrollbars move when the user interacts with them.
     * Increasing the unit increment will make the scrollbars move faster, while decreasing it
     * will make them move slower.
     */
    @Override
    public void setupPictureScrollPaneScrollSpeed() {

        form_pictureScrollPane.getVerticalScrollBar().setUnitIncrement(50);
        form_pictureScrollPane.getHorizontalScrollBar().setUnitIncrement(50);
    }

    /**
     Redraws everything by revalidating the component and repainting it.
     */
    private void redrawEverything() {

        revalidate();
        repaint();
    }

    /**
     * Populates the image panel with the image from the system clipboard, if available.
     */
    @Override
    public void populateImagePanelFromClipboard() {

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
                throw new RuntimeException();

            }

        } else {

            LOGGER.log(java.util.logging.Level.SEVERE, "Clipboard does not contain an image");
            throw new RuntimeException();
        }
    }

    /**
     Creates a JLabel with the specified ImageIcon as its content.

     @param newImage the ImageIcon to set as the content of the label

     @return a new JLabel with the specified image as its content
     */
    private JLabel createImageLabel(ImageIcon newImage) {

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

        destructImagePanel();
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

        zoomFactor += 0.2;

        addImageToPanel(zoomFactor);
    }

    /**
     * Sends the picture to the WebSocket.
     *
     * @param e the MouseEvent object representing the event
     */
    @Override
    protected void sendPictureButtonMouseClicked(MouseEvent e) {

        //edt tested
        if (isImageEmpty()) return;

        //convert image to a byte array
        try (var baos = new ByteArrayOutputStream()) {

            //bytearrayoutputstream to byte array
            ImageIO.write(image, "png", baos);
            byte[] imageBytesArray = baos.toByteArray();

            //setup model and convert to json
            PictureModel pictureModel = buildPictureModelForWebSocket(imageBytesArray);
            final String imageObjectJson = convertPictureModelToJson(pictureModel);

            //send json to websocket
            sendPictureMessageToWebSocket(imageObjectJson);

            //destruct panel
            destructImagePanel();

        } catch (IOException ex) {

            LOGGER.log(java.util.logging.Level.SEVERE, "Could not convert image to byte array", ex);
            throw new RuntimeException();
        }
    }

    /**
     * Checks if the image is empty (null).
     *
     * @return true if the image is empty, false otherwise
     */
    private boolean isImageEmpty() {

        return image == null;
    }

    /**
     * Builds a PictureModel object for WebSocket communication based on the provided image byte array.
     *
     * @param imageBytesArray the byte array representation of the image
     * @return a PictureModel object with the image, sender, time, and message information set
     */
    private PictureModel buildPictureModelForWebSocket(final byte[] imageBytesArray) {

        PictureModel pictureModel = new PictureModel();

        pictureModel.setPicture(imageBytesArray);
        pictureModel.setSender(mainFrame.getUsername());
        pictureModel.setTime(LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")));
        pictureModel.setMessage(form_pictureDescriptionTextField.getText());

        return pictureModel;
    }

    /**
     * Converts a PictureModel object to its JSON representation.
     *
     * @param pictureModel the PictureModel object to be converted
     * @return a JSON string representing the PictureModel object
     * @throws JsonProcessingException if an error occurs during the JSON processing
     */
    private String convertPictureModelToJson(final PictureModel pictureModel) throws JsonProcessingException {

        return mainFrame.getObjectMapper().writeValueAsString(pictureModel);
    }

    /**
     * Sends a picture message to a WebSocket server.
     *
     * @param imageObjectJson the JSON representation of the picture message
     */
    private void sendPictureMessageToWebSocket(final String imageObjectJson) {

        mainFrame.getWebsocketClient().send(imageObjectJson);
    }

    /**
     * Handles the mouse click event for the selectPictureButton.
     *
     * @param e the MouseEvent object representing the mouse click event
     */
    @Override
    protected void selectPictureButtonMouseClicked(MouseEvent e) {

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
                throw new RuntimeException();
            }
        }
    }

    /**
     Zooms the mother panel based on the mouse wheel movement.

     @param e the MouseWheelEvent object representing the event
     */
    @Override
    protected void zoomMotherPanelMouseWheelMoved(MouseWheelEvent e) {

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

    /**
     * Removes all components from the image panel and makes it invisible.
     */
    private void destructImagePanel() {

        this.removeAll();
        this.setVisible(false);
    }
}