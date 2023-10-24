package com.soeguet.gui.comments.reaction_panel;

import com.soeguet.gui.comments.reaction_panel.interfaces.ReactionHandlerInterface;
import com.soeguet.gui.comments.reaction_panel.interfaces.ReactionPanelInterface;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;

import javax.swing.*;
import java.awt.*;

public class ReactionPanelImpl extends JPanel implements ReactionPanelInterface {

    private final MainFrameGuiInterface mainFrame;
    private final JLayeredPane parentPane;
    private int width = 400;
    private int height = 100;
    private ReactionHandlerInterface reactionHandler;
    private float opacity = 0.0f;
    private Timer animationTimer;

    public ReactionPanelImpl(final MainFrameGuiInterface mainFrame, JLayeredPane parentPane) {

        this.mainFrame = mainFrame;
        this.parentPane = parentPane;
    }

    @Override
    public void initializeReactionHandler() {

        reactionHandler = new ReactionHandlerImpl(mainFrame, this);
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

        animationTimer = null;
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
    public void setReactionPanelUp() {

        setBackground(Color.RED);
        setBounds(parentPane.getWidth() / 2 - width / 2, parentPane.getHeight() / 2 - height / 2, width, height);
    }

    @Override
    public int getWidth() {

        return width;
    }

    public void setWidth(final int width) {

        this.width = width;
    }

    @Override
    public int getHeight() {

        return height;
    }

    public void setHeight(final int height) {

        this.height = height;
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        super.paintComponent(g);
    }
}