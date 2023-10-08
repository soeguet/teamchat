package com.soeguet.gui.comments.interfaces;

import com.soeguet.model.jackson.BaseModel;

public interface CommentManager {

    int categorizeMessageFromSocket(final BaseModel baseModel);

    //RIGHT SIDE
    void setupMessagesRightSide(final BaseModel messageModel, final String nickname);

    void setupLinkRightSite(final BaseModel messageModel);

    void setupPicturesRightSide(final BaseModel messageModel, final String nickname);

    //LEFT SIDE
    void setupMessagesLeftSide(final BaseModel messageModel, final String nickname);

    void setupPicturesLeftSide(final BaseModel messageModel, final String nickname);

    void setupLinkLeftSide(BaseModel baseModel);
}