package com.soeguet.gui.comments.interfaces;

import com.soeguet.model.UserInteraction;
import com.soeguet.model.jackson.BaseModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public interface CommentInterface {

    JLabel getNameLabel();

    void setupTextPanelWrapper();

    void setupPicturePanelWrapper();

    void setBorderColor(Color borderColor);

    void initializeBorderHandler(final Color borderColor);

    void initializeReactionStickerHandler(List<UserInteraction> userInteractions);

    BaseModel getBaseModel();
    void setBaseModel(BaseModel baseModel);
}