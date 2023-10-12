package com.soeguet.gui.option_pane.links;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.soeguet.gui.main_frame.interfaces.MainFrameInterface;
import com.soeguet.gui.option_pane.links.dtos.AbsoluteLinkRecord;
import com.soeguet.gui.option_pane.links.dtos.MetadataStorageRecord;
import com.soeguet.gui.option_pane.links.generated.LinkDialog;
import com.soeguet.gui.option_pane.links.interfaces.LinkDialogInterface;
import com.soeguet.model.MessageTypes;
import com.soeguet.model.jackson.MessageModel;
import org.jsoup.nodes.Document;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

public class LinkDialogImpl extends LinkDialog implements LinkDialogInterface {

    private final MainFrameInterface mainFrame;
    private final LinkDialogHandler linkDialogHandler;

    public LinkDialogImpl(MainFrameInterface mainFrame, LinkDialogHandler linkDialogHandler) {

        super((Window) mainFrame);
        this.mainFrame = mainFrame;
        this.linkDialogHandler = linkDialogHandler;
    }

    public LinkDialogImpl() {

        super(null);
        mainFrame = null;
        linkDialogHandler = new LinkDialogHandler();
    }

    @Override
    public MetadataStorageRecord checkForMetaData(final String link) {

        //if GET request is ok -> look for metadata
        int statusCode = linkDialogHandler.checkStatusCodeOfLink(link);
        if (statusCode < 400) {

            //check if it is an absolute link -> if not, there are most likely no metadata
            AbsoluteLinkRecord absoluteLinkRecord = linkDialogHandler.validateUri(link);

            if (absoluteLinkRecord.isAbsoluteLink()) {

                Document doc = linkDialogHandler.loadDocumentForLink(absoluteLinkRecord.link());

                //fetch metadata -> title and preview image
                return linkDialogHandler.fetchMetaDataFromLink(doc);
            }
        }

        return null;
    }

    @Override
    public JPanel createMetadataPanel(final MetadataStorageRecord metadataStorageRecord) {

        final JPanel panel = generateMetadataPanel();

        //metadata image
        if (metadataStorageRecord.previewImage() != null) {

            BufferedImage bufferedImage = metadataStorageRecord.previewImage();
            JLabel imageLabel = new JLabel(new ImageIcon(bufferedImage));
            panel.add(imageLabel, BorderLayout.CENTER);

        } else {

            //return an empty panel if no image. title is not needed if there is no image
            return null;
        }

        //metadata title
        if (metadataStorageRecord.title() != null) {

            final JTextPane titleLabel = createTitleLabel(metadataStorageRecord);
            panel.add(titleLabel, BorderLayout.NORTH);
        }

        //add a vertical spacer
        final JPanel verticalSpacer = createVerticalSpacer();
        panel.add(verticalSpacer, BorderLayout.SOUTH);

        return panel;
    }

    /**
     Generates and displays the GUI.

     This method is called to generate and display the graphical user interface (GUI).

     This method performs the following steps:
     1. Packs the components of the GUI, ensuring they are laid out and sized correctly.
     2. Sets the location of the GUI relative to the mainFrame.
     3. Sets the visibility of the GUI to true, making it visible to the user.
     4. Requests focus for the commentTextPane component, allowing user input.

     There are no input parameters for this method.

     There is no return value for this method.

     @see JFrame
     */
    @Override
    public void generate() {

        pack();
        setLocationRelativeTo((JFrame) mainFrame);
        setVisible(true);
        getCommentTextPane().requestFocus();
    }

    private JPanel generateMetadataPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new LineBorder(Color.BLACK, 1));
        return panel;
    }

    private JTextPane createTitleLabel(final MetadataStorageRecord metadataStorageRecord) {

        JTextPane titleLabel = new JTextPane();
        titleLabel.setEditable(false);
        titleLabel.setText(metadataStorageRecord.title());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        return titleLabel;
    }

    private JPanel createVerticalSpacer() {

        JPanel verticalSpacer = new JPanel();
        verticalSpacer.setPreferredSize(new Dimension(0, 10));
        verticalSpacer.setMinimumSize(new Dimension(0, 10));
        return verticalSpacer;
    }

    @Override
    protected void okButtonActionPerformed(final ActionEvent e) {

        String link = getLinkTextPane().getText();
        String comment = getCommentTextPane().getText();
        String message = link + "{LINK}" + comment;

        MessageModel messageModel = new MessageModel((byte) MessageTypes.LINK, mainFrame.getUsername(), message);

        try {

            String messageString = mainFrame.getObjectMapper().writeValueAsString(messageModel);
            mainFrame.getWebsocketClient().send(messageString);

        } catch (JsonProcessingException ex) {

            throw new RuntimeException(ex);
        }

        this.cancelButtonActionPerformed(e);
    }

    @Override
    protected void cancelButtonActionPerformed(final ActionEvent e) {

        this.dispose();
        this.setVisible(false);
    }
}