package com.soeguet.main_panel.dtos;

import com.soeguet.enums.CommentTypeEnum;
import com.soeguet.model.jackson.BaseModel;

public record MessageHandlerDTO(BaseModel baseModel, CommentTypeEnum commentType) {}
