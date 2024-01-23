package com.soeguet.links;

import com.soeguet.links.dtos.LinkTransferDTO;
import com.soeguet.links.dtos.MetadataStorageRecord;
import com.soeguet.links.generated.LinkDialog;
import com.soeguet.links.interfaces.LinkDialogInterface;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class LinkDialogImpl extends LinkDialog implements LinkDialogInterface {

  private final LinkDialogHandler linkDialogHandler;

  /**
   * Constructs a new LinkDialogImpl object.
   *
   * @param mainFrame The MainFrameInterface object representing the main frame.
   * @param linkDialogHandler The LinkDialogHandler object used to handle dialog events.
   */
  public LinkDialogImpl(LinkDialogHandler linkDialogHandler) {

    // TODO 1
    super(null);
    this.linkDialogHandler = linkDialogHandler;
  }

  /**
   * Checks for metadata for a given link.
   *
   * @param link the link to check for metadata
   * @return the metadata storage record if metadata is found, else null
   */
  @Override
  public MetadataStorageRecord checkForMetaData(final String link) {

    return linkDialogHandler.checkForMetaData(link);
  }

  /**
   * Creates a metadata panel based on the provided metadata storage record.
   *
   * @param metadataStorageRecord The metadata storage record containing the information for the
   *     panel.
   * @return The created metadata panel. Returns null if no image is available.
   */
  @Override
  public JPanel createMetadataPanel(final MetadataStorageRecord metadataStorageRecord) {

    final JPanel panel = generateMetadataPanel();

    // metadata image
    if (metadataStorageRecord.previewImage() != null) {

      BufferedImage bufferedImage = metadataStorageRecord.previewImage();
      JLabel imageLabel = new JLabel(new ImageIcon(bufferedImage));
      panel.add(imageLabel, BorderLayout.CENTER);

    } else {

      // return an empty panel if no image. title is not needed if there is no image
      return null;
    }

    // metadata title
    if (metadataStorageRecord.title() != null) {

      final JTextPane titleLabel = createTitleLabel(metadataStorageRecord);
      panel.add(titleLabel, BorderLayout.NORTH);
    }

    // add a vertical spacer
    final JPanel verticalSpacer = createVerticalSpacer();
    panel.add(verticalSpacer, BorderLayout.SOUTH);

    return panel;
  }

  /**
   * Generates and displays the GUI.
   *
   * <p>This method is called to generate and display the graphical user interface (GUI).
   *
   * <p>This method performs the following steps: 1. Packs the components of the GUI, ensuring they
   * are laid out and sized correctly. 2. Sets the location of the GUI relative to the mainFrame. 3.
   * Sets the visibility of the GUI to true, making it visible to the user. 4. Requests focus for
   * the commentTextPane component, allowing user input.
   *
   * <p>There are no input parameters for this method.
   *
   * <p>There is no return value for this method.
   *
   * @see JFrame
   */
  @Override
  public void generate() {

    pack();
    // TODO 1
    setLocationRelativeTo(null);
    setVisible(true);
    getCommentTextPane().requestFocus();
  }

  /**
   * Generates a metadata panel.
   *
   * <p>This method is called to generate a JPanel which acts as a metadata panel.
   *
   * @return A JPanel representing the metadata panel.
   */
  private JPanel generateMetadataPanel() {

    return new JPanel(new BorderLayout());
  }

  /**
   * Creates a title label.
   *
   * <p>This method is called to createQuoteTopTextPane and customize a JTextPane component to be
   * used as a title label. The title label displays the title of a metadata storage record.
   *
   * @param metadataStorageRecord The metadata storage record containing the title to be displayed.
   * @return The JTextPane component representing the title label.
   * @see MetadataStorageRecord
   * @see JTextPane
   * @see Font
   */
  private JTextPane createTitleLabel(final MetadataStorageRecord metadataStorageRecord) {

    JTextPane titleLabel = new JTextPane();
    titleLabel.setEditable(false);
    titleLabel.setText(metadataStorageRecord.title());
    titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
    return titleLabel;
  }

  /**
   * Creates a vertical spacer panel.
   *
   * <p>This method creates a JPanel that acts as a vertical spacer. The panel has a preferred size
   * and minimum size of width 0 and height 10, effectively creating a vertical space of 10 pixels
   * between components.
   *
   * @return The created JPanel acting as a vertical spacer.
   * @see JPanel
   * @see Dimension
   */
  private JPanel createVerticalSpacer() {

    JPanel verticalSpacer = new JPanel();
    verticalSpacer.setPreferredSize(new Dimension(0, 10));
    verticalSpacer.setMinimumSize(new Dimension(0, 10));
    return verticalSpacer;
  }

  /**
   * Performs an action when the OK button is clicked. Sends links to Websocket.
   *
   * @param e The ActionEvent object that triggered the method call.
   */
  @Override
  protected void okButtonActionPerformed(final ActionEvent e) {

    String link = getLinkTextPane().getText();
    String comment = getCommentTextPane().getText();

    // TODO find maybe a better method instead of separating this via regex
    LinkTransferDTO linkTransferDTO = new LinkTransferDTO(link, comment);

    linkDialogHandler.sendLinkToWebsocket(linkTransferDTO);
    //        try {
    //
    //            final String serializedLinkTransferDTO =
    // mainFrame.getObjectMapper().writeValueAsString(linkTransferDTO);
    //            linkDialogHandler.sendLinkToWebsocket(mainFrame, serializedLinkTransferDTO);
    //
    //        } catch (JsonProcessingException ex) {
    //
    //            throw new RuntimeException(ex);
    //        }

    this.cancelButtonActionPerformed(e);
  }

  /**
   * Performs the action when the cancel button is clicked.
   *
   * <p>This method is called when the cancel button is clicked and performs the following actions:
   * 1. Disposes the current dialog. 2. Sets the visibility of the current dialog to false.
   *
   * @param e The ActionEvent that triggered the method.
   */
  @Override
  protected void cancelButtonActionPerformed(final ActionEvent e) {

    this.dispose();
    this.setVisible(false);
  }
}
