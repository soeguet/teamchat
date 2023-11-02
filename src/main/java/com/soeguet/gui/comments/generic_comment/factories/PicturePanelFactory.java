package com.soeguet.gui.comments.generic_comment.factories;

import com.soeguet.gui.comments.generic_comment.gui_elements.panels.CustomPictureWrapperPanel;
import com.soeguet.gui.comments.generic_comment.gui_elements.labels.CustomPictureLabel;
import com.soeguet.gui.comments.generic_comment.gui_elements.textpane.CustomTextPane;
import com.soeguet.model.jackson.*;

import javax.swing.*;

public class PicturePanelFactory extends JLabel {

    // variables -- start
    private final PictureModel pictureModel;
    // variables -- end

    // constructors -- start
    public PicturePanelFactory(final BaseModel baseModel) {

        this.pictureModel = (PictureModel) baseModel;
    }
    // constructors -- end

    public CustomPictureWrapperPanel create() {
        // TODO: 02.11.23 implement remaining parts -- quote left
        // TEST is the quote block working? cannot test it right now since not implemented yet
        // FEATURE maybe Link support for description/text part as well?

        /*
        SCHEMA: Picture Panel consists of:

        QUOTE
        PICTURE
        DESCRIPTION/TEXT
         */

        //WRAPPER: this is the main wrapper panel that is returned
        final CustomPictureWrapperPanel customPictureWrapperPanel = generateCustomPictureWrapperPanel();

        //QUOTE PART if not null!
        final QuoteModel<? extends BaseModel> quotedMessageModel = pictureModel.getQuotedMessage();

        if (quotedMessageModel != null) {

            String quotedText = extractQuotedText(quotedMessageModel);
            CustomTextPane customQuoteTextPane = new CustomTextPane(true, quotedText);
            customPictureWrapperPanel.add(customQuoteTextPane, "cell 0 0");
        }

        //PICTURE PART
        final CustomPictureLabel customPictureLabel = generateCustomPictureLabel();
        customPictureWrapperPanel.add(customPictureLabel, "cell 0 1");

        //DESCRIPTION/TEXT PART if not null!
        final CustomTextPane customTextPane = generateCustomTextPane();
        if (customTextPane != null) customPictureWrapperPanel.add(customTextPane, "cell 0 2");

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

            case LinkModel linkModel -> linkModel.getLink() + System.lineSeparator() + linkModel.getComment();

            case null ->
                    throw new IllegalArgumentException("Unknown type: PictureBubbleFactory > extractQuotedText");
        };
    }

    /**
     * Generates a CustomTextPane based on the description from the pictureModel.
     * If the description is empty, returns null.
     *
     * @return the generated CustomTextPane object, or null if the description is empty
     */
    private CustomTextPane generateCustomTextPane() {

        if (pictureModel.getDescription().isEmpty()) {

            return null;
        }

        CustomTextPane customTextPane = new CustomTextPane(true, pictureModel.getDescription());
        customTextPane.create();
        return customTextPane;
    }

    /**
     * Generates a CustomPictureLabel based on the pictureModel.
     *
     * @return the generated CustomPictureLabel object
     */
    private CustomPictureLabel generateCustomPictureLabel() {

        CustomPictureLabel customPictureLabel = new CustomPictureLabel(pictureModel);
        customPictureLabel.setOpaque(true);
        customPictureLabel.addPictureAsIconToLabel();
        return customPictureLabel;
    }

    /**
     * Generates a CustomPictureWrapperPanel.
     *
     * @return the generated CustomPictureWrapperPanel object
     */
    private CustomPictureWrapperPanel generateCustomPictureWrapperPanel() {

        CustomPictureWrapperPanel customPictureWrapperPanel = new CustomPictureWrapperPanel();
        customPictureWrapperPanel.setOpaque(false);
        customPictureWrapperPanel.setPictureWrapperPanelLayoutManager();
        return customPictureWrapperPanel;
    }
}