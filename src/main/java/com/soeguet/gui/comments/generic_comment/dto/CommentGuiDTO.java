package com.soeguet.gui.comments.generic_comment.dto;

import com.soeguet.gui.comments.generic_comment.gui_elements.CommentSidePanel;
import com.soeguet.gui.comments.util.CommentTypeEnum;

import javax.swing.*;

public record CommentGuiDTO(CommentTypeEnum commentType, CommentSidePanel sidePanel, JLayeredPane layeredContainer, JPanel container) {

}