package com.soeguet.gui.option_pane.links;

import com.soeguet.gui.main_frame.interfaces.MainFrameInterface;
import com.soeguet.gui.option_pane.links.dtos.MetadataStorageRecord;
import com.soeguet.gui.option_pane.links.generated.LinkDialog;
import com.soeguet.gui.option_pane.links.interfaces.LinkDialogInterface;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

public class LinkDialogImpl extends LinkDialog implements LinkDialogInterface {

    private final MainFrameInterface mainFrame;
    private final LinkDialogHandler linkDialogHandler;

    /**
     Constructs a new LinkDialogImpl object.

     @param mainFrame         The MainFrameInterface object representing the main frame.
     @param linkDialogHandler The LinkDialogHandler object used to handle dialog events.
     */
    public LinkDialogImpl(MainFrameInterface mainFrame, LinkDialogHandler linkDialogHandler) {

        super((Window) mainFrame);
        this.mainFrame = mainFrame;
        this.linkDialogHandler = linkDialogHandler;
    }

    /**
     Checks for metadata for a given link.

     @param link the link to check for metadata

     @return the metadata storage record if metadata is found, else null
     */
    @Override
    public MetadataStorageRecord checkForMetaData(final String link) {

        return linkDialogHandler.checkForMetaData(link);
    }

    /**
     Creates a metadata panel based on the provided metadata storage record.

     @param metadataStorageRecord The metadata storage record containing the information for the panel.

     @return The created metadata panel.
     Returns null if no image is available.
     */
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

    /**
     Generates a metadata panel.

     This method is called to generate a JPanel which acts as a metadata panel.

     @return A JPanel representing the metadata panel.
     */
    private JPanel generateMetadataPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new LineBorder(Color.BLACK, 1));
        return panel;
    }

    /**
     Creates a title label.

     This method is called to create and customize a JTextPane component to be used as a title label.
     The title label displays the title of a metadata storage record.

     @param metadataStorageRecord The metadata storage record containing the title to be displayed.

     @return The JTextPane component representing the title label.

     @see MetadataStorageRecord
     @see JTextPane
     @see Font
     */
    private JTextPane createTitleLabel(final MetadataStorageRecord metadataStorageRecord) {

        JTextPane titleLabel = new JTextPane();
        titleLabel.setEditable(false);
        titleLabel.setText(metadataStorageRecord.title());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        return titleLabel;
    }

    /**
     Creates a vertical spacer panel.

     This method creates a JPanel that acts as a vertical spacer.
     The panel has a preferred size and minimum size of width 0 and height 10, effectively creating a vertical space
     of 10 pixels between components.

     @return The created JPanel acting as a vertical spacer.

     @see JPanel
     @see Dimension
     */
    private JPanel createVerticalSpacer() {

        JPanel verticalSpacer = new JPanel();
        verticalSpacer.setPreferredSize(new Dimension(0, 10));
        verticalSpacer.setMinimumSize(new Dimension(0, 10));
        return verticalSpacer;
    }

    /**
     Performs an action when the OK button is clicked. Sends links to Websocket.

     @param e The ActionEvent object that triggered the method call.
     */
    @Override
    protected void okButtonActionPerformed(final ActionEvent e) {

        String link = getLinkTextPane().getText();
        String comment = getCommentTextPane().getText();

        //TODO find maybe a better method instead of separating this via regex
        String message = link + "{LINK}" + comment;

        linkDialogHandler.sendLinkToWebsocket(mainFrame, message);

        this.cancelButtonActionPerformed(e);
    }

    /**
     Performs the action when the cancel button is clicked.

     This method is called when the cancel button is clicked and performs the following actions:
     1. Disposes the current dialog.
     2. Sets the visibility of the current dialog to false.

     @param e The ActionEvent that triggered the method.
     */
    @Override
    protected void cancelButtonActionPerformed(final ActionEvent e) {

        this.dispose();
        this.setVisible(false);
    }
}