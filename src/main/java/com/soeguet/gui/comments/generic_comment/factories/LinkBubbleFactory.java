package com.soeguet.gui.comments.generic_comment.factories;

import com.soeguet.gui.comments.generic_comment.gui_elements.panels.CustomLinkPanel;
import com.soeguet.model.jackson.BaseModel;

public class LinkBubbleFactory {

    // variables -- start
    private final BaseModel baseModel;
    // variables -- end

    // constructors -- start
    public LinkBubbleFactory(BaseModel baseModel) {

        this.baseModel = baseModel;
    }
    // constructors -- end

    public CustomLinkPanel create() {

        CustomLinkPanel customLinkPane = new CustomLinkPanel(baseModel);
        customLinkPane.setLayoutManager();
        customLinkPane.addLinkToPanel();
        customLinkPane.addLinkComment();

        return customLinkPane;
    }
}