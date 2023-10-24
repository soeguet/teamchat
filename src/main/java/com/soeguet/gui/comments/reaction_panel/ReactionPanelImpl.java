package com.soeguet.gui.comments.reaction_panel;

import com.soeguet.gui.comments.reaction_panel.interfaces.ReactionHandlerInterface;
import com.soeguet.gui.comments.reaction_panel.interfaces.ReactionPanelInterface;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;

import javax.swing.*;
import java.awt.*;

public class ReactionPanelImpl extends JPanel implements ReactionPanelInterface {

    private final MainFrameGuiInterface mainFrame;
    private final JLayeredPane parentPane;
    private ReactionHandlerInterface reactionHandler;
    private float opacity = 0.0f;
    private Timer animationTimer;

    public ReactionPanelImpl(final MainFrameGuiInterface mainFrame, JLayeredPane parentPane) {

        this.mainFrame = mainFrame;
        this.parentPane = parentPane;

        setBackground(Color.RED);
        setBounds(0, 0, 100, 100);
        parentPane.add(this, JLayeredPane.POPUP_LAYER);
    }

    public float getOpacity() {

        return opacity;
    }

    public void setOpacity(final float opacity) {

        this.opacity = opacity;
    }

    @Override
    public void addOpacity(final float opacityIncrease) {

        this.opacity += opacityIncrease;
    }

    @Override
    public void initializeReactionHandler() {

        reactionHandler = new ReactionHandlerImpl(mainFrame,this);
    }

    public void startAnimation() {

        animationTimer = reactionHandler.increaseOpacity();
        setOpaque(true);
    }

    @Override
    public void stopAnimation() {

        if (animationTimer.isRunning()) {

                animationTimer.stop();
        }
//
        animationTimer = null;
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        super.paintComponent(g);
    }
}