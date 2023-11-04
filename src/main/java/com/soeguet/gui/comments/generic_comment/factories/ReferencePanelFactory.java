package com.soeguet.gui.comments.generic_comment.factories;

import com.soeguet.gui.comments.generic_comment.gui_elements.panels.CustomReferencePanel;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.model.jackson.BaseModel;

public class ReferencePanelFactory {

    // variables -- start
    private final MainFrameGuiInterface mainFrame;
    private final BaseModel baseModel;
    // variables -- end

    // constructors -- start
    public ReferencePanelFactory(MainFrameGuiInterface mainFrame, final BaseModel baseModel) {

        this.mainFrame = mainFrame;
        this.baseModel = baseModel;
    }
    // constructors -- end

    public CustomReferencePanel createReferencePanel() {

        CustomReferencePanel customReferencePanel = new CustomReferencePanel(mainFrame, baseModel);
        customReferencePanel.setLayoutManager();
        customReferencePanel.setupNameAndTimeTopPanel();
        customReferencePanel.populateReferencePanel();

        return customReferencePanel;
    }
}