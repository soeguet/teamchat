package com.soeguet.gui.comments.generic_comment.factories;

import com.soeguet.gui.comments.generic_comment.gui_elements.textpane.CustomPicturePane;
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

    public CustomPicturePane create() {

        CustomPicturePane customPicturePane = new CustomPicturePane(pictureModel.getPicture());

        customPicturePane.setOpaque(true);
        customPicturePane.addPictureAsIconToLabel();

        return customPicturePane;
    }
}