package com.soeguet.gui.image_panel;

import com.soeguet.gui.image_panel.generated.ImagePanel;
import com.soeguet.gui.main_frame.MainGuiElementsInterface;
import org.jetbrains.annotations.NotNull;

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
import java.util.logging.Logger;

public class ImagePanelImpl extends ImagePanel {

    private final Logger logger = Logger.getLogger(ImagePanelImpl.class.getName());
    private final JFrame mainFrame;
    private final Point offset = new Point();
    private double zoomFactor = 1.0;
    private BufferedImage image;

    public ImagePanelImpl(JFrame mainFrame) {

        this.mainFrame = mainFrame;

        populateImagePanel();
        setPosition();
        setLayeredPaneLayerPositions();
        setupPictureScrollPaneScrollSpeed();
    }

    private void populateImagePanel() {

    }

    private void setPosition() {

        MainGuiElementsInterface gui = getMainFrame();
        assert gui != null;

        int textPaneWidth = gui.getMainTextPanelLayeredPane().getWidth();
        int textPaneHeight = gui.getMainTextPanelLayeredPane().getHeight();

        this.setBounds(20, 20, textPaneWidth - 40, textPaneHeight - 40);

        gui.getMainTextPanelLayeredPane().add(this, JLayeredPane.MODAL_LAYER);
    }

    private void setLayeredPaneLayerPositions() {

        MainGuiElementsInterface gui = getMainFrame();
        assert gui != null;

        final int width = form_pictureMainPanel.getWidth();
        final int height = form_pictureMainPanel.getHeight();

        final int bottomMarginToTextPanel = 10;

        form_pictureScrollPane.setBounds(0, 0, width, height - form_pictureInteractionPanel.getHeight() - bottomMarginToTextPanel);

        form_zoomMotherPanel.setBounds(0, 0, width, height - form_pictureInteractionPanel.getHeight() - bottomMarginToTextPanel);
        redrawEverything();
    }

    private void setupPictureScrollPaneScrollSpeed() {

        form_pictureScrollPane.getVerticalScrollBar().setUnitIncrement(50);
        form_pictureScrollPane.getHorizontalScrollBar().setUnitIncrement(50);
    }

    private MainGuiElementsInterface getMainFrame() {

        if (!(mainFrame instanceof MainGuiElementsInterface)) {
            return null;
        }

        return (MainGuiElementsInterface) mainFrame;
    }

    /**
     Redraws everything by revalidating the component and repainting it.
     */
    private void redrawEverything() {

        revalidate();
        repaint();
    }

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
                logger.log(java.util.logging.Level.SEVERE, "Could not load image from clipboard", ex);
            }
        } else {
            System.out.println("No image found in clipboard");
        }
    }

    /**
     Creates a JLabel with the specified ImageIcon as its content.

     @param newImage the ImageIcon to set as the content of the label

     @return a new JLabel with the specified image as its content
     */
    @NotNull
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

        zoomFactor -= 0.1;

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

        return image.getScaledInstance((int) (image.getWidth(null) * zoomFactor), (int) (image.getHeight(null) * zoomFactor), Image.SCALE_SMOOTH);
    }

    @Override
    protected void zoomOutButtonMousePressed(MouseEvent e) {

        System.out.println("zoomOutButtonMousePressed");
        zoomFactor -= 0.1;

        addImageToPanel(zoomFactor);
    }

    /**
     Increases the zoom factor by 0.1 and adds the image to the panel with the new zoom factor.

     @param e the MouseEvent object representing the event
     */
    @Override
    protected void zoomInButtonMouseClicked(MouseEvent e) {

        zoomFactor += 0.1;

        addImageToPanel(zoomFactor);
    }

    @Override
    protected void zoomInButtonMousePressed(MouseEvent e) {

        System.out.println("zoomInButtonMousePressed");
        zoomFactor += 0.1;

        addImageToPanel(zoomFactor);
    }



    /**
     * Zooms the mother panel based on the mouse wheel movement.
     *
     * @param e the MouseWheelEvent object representing the event
     */
    @Override
    protected void zoomMotherPanelMouseWheelMoved(MouseWheelEvent e) {

        if (e.isControlDown()) {

            SwingUtilities.invokeLater(() -> {

                if (e.getWheelRotation() < 0) {

                    zoomFactor += 0.1;

                } else {

                    zoomFactor -= 0.1;
                }

                addImageToPanel(zoomFactor);
                redrawEverything();

            });

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
        }

    }
}