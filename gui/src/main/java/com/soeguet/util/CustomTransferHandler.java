package com.soeguet.util;

import com.soeguet.image_panel.ImagePanelImpl;
import com.soeguet.image_panel.interfaces.ImageInterface;
import com.soeguet.links.LinkDialogHandler;
import com.soeguet.links.LinkDialogImpl;
import com.soeguet.links.dtos.MetadataStorageRecord;
import com.soeguet.links.interfaces.LinkDialogInterface;
import com.soeguet.main_frame.ChatMainFrameImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.logging.Logger;

public class CustomTransferHandler extends TransferHandler {

    Logger logger = Logger.getLogger(CustomTransferHandler.class.getName());
    private final TransferHandler originalTransferHandler;

    public CustomTransferHandler() {

        this.originalTransferHandler = ChatMainFrameImpl.getMainFrameInstance().getTextEditorPane().getTransferHandler();
    }

    @Override
    public boolean importData(final JComponent jComponent, final Transferable transferable) {

        if (transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {

            // image route

            // image to text pane -> activate image panel
            callImagePanel();

            return true;

        } else if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {

            // FEATURE maybe emoji detection?

            // string route
            final String data = extractStringFromTransferable(transferable);

            // check if link
            if (checkDataForLinks(data)) {
                // don't append the link to the text pane
                return false;
            }

        } else {

            logger.log(java.util.logging.Level.SEVERE, "Unsupported data flavor");
            throw new RuntimeException("Unsupported data flavor");
        }

        // append what was pasted to the text pane
        return originalTransferHandler.importData(jComponent, transferable);
    }

    private boolean checkDataForLinks(final String data) {

        if (data.startsWith("http://") || data.startsWith("https://")) {

            // send the link
            SwingUtilities.invokeLater(() -> callLinkConfirmationDialog(data));

            // don't append the link to the text pane
            return true;
        }
        return false;
    }

    private String extractStringFromTransferable(final Transferable transferable) {

        String data;

        try {

            data = (String) transferable.getTransferData(DataFlavor.stringFlavor);

        } catch (UnsupportedFlavorException | IOException e) {

            logger.log(java.util.logging.Level.SEVERE, "Error importing data", e);
            throw new RuntimeException(e);
        }
        return data;
    }

    /** Calls the methods of the ImageInterface to set up the ImagePanel. */
    private void callImagePanel() {

       ImageInterface imagePanel = new ImagePanelImpl();

        imagePanel.setPosition();
        imagePanel.setLayeredPaneLayerPositions();
        imagePanel.setupPictureScrollPaneScrollSpeed();
        imagePanel.populateImagePanelFromClipboard();
    }

    /**
     * Calls the link confirmation dialog with the given link.
     *
     * @param link the link to be displayed in the dialog
     */
    private void callLinkConfirmationDialog(String link) {

        final LinkDialogInterface linkDialog = createLinkDialog(link);
        linkDialog.generate();
    }

    /**
     * Creates a link dialog interface with the given link. It checks if there is metadata
     * associated with the link and adds the corresponding metadata panel to the content panel of
     * the dialog if it exists.
     *
     * @param link the link to be associated with the dialog
     * @return the created link dialog interface
     */
    private LinkDialogInterface createLinkDialog(final String link) {

        final LinkDialogInterface linkDialog = initializeLinkDialog(link);

        MetadataStorageRecord metadataStorageRecord = linkDialog.checkForMetaData(link);

        if (metadataStorageRecord != null) {

            JPanel metaDataPanel = linkDialog.createMetadataPanel(metadataStorageRecord);

            if (metaDataPanel != null) {

                linkDialog.getContentPanel().add(metaDataPanel, BorderLayout.CENTER);
            }
        }

        return linkDialog;
    }

    /**
     * Initializes a LinkDialogInterface with the given link.
     *
     * @param link the link to be displayed in the dialog
     * @return the initialized LinkDialogInterface
     */
    private LinkDialogInterface initializeLinkDialog(final String link) {

        LinkDialogInterface linkDialog = new LinkDialogImpl(new LinkDialogHandler());

        linkDialog.getLinkTextPane().setEditorKit(new WrapEditorKit());
        linkDialog.getLinkTextPane().setText(link);
        linkDialog.getCommentTextPane().setEditorKit(new WrapEditorKit());

        return linkDialog;
    }
}