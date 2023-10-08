package com.soeguet.gui.comments.interfaces;

import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.MessageModel;

public interface CommentManager {

    void setupLinkRightSite(final BaseModel messageModel);

    int categorizeMessageFromSocket(final BaseModel baseModel);

    void setupMessagesRightSide(final BaseModel messageModel, final String nickname);

    void setupPicturesRightSide(final BaseModel messageModel, final String nickname);

    void setupMessagesLeftSide(final BaseModel messageModel, final String nickname);

    void setupPicturesLeftSide(final BaseModel messageModel, final String nickname);

    void setupLinkLeftSide(BaseModel baseModel);
}