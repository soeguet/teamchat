package com.soeguet.gui.comments.generic_comment.dto;

import com.soeguet.gui.comments.generic_comment.gui_elements.panels.CommentSidePanel;
import com.soeguet.gui.comments.generic_comment.gui_elements.panels.CustomContentContainer;
import com.soeguet.gui.comments.generic_comment.gui_elements.panels.TransparentTopPanel;
import com.soeguet.gui.comments.util.CommentTypeEnum;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.model.jackson.BaseModel;
import javax.swing.*;

public record CommentGuiDTO(
        MainFrameGuiInterface mainFrame,
        CommentTypeEnum commentType,
        BaseModel baseModel,
        CommentSidePanel sidePanel,
        TransparentTopPanel topContainer,
        CustomContentContainer customContentContainer,
        JPanel mainContainer) {}
