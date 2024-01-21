package com.soeguet.generic_comment.factories;

import com.soeguet.generic_comment.gui_elements.panels.CustomLinkPanel;
import com.soeguet.model.jackson.BaseModel;

public class LinkPanelFactory {

    // variables -- start
    private final BaseModel baseModel;

    // variables -- end

    // constructors -- start
    public LinkPanelFactory(BaseModel baseModel) {


        this.baseModel = baseModel;
    }

    // constructors -- end

    public CustomLinkPanel create() {

        CustomLinkPanel customLinkPane = new CustomLinkPanel(baseModel);
        customLinkPane.setLayoutManager();
        customLinkPane.addQuoteToLinkPanel();
        customLinkPane.addLinkToPanel();

        return customLinkPane;
    }
}