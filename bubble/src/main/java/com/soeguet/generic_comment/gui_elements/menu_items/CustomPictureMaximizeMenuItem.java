package com.soeguet.generic_comment.gui_elements.menu_items;

import com.soeguet.model.jackson.PictureModel;
import java.awt.Desktop;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class CustomPictureMaximizeMenuItem extends JMenuItem implements MouseListener {

    // variables -- start
    private final PictureModel pictureModel;

    // variables -- end

    // constructors -- start
    public CustomPictureMaximizeMenuItem(String title, PictureModel pictureModel) {

        super(title);

        this.pictureModel = pictureModel;

        addMouseListener(this);
    }

    // constructors -- end

    /**
     * Opens the image stored in the picture model in an external image viewer.
     *
     * <p>This method reads the picture data from the picture model and saves it as a temporary PNG
     * file. The temporary file is then opened using the default external image viewer associated
     * with the system.
     *
     * <p>If an error occurs while opening the image in the external viewer, an error message dialog
     * is displayed.
     *
     * @throws IOException if an I/O error occurs while reading the picture data or creating the
     *     temporary file
     */
    private void openImageInExternalImageViewer() {

        try {

            BufferedImage bufferedImage =
                    ImageIO.read(new ByteArrayInputStream(pictureModel.getPicture()));

            File tempFile = File.createTempFile("tempImage", ".png");
            tempFile.deleteOnExit();

            ImageIO.write(bufferedImage, "png", tempFile);

            Desktop.getDesktop().open(tempFile);

        } catch (IOException ex) {

            JOptionPane.showMessageDialog(
                    null,
                    "Error while opening image in external viewer: %s".formatted(ex.getMessage()));
        }
    }

    @Override
    public void mouseClicked(final MouseEvent e) {}

    /**
     * Invoked when a mouse button is pressed on a component.
     *
     * <p>This method starts a new virtual thread that calls the {@link
     * #openImageInExternalImageViewer()} method. The method runs asynchronously, allowing the main
     * thread to continue processing other events.
     *
     * @param e the MouseEvent that triggered this event
     */
    @Override
    public void mousePressed(final MouseEvent e) {

        new Thread(this::openImageInExternalImageViewer).start();
    }

    @Override
    public void mouseReleased(final MouseEvent e) {}

    @Override
    public void mouseEntered(final MouseEvent e) {}

    @Override
    public void mouseExited(final MouseEvent e) {}
}