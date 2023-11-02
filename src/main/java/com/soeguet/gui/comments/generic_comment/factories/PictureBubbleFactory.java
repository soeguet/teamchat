package com.soeguet.gui.comments.generic_comment.factories;

import com.soeguet.gui.comments.generic_comment.gui_elements.panels.CustomPictureWrapperPanel;
import com.soeguet.gui.comments.generic_comment.gui_elements.labels.CustomPictureLabel;
import com.soeguet.gui.comments.generic_comment.gui_elements.textpane.CustomTextPane;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.PictureModel;

import javax.swing.*;

public class PictureBubbleFactory extends JLabel {

    // variables -- start
    private final PictureModel pictureModel;
    // variables -- end

    // constructors -- start
    public PictureBubbleFactory(final BaseModel baseModel) {

        this.pictureModel = (PictureModel) baseModel;
    }
    // constructors -- end

    public CustomPictureWrapperPanel create() {
        // TODO: 02.11.23 implement remaining parts -- quote left
        // FEATURE maybe Link support for description/text part as well?

        /*
        SCHEMA: Picture Panel consists of:

        QUOTE
        PICTURE
        DESCRIPTION/TEXT
         */

        //WRAPPER: this is the main wrapper panel that is returned
        final CustomPictureWrapperPanel customPictureWrapperPanel = generateCustomPictureWrapperPanel();

        //QUOTE PART
        //[...]

        //PICTURE PART
        final CustomPictureLabel customPictureLabel = generateCustomPictureLabel();
        customPictureWrapperPanel.add(customPictureLabel, "cell 0 1");

        //DESCRIPTION/TEXT PART
        final CustomTextPane customTextPane = generateCustomTextPane();
        customPictureWrapperPanel.add(customTextPane, "cell 0 2");

        return customPictureWrapperPanel;
    }

    private CustomTextPane generateCustomTextPane() {

        CustomTextPane customTextPane = new CustomTextPane(true, pictureModel.getDescription());
        customTextPane.create();
        return customTextPane;
    }

    private CustomPictureLabel generateCustomPictureLabel() {

        CustomPictureLabel customPictureLabel = new CustomPictureLabel(pictureModel);
        customPictureLabel.setOpaque(true);
        customPictureLabel.addPictureAsIconToLabel();
        return customPictureLabel;
    }

    private CustomPictureWrapperPanel generateCustomPictureWrapperPanel() {

        CustomPictureWrapperPanel customPictureWrapperPanel = new CustomPictureWrapperPanel();
        customPictureWrapperPanel.setOpaque(false);
        customPictureWrapperPanel.setPictureWrapperPanelLayoutManager();
        return customPictureWrapperPanel;
    }
}