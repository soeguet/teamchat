package com.soeguet.gui.comments.reaction_panel.interfaces;

import javax.swing.*;

public interface ReactionPanelInterface {

    void setVisible(boolean b);

    void initializeReactionHandler();

    void startAnimation();

    void stopAnimation();
    float getOpacity();

    void addOpacity(float v);
}