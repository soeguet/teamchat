package com.soeguet.gui.comments.reaction_panel;

import com.soeguet.gui.comments.reaction_panel.interfaces.ReactionHandlerInterface;
import com.soeguet.gui.comments.reaction_panel.interfaces.ReactionPanelInterface;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;

import javax.swing.*;

public class ReactionHandlerImpl implements ReactionHandlerInterface {
private final ReactionPanelInterface reactionPanel;
private final MainFrameGuiInterface mainFrame;
    public ReactionHandlerImpl(final MainFrameGuiInterface mainFrame, final ReactionPanelInterface reactionPanel) {

        this.reactionPanel = reactionPanel;
        this.mainFrame = mainFrame;
    }

    @Override
    public Timer increaseOpacity() {

        Timer timer = new Timer(2000, event -> {

            reactionPanel.addOpacity(0.025f);
            mainFrame.repaint();

            System.out.println(reactionPanel.getOpacity());
            if (reactionPanel.getOpacity() >= 0.3f) {

                ((Timer) event.getSource()).stop();
            }
        });
        timer.setDelay(20);
        timer.start();

        reactionPanel.setVisible(true);

        return timer;
    }


}