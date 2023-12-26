package com.soeguet.gui.comments.interfaces;

import com.soeguet.gui.comments.util.CommentTypeEnum;
import com.soeguet.model.jackson.BaseModel;

public interface CommentManager {

    CommentTypeEnum categorizeMessageFromSocket(final BaseModel baseModel);

    void setupMessage(BaseModel baseModel, CommentTypeEnum commentType);
}
