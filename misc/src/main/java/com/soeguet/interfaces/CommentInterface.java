package com.soeguet.interfaces;

import com.soeguet.enums.CommentTypeEnum;
import com.soeguet.model.UserInteraction;
import com.soeguet.model.jackson.BaseModel;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.border.Border;

public interface CommentInterface {

    JLabel getNameLabel();

    void setupTextPanelWrapper();

    void setupPicturePanelWrapper();

    void initializeBorderHandler(final Color borderColor);

    void initializeReactionStickerHandler(List<UserInteraction> userInteractions);

    BaseModel getBaseModel();

    void setBaseModel(BaseModel baseModel);

    void setCustomPaint(final Consumer<Graphics> customPaint);

    int getWidth();

    int getHeight();

    Color getBorderColor();

    void setBorderColor(Color borderColor);

    void setCommentType(final CommentTypeEnum commentType);

    void setBorder(Border border);
}