package com.soeguet.generic_comment.factories;

import com.soeguet.generic_comment.gui_elements.labels.CustomPictureLabel;
import com.soeguet.generic_comment.gui_elements.panels.CustomPictureWrapperPanel;
import com.soeguet.generic_comment.gui_elements.textpanes.CustomSimpleTextPane;
import com.soeguet.model.jackson.*;

public class PicturePanelFactory {

  // variables -- start
  private final PictureModel pictureModel;

  // variables -- end

  // constructors -- start
  public PicturePanelFactory(final BaseModel baseModel) {

    this.pictureModel = (PictureModel) baseModel;
  }

  // constructors -- end

  public CustomPictureWrapperPanel create() {

    /*
    SCHEMA: Picture Panel consists of:

    QUOTE
    PICTURE
    DESCRIPTION/TEXT
     */

    // WRAPPER: this is the main wrapper panel that is returned
    final CustomPictureWrapperPanel customPictureWrapperPanel = generateCustomPictureWrapperPanel();

    // QUOTE PART if not null!
    final QuoteModel<? extends BaseModel> quotedMessageModel = pictureModel.getQuotedMessage();

    if (quotedMessageModel != null) {

      String quotedText = extractQuotedText(quotedMessageModel);
      CustomSimpleTextPane customQuoteTextPane = new CustomSimpleTextPane();
      customQuoteTextPane.replaceEmojiDescriptionWithActualImageIcon(quotedText);
      customPictureWrapperPanel.add(customQuoteTextPane, "cell 0 0");
    }

    // PICTURE PART
    final CustomPictureLabel customPictureLabel = generateCustomPictureLabel();
    customPictureWrapperPanel.add(customPictureLabel, "cell 0 1, center");

    // DESCRIPTION/TEXT PART if not null!
    if (pictureModel.getDescription() != null && !pictureModel.getDescription().isBlank()) {

      CustomSimpleTextPane customSimpleTextPane = new CustomSimpleTextPane();
      customSimpleTextPane.replaceEmojiDescriptionWithActualImageIcon(
          pictureModel.getDescription());
      customPictureWrapperPanel.add(customSimpleTextPane, "cell 0 2");
    }

    return customPictureWrapperPanel;
  }

  /**
   * Extracts the quoted text from a given QuoteModel object.
   *
   * @param quotedMessageModel The QuoteModel object to extract the quoted text from.
   * @return The quoted text, depending on the type of BaseModel.
   * @throws IllegalArgumentException if the baseModel is null.
   */
  private String extractQuotedText(final QuoteModel<? extends BaseModel> quotedMessageModel) {

    final BaseModel baseModel = quotedMessageModel.t();

    if (baseModel instanceof MessageModel messageModel) {

      return messageModel.getMessage();

    } else if (baseModel instanceof PictureModel pictureModel) {

      return pictureModel.getDescription();

    } else if (baseModel instanceof LinkModel linkModel) {

      return linkModel.getLink() + System.lineSeparator() + linkModel.getComment();
    } else {

      throw new IllegalArgumentException("The baseModel is null!");
    }
  }

  /**
   * Generates a CustomPictureLabel based on the pictureModel.
   *
   * @return the generated CustomPictureLabel object
   */
  private CustomPictureLabel generateCustomPictureLabel() {

    CustomPictureLabel customPictureLabel = new CustomPictureLabel(pictureModel);
    customPictureLabel.setOpaque(true);
    return customPictureLabel;
  }

  /**
   * Generates a CustomPictureWrapperPanel.
   *
   * @return the generated CustomPictureWrapperPanel object
   */
  private CustomPictureWrapperPanel generateCustomPictureWrapperPanel() {

    CustomPictureWrapperPanel customPictureWrapperPanel = new CustomPictureWrapperPanel();
    customPictureWrapperPanel.setPictureWrapperPanelLayoutManager();
    customPictureWrapperPanel.setOpaque(false);
    return customPictureWrapperPanel;
  }
}
