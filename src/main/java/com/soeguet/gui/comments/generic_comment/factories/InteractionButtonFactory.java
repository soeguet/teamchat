package com.soeguet.gui.comments.generic_comment.factories;

import com.soeguet.gui.comments.generic_comment.gui_elements.buttons.CustomInteractionButton;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.model.jackson.BaseModel;
import javax.swing.*;

public class InteractionButtonFactory extends JButton {

    // variables -- start
    private final MainFrameGuiInterface mainFrame;
    private final BaseModel baseModel;

    // variables -- end

    // constructors -- start
    public InteractionButtonFactory(
            final MainFrameGuiInterface mainFrame, final BaseModel baseModel) {

        this.mainFrame = mainFrame;
        this.baseModel = baseModel;
    }

    // constructors -- end

    public CustomInteractionButton create() {

        CustomInteractionButton customInteractionButton =
                new CustomInteractionButton(mainFrame, baseModel);
        customInteractionButton.setText("...");

        return customInteractionButton;
    }
}
