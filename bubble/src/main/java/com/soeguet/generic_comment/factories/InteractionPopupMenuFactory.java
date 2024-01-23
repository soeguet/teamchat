package com.soeguet.generic_comment.factories;

import com.soeguet.generic_comment.gui_elements.CustomInteractionPopupMenu;
import com.soeguet.generic_comment.gui_elements.menu_items.CustomReplyMenuItem;
import com.soeguet.model.jackson.BaseModel;
import javax.swing.*;

public class InteractionPopupMenuFactory {

  // variables -- start
  private final BaseModel baseModel;

  // variables -- end

  // constructors -- start
  public InteractionPopupMenuFactory(BaseModel baseModel) {

    this.baseModel = baseModel;
  }

  // constructors -- end

  public CustomInteractionPopupMenu create() {

    CustomInteractionPopupMenu customInteractionPopupMenu = new CustomInteractionPopupMenu();

    final CustomReplyMenuItem customReplyMenuItem = new CustomReplyMenuItem(baseModel);

    customInteractionPopupMenu.add(customReplyMenuItem);
    customInteractionPopupMenu.addSeparator();
    customInteractionPopupMenu.add(new JMenuItem("Report"));

    return customInteractionPopupMenu;
  }
}
