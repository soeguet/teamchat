package com.soeguet.gui.comments.generic_comment.factories;

import com.soeguet.gui.comments.generic_comment.gui_elements.panels.CommentSidePanel;
import com.soeguet.gui.comments.util.CommentTypeEnum;
import com.soeguet.model.jackson.BaseModel;

public class SidePanelFactory {

    // variables -- start
    private final BaseModel baseModel;
    private final CommentTypeEnum commentType;
    // variables -- end

    // constructors -- start
    public SidePanelFactory(BaseModel baseModel, CommentTypeEnum commentType) {

        this.baseModel = baseModel;
        this.commentType = commentType;
    }
    // constructors -- end

    public CommentSidePanel create() {

        CommentSidePanel commentSidePanel = new CommentSidePanel(commentType);

        commentSidePanel.setSidePanelLayoutManager();
        commentSidePanel.getNameLabel().setText(baseModel.getSender());
        commentSidePanel.getTimeLabel().setText("10:00");
        commentSidePanel.addComponents();

        return commentSidePanel;
    }
}