package com.soeguet.generic_comment.factories;

import com.soeguet.generic_comment.gui_elements.buttons.CustomInteractionButton;
import com.soeguet.model.jackson.BaseModel;
import javax.swing.*;

public class InteractionButtonFactory extends JButton {

  // variables -- start
  private final BaseModel baseModel;

  // variables -- end

  // constructors -- start
  public InteractionButtonFactory(final BaseModel baseModel) {
    this.baseModel = baseModel;
  }

  // constructors -- end

  public CustomInteractionButton create() {

    CustomInteractionButton customInteractionButton = new CustomInteractionButton(baseModel);
    customInteractionButton.setText("...");

    return customInteractionButton;
  }
}
