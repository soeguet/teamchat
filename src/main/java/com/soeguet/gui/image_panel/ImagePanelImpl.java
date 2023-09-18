package com.soeguet.gui.image_panel;

import com.soeguet.gui.image_panel.generated.ImagePanel;
import com.soeguet.gui.main_frame.MainGuiElementsInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Logger;

public class ImagePanelImpl extends ImagePanel {

    private final Logger logger = Logger.getLogger(ImagePanelImpl.class.getName());
    private final JFrame mainFrame;
    private final Point offset = new Point();

    public ImagePanelImpl(JFrame mainFrame) {

        this.mainFrame = mainFrame;

        populateImagePanel();
        setPosition();
        setLayeredPaneLayerPositions();
        setupPictureScrollPaneScrollSpeed();
    }

    private void setLayeredPaneLayerPositions() {

        MainGuiElementsInterface gui = getMainFrame();
        assert gui != null;

        final int width = form_pictureMainPanel.getWidth();
        final int height = form_pictureMainPanel.getHeight();

        final int bottomMarginToTextPanel = 10;

        form_pictureScrollPane.setBounds(0, 0, width, height - form_pictureInteractionPanel.getHeight()- bottomMarginToTextPanel);

        form_zoomMotherPanel.setBounds(0, 0, width, height - form_pictureInteractionPanel.getHeight()- bottomMarginToTextPanel);
        revalidate();
        repaint();
    }

    private void setupPictureScrollPaneScrollSpeed() {

        form_pictureScrollPane.getVerticalScrollBar().setUnitIncrement(50);
        form_pictureScrollPane.getHorizontalScrollBar().setUnitIncrement(50);

    }

    public void populateImagePanelFromClipboard() {

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = clipboard.getContents(null);

        if (contents != null && contents.isDataFlavorSupported(DataFlavor.imageFlavor)) {
            try {
                BufferedImage image = (BufferedImage) contents.getTransferData(DataFlavor.imageFlavor);
                ImageIcon imageIcon = new ImageIcon(image);
                JLabel imageLabel = new JLabel(imageIcon);
                getPicturePanel().add(imageLabel);

                getPicturePanel().revalidate();
                getPicturePanel().repaint();

            } catch (UnsupportedFlavorException | IOException ex) {
                logger.log(java.util.logging.Level.SEVERE, "Could not load image from clipboard", ex);
            }
        } else {
            System.out.println("No image found in clipboard");
        }
    }

    private void setPosition() {

        MainGuiElementsInterface gui = getMainFrame();
        assert gui != null;

        int textPaneWidth = gui.getMainTextPanelLayeredPane().getWidth();
        int textPaneHeight = gui.getMainTextPanelLayeredPane().getHeight();

        this.setBounds(20, 20, textPaneWidth - 40, textPaneHeight - 40);

        gui.getMainTextPanelLayeredPane().add(this, JLayeredPane.MODAL_LAYER);
    }

    private void populateImagePanel() {

    }

    @Override
    protected void closeImagePanelButtonMouseReleased(MouseEvent e) {

        this.removeAll();
        this.setVisible(false);
    }

    private MainGuiElementsInterface getMainFrame() {

        if (!(mainFrame instanceof MainGuiElementsInterface)) {
            return null;
        }

        return (MainGuiElementsInterface) mainFrame;
    }

    @Override
    protected void thisMousePressed(MouseEvent e) {

        offset.setLocation(e.getX(), e.getY());
    }

    @Override
    protected void thisMouseDragged(MouseEvent e) {

        int x = e.getX() - offset.x + this.getX();
        int y = e.getY() - offset.y + this.getY();
        this.setLocation(x, y);
    }
}