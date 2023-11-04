package com.soeguet.gui.comments.generic_comment.factories;

import com.soeguet.gui.comments.generic_comment.gui_elements.panels.CustomReplyPanel;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.model.jackson.BaseModel;

import javax.swing.*;

public class ReplyPanelFactory {

    private final MainFrameGuiInterface mainFrame;
    private final BaseModel baseModel;
    private CustomReplyPanel customReplyPanel;

    public ReplyPanelFactory(MainFrameGuiInterface mainFrame, BaseModel baseModel) {

        this.mainFrame = mainFrame;
        this.baseModel = baseModel;
    }

    public void create() {

        customReplyPanel = new CustomReplyPanel(mainFrame, baseModel);
        customReplyPanel.setCustomReplyPanelLayoutManger();
        customReplyPanel.populateCustomReplyPanel();
        customReplyPanel.setMaximumSizeWithingMainFrame();

        mainFrame.getMainTextPanelLayeredPane().add(customReplyPanel, JLayeredPane.MODAL_LAYER);

        customReplyPanel.moveReplyPanelToCenter();
        customReplyPanel.setVisible(true);
    }
    public void setFocusOnTextPane(){

        customReplyPanel.setFocusOnTextPane();
    }
}