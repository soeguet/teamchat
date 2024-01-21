package com.soeguet.generic_comment.factories;

import com.soeguet.generic_comment.gui_elements.panels.CustomReplyPanel;
import com.soeguet.model.jackson.BaseModel;

public class ReplyPanelFactory {

    // variables -- start
    private final BaseModel baseModel;
    private CustomReplyPanel customReplyPanel;

    // variables -- end

    // constructors -- start
    public ReplyPanelFactory(BaseModel baseModel) {

        this.baseModel = baseModel;
    }

    // constructors -- end

    public void create() {

        customReplyPanel = new CustomReplyPanel(baseModel);

        customReplyPanel.setCustomReplyPanelLayoutManger();
        customReplyPanel.populateCustomReplyPanel();
        customReplyPanel.setMaximumSizeWithingMainFrame();

        // TOOO 1
//        ChatMainFrameImpl.getMainFrameInstance().getMainTextPanelLayeredPane().add(customReplyPanel,
//                                                                                   JLayeredPane.MODAL_LAYER);

        customReplyPanel.moveReplyPanelToCenter();
        customReplyPanel.setVisible(true);
    }

    public void setFocusOnTextPane() {

        customReplyPanel.setFocusOnTextPane();
    }
}