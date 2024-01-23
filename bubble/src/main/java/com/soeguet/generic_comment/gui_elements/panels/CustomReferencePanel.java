package com.soeguet.generic_comment.gui_elements.panels;

import com.soeguet.generic_comment.factories.PicturePanelFactory;
import com.soeguet.generic_comment.gui_elements.textpanes.CustomLinkTextPane;
import com.soeguet.generic_comment.gui_elements.textpanes.CustomReplyPreviewTopInformationTextPane;
import com.soeguet.generic_comment.gui_elements.textpanes.CustomSimpleTextPane;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.LinkModel;
import com.soeguet.model.jackson.MessageModel;
import com.soeguet.model.jackson.PictureModel;
import com.soeguet.util.WrapEditorKit;
import java.awt.*;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public class CustomReferencePanel extends JPanel {

  // variables -- start
  private final BaseModel baseModel;

  // variables -- end

  // constructors -- start
  public CustomReferencePanel(final BaseModel baseModel) {

    this.baseModel = baseModel;

    this.setBackground(null);
    this.setOpaque(false);
  }

  // constructors -- end

  public void setLayoutManager() {

    /*

    SCHEMA: CustomReferencePanel

    [[name - time]]     #CustomReplyPreviewTopInformationTextPane
    [[quoted text]]

    */

    if (baseModel instanceof MessageModel messageModel) {

      super.setLayout(
          new MigLayout(
              "wrap",
              // columns
              "[fill,grow]",
              // rows
              "[fill][fill,grow,:150:200]"));
    } else if (baseModel instanceof PictureModel pictureModel) {

      super.setLayout(
          new MigLayout(
              "wrap",
              // columns
              "[fill,grow]",
              // rows
              "[fill][fill,grow,:300:350]"));
    } else if (baseModel instanceof LinkModel linkModel) {

      super.setLayout(
          new MigLayout(
              "wrap",
              // columns
              "[fill,grow]",
              // rows
              "[fill][fill,grow,:150:200]"));
    } else {

      throw new IllegalArgumentException("The baseModel is null!");
    }
  }

  public void createQuotedTextPane() {
    // TODO: 01.11.23 this needs to be rather complex to determine, whether the quoted text is a
    // link or a text

    if (baseModel instanceof MessageModel messageModel) {

      // TODO change this to be a TextPane?
      CustomReplyPreviewTopInformationTextPane customQuotedTextPane =
          new CustomReplyPreviewTopInformationTextPane(messageModel);
      customQuotedTextPane.createQuoteTopTextPane();
      super.add(customQuotedTextPane, "cell 0 0");

      JTextPane jTextPane = new JTextPane();
      jTextPane.setEditorKit(new WrapEditorKit());

      // FIXME: 02.11.23
      // jTextPane.setText("messageModel.getQuotedMessageText())");
      jTextPane.setEnabled(false);
      super.add(jTextPane, "cell 0 1");
    }
  }

  public void populateReferencePanel() {

    if (baseModel instanceof MessageModel messageModel) {
      JScrollPane jScrollPane = new JScrollPane();
      final CustomSimpleTextPane referenceMessageTextPane =
          createReferenceMessageTextPane(messageModel);

      jScrollPane.setViewportView(referenceMessageTextPane);
      jScrollPane.setOpaque(false);
      jScrollPane.getViewport().setOpaque(false);
      jScrollPane.setBorder(null);

      super.add(jScrollPane, "cell 0 1, grow, width ::500, h ::800");

    } else if (baseModel instanceof PictureModel pictureModel) {

      JScrollPane jScrollPane = new JScrollPane();
      jScrollPane.setSize(500, 600);

      PicturePanelFactory picturePanelFactory = new PicturePanelFactory(pictureModel);
      CustomPictureWrapperPanel customPictureWrapperPanel = picturePanelFactory.create();

      super.add(jScrollPane, "cell 0 1, grow, width 500!, h 600!, center");
      revalidate();
      repaint();

      SwingUtilities.invokeLater(() -> jScrollPane.setViewportView(customPictureWrapperPanel));

    } else if (baseModel instanceof LinkModel linkModel) {

      // LINK
      final CustomLinkTextPane customLinkTextPane = createCustomLinkTextPaneWithComment(linkModel);

      final JScrollPane jScrollPane = createScrollPane();

      // TODO 1

      //                this.mainFrame.revalidate();
      //                this.mainFrame.repaint();

      SwingUtilities.invokeLater(() -> jScrollPane.setViewportView(customLinkTextPane));

      super.add(jScrollPane, "cell 0 1, width ::500, h ::800");
    }
  }

  private JScrollPane createScrollPane() {

    JScrollPane jScrollPane = new JScrollPane();
    jScrollPane.setOpaque(false);
    jScrollPane.getViewport().setOpaque(false);
    jScrollPane.setBorder(null);
    return jScrollPane;
  }

  private CustomLinkTextPane createCustomLinkTextPaneWithComment(final LinkModel linkModel) {

    CustomLinkTextPane customLinkTextPane = new CustomLinkTextPane(linkModel);
    customLinkTextPane.create();

    return customLinkTextPane;
  }

  private CustomSimpleTextPane createReferenceMessageTextPane(final MessageModel messageModel) {

    CustomSimpleTextPane referenceMessageTextPane = new CustomSimpleTextPane();
    referenceMessageTextPane.replaceEmojiDescriptionWithActualImageIcon(messageModel.getMessage());

    return referenceMessageTextPane;
  }

  public void setupNameAndTimeTopPanel() {

    super.add(createNameAndTimeTextPane(), "cell 0 0");
  }

  private CustomSimpleTextPane createNameAndTimeTextPane() {

    CustomSimpleTextPane nameAndTimeTextPane = new CustomSimpleTextPane();

    nameAndTimeTextPane.setText("%s - %s".formatted(baseModel.getSender(), baseModel.getTime()));
    nameAndTimeTextPane.setFont(new Font(nameAndTimeTextPane.getFont().getName(), Font.ITALIC, 11));

    return nameAndTimeTextPane;
  }

  @Override
  protected void paintComponent(final Graphics g) {

    Graphics2D g2d = (Graphics2D) g;

    g2d.setColor(new Color(0, 0, 0, 10));
    g2d.fillRect(0, 0, this.getWidth(), this.getHeight());

    g2d.setColor(new Color(0, 0, 0, 100));

    // left line
    g2d.drawLine(0, 0, 0, this.getHeight() - 1);

    // right line
    g2d.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1, this.getHeight() - 1);
  }
}
