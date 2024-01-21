package com.soeguet.generic_comment.reaction_panel.interfaces;

public interface ReactionPanelInterface {

    void setVisible(boolean b);

    void initializeReactionHandler();

    void startAnimation();

    void stopAnimation();

    float getOpacity();

    void addOpacity(float v);

    void setReactionPanelUp();

    void setBounds(int x, int y, int width, int height);
}