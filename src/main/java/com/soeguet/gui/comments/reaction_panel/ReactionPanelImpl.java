package com.soeguet.gui.comments.reaction_panel;

import com.soeguet.gui.comments.reaction_panel.interfaces.ReactionPanelInterface;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;

import javax.swing.*;
import java.awt.*;

public class ReactionPanelImpl extends JPanel implements ReactionPanelInterface {

    private final MainFrameGuiInterface mainFrame;
    private final JLayeredPane parentPane;

    public ReactionPanelImpl(final MainFrameGuiInterface mainFrame, JLayeredPane parentPane) {

        this.mainFrame = mainFrame;
        this.parentPane = parentPane;
    }

    public JPanel createReactionPanel() {

        setBackground(Color.RED);
        setBounds((parentPane.getWidth()-100)/2, (parentPane.getHeight()-100)/2, 100, 100);

        return this;
    }
}