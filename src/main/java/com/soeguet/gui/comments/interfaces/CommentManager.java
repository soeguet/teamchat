package com.soeguet.gui.comments.interfaces;

import com.soeguet.gui.comments.util.CommentTypeEnum;
import com.soeguet.gui.main_panel.dtos.MessageHandlerDTO;
import com.soeguet.model.jackson.BaseModel;

public interface CommentManager {

    CommentTypeEnum categorizeMessageFromSocket(final BaseModel baseModel);

    void setupMessage(MessageHandlerDTO messageHandlerDTO);
}