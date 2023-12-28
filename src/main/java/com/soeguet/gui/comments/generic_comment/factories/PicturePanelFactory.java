package com.soeguet.gui.comments.generic_comment.factories;

import com.soeguet.gui.comments.generic_comment.gui_elements.labels.CustomPictureLabel;
import com.soeguet.gui.comments.generic_comment.gui_elements.panels.CustomPictureWrapperPanel;
import com.soeguet.gui.comments.generic_comment.gui_elements.textpanes.CustomSimpleTextPane;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.LinkModel;
import com.soeguet.model.jackson.MessageModel;
import com.soeguet.model.jackson.PictureModel;
import com.soeguet.model.jackson.QuoteModel;

public class PicturePanelFactory {

    // variables -- start
    private final MainFrameGuiInterface mainFrame;
    private final PictureModel pictureModel;

    // variables -- end

    // constructors -- start
    public PicturePanelFactory(MainFrameGuiInterface mainFrame, final BaseModel baseModel) {

        this.mainFrame = mainFrame;

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
        final CustomPictureWrapperPanel customPictureWrapperPanel =
                generateCustomPictureWrapperPanel();

        // QUOTE PART if not null!
        final QuoteModel<? extends BaseModel> quotedMessageModel = pictureModel.getQuotedMessage();

        if (quotedMessageModel != null) {

            String quotedText = extractQuotedText(quotedMessageModel);
            CustomSimpleTextPane customQuoteTextPane = new CustomSimpleTextPane(mainFrame);
            customQuoteTextPane.replaceEmojiDescriptionWithActualImageIcon(quotedText);
            customPictureWrapperPanel.add(customQuoteTextPane, "cell 0 0");
        }

        // PICTURE PART
        final CustomPictureLabel customPictureLabel = generateCustomPictureLabel();
        customPictureWrapperPanel.add(customPictureLabel, "cell 0 1, center");

        // DESCRIPTION/TEXT PART if not null!
        if (pictureModel.getDescription() != null && !pictureModel.getDescription().isBlank()) {

            CustomSimpleTextPane customSimpleTextPane = new CustomSimpleTextPane(mainFrame);
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

        return switch (baseModel) {
            case MessageModel messageModel -> messageModel.getMessage();

            case PictureModel pictureMod -> pictureMod.getDescription();

            case LinkModel linkModel -> linkModel.getLink()
                    + System.lineSeparator()
                    + linkModel.getComment();
        };
    }

    /**
     * Generates a CustomPictureLabel based on the pictureModel.
     *
     * @return the generated CustomPictureLabel object
     */
    private CustomPictureLabel generateCustomPictureLabel() {

        CustomPictureLabel customPictureLabel = new CustomPictureLabel(mainFrame, pictureModel);
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
