package com.soeguet.gui.comments.generic_comment.factories;

import com.soeguet.gui.comments.generic_comment.gui_elements.panels.CustomReferencePanel;
import com.soeguet.model.jackson.BaseModel;

public class ReferencePanelFactory {

    // variables -- start
    private final BaseModel baseModel;
    // variables -- end

    // constructors -- start
    public ReferencePanelFactory(final BaseModel baseModel) {

        this.baseModel = baseModel;
    }
    // constructors -- end

    public CustomReferencePanel createReferencePanel() {

        CustomReferencePanel customQuotePanel = new CustomReferencePanel(baseModel);
        customQuotePanel.setLayoutManager();
        customQuotePanel.setupNameAndTimeTopPanel();
        customQuotePanel.populateReferencePanel();

        return customQuotePanel;
    }
}