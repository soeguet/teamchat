package com.soeguet.gui.comments.generic_comment.factories;

import com.soeguet.gui.comments.generic_comment.gui_elements.panels.CustomLinkPanel;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.model.jackson.BaseModel;

public class LinkPanelFactory {

    private final MainFrameGuiInterface mainFrame;
    // variables -- start
    private final BaseModel baseModel;

    // variables -- end

    // constructors -- start
    public LinkPanelFactory(MainFrameGuiInterface mainFrame, BaseModel baseModel) {

        this.mainFrame = mainFrame;

        this.baseModel = baseModel;
    }

    // constructors -- end

    public CustomLinkPanel create() {

        CustomLinkPanel customLinkPane = new CustomLinkPanel(mainFrame, baseModel);
        customLinkPane.setLayoutManager();
        customLinkPane.addQuoteToLinkPanel();
        customLinkPane.addLinkToPanel();

        return customLinkPane;
    }
}
