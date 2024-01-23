package com.soeguet.comments.interfaces;

import com.soeguet.enums.CommentTypeEnum;
import com.soeguet.model.jackson.BaseModel;

public interface CommentManager {

  CommentTypeEnum categorizeMessageFromSocket(final BaseModel baseModel);

  void setupMessage(BaseModel baseModel, CommentTypeEnum commentType);
}
