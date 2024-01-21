package com.soeguet.generic_comment.dto;

import com.soeguet.enums.CommentTypeEnum;
import com.soeguet.generic_comment.gui_elements.panels.CommentSidePanel;
import com.soeguet.generic_comment.gui_elements.panels.CustomContentContainer;
import com.soeguet.generic_comment.gui_elements.panels.TransparentTopPanel;
import com.soeguet.model.jackson.BaseModel;
import javax.swing.*;

public record CommentGuiDTO(
        CommentTypeEnum commentType,
        BaseModel baseModel,
        CommentSidePanel sidePanel,
        TransparentTopPanel topContainer,
        CustomContentContainer customContentContainer,
        JPanel mainContainer) {}