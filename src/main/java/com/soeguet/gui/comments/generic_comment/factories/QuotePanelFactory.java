package com.soeguet.gui.comments.generic_comment.factories;

import com.soeguet.gui.comments.generic_comment.gui_elements.panels.CustomQuotePanel;
import com.soeguet.model.jackson.BaseModel;

public class QuotePanelFactory {

    // variables -- start
    private final BaseModel baseModel;
    // variables -- end

    // constructors -- start
    public QuotePanelFactory(final BaseModel baseModel) {

        this.baseModel = baseModel;
    }
    // constructors -- end

    public CustomQuotePanel createReplyQuotePanel() {

        CustomQuotePanel customQuotePanel = new CustomQuotePanel(baseModel);
        customQuotePanel.setLayoutManager();
        customQuotePanel.createReplyQuoteTextPane();

        return customQuotePanel;
    }
}