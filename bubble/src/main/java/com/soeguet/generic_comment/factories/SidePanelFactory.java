package com.soeguet.generic_comment.factories;

import com.soeguet.enums.CommentTypeEnum;
import com.soeguet.generic_comment.gui_elements.buttons.CustomInteractionButton;
import com.soeguet.generic_comment.gui_elements.panels.CommentSidePanel;
import com.soeguet.model.jackson.BaseModel;
import javax.swing.*;

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

    // time label
    final JLabel timeLabel = commentSidePanel.getTimeLabel();
    if (baseModel.getTime() == null || baseModel.getTime().isBlank()) {

      // this way the label will take up the space but not be visible -> bubbles will be
      // aligned
      timeLabel.setText("00:00");
      timeLabel.setVisible(false);

    } else {

      timeLabel.setText(baseModel.getTime());
    }

    // interaction menu
    final CustomInteractionButton interactionButton =
        new InteractionButtonFactory(baseModel).create();
    commentSidePanel.setInteractionButton(interactionButton);

    commentSidePanel.addComponents();

    return commentSidePanel;
  }
}
