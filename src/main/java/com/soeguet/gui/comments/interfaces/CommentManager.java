package com.soeguet.gui.comments.interfaces;

import com.soeguet.gui.comments.util.CommentTypeEnum;
import com.soeguet.gui.main_panel.dtos.MessageHandlerDTO;
import com.soeguet.model.jackson.BaseModel;

public interface CommentManager {

    CommentTypeEnum categorizeMessageFromSocket(final BaseModel baseModel);

    //RIGHT SIDE
    void setupMessagesRightSide(final BaseModel messageModel, final String nickname);

    void setupLinkRightSite(final BaseModel messageModel);

    void setupPicturesRightSide(final BaseModel messageModel, final String nickname);

    //LEFT SIDE
    void setupMessagesLeftSide(final BaseModel messageModel, final String nickname);

    void setupPicturesLeftSide(final BaseModel messageModel, final String nickname);

    void setupLinkLeftSide(BaseModel baseModel);

    void setupMessage(MessageHandlerDTO messageHandlerDTO);
}