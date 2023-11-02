package com.soeguet.gui.comments.generic_comment.factories;

import com.soeguet.gui.comments.generic_comment.gui_elements.CustomInteractionPopupMenu;
import com.soeguet.gui.comments.generic_comment.gui_elements.menu_items.CustomReplyMenuItem;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.model.jackson.BaseModel;

import javax.swing.*;

public class InteractionPopupMenuFactory {

    // variables -- start
    private final MainFrameGuiInterface mainFrame;
    private final BaseModel baseModel;
    // variables -- end

    // constructors -- start
    public InteractionPopupMenuFactory(final MainFrameGuiInterface mainFrame, BaseModel baseModel) {

        this.mainFrame = mainFrame;
        this.baseModel = baseModel;
    }
    // constructors -- end

    public CustomInteractionPopupMenu create() {

        CustomInteractionPopupMenu customInteractionPopupMenu = new CustomInteractionPopupMenu();

        final CustomReplyMenuItem customReplyMenuItem = new CustomReplyMenuItem(mainFrame, baseModel);

        customInteractionPopupMenu.add(customReplyMenuItem);
        customInteractionPopupMenu.addSeparator();
        customInteractionPopupMenu.add(new JMenuItem("Report"));

        return customInteractionPopupMenu;
    }
}