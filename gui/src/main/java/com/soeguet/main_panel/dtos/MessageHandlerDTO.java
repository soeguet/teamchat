package com.soeguet.main_panel.dtos;

import com.soeguet.gui.comments.util.CommentTypeEnum;
import com.soeguet.model.jackson.BaseModel;

public record MessageHandlerDTO(BaseModel baseModel, CommentTypeEnum commentType) {}