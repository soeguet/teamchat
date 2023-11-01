package com.soeguet.gui.comments.generic_comment.gui_elements.util;

import com.soeguet.gui.comments.generic_comment.gui_elements.panels.CustomContentContainer;
import com.soeguet.gui.comments.generic_comment.util.Side;

import java.awt.*;

public class ChatBubblePaintHandler {

    private final CustomContentContainer customContentContainer;
    private final Side side;

    public void setBorderColor(final Color borderColor) {

        this.borderColor = borderColor;
        customContentContainer.repaint();
    }

    private Color borderColor;

    public ChatBubblePaintHandler(final CustomContentContainer customContentContainer, final Side side,
                                  final Color borderColor) {

        this.customContentContainer = customContentContainer;
        this.side = side;
        this.borderColor = borderColor;
    }

    public void setupChatBubble() {

        final CustomContentContainer container = this.getCustomContentContainer();

        switch (this.getSide()) {
            case LEFT -> setupLeftChatBubble(container);
            case RIGHT -> setupRightChatBubble(container);
        }

    }

    public CustomContentContainer getCustomContentContainer() {

        return customContentContainer;
    }

    public Side getSide() {

        return side;
    }

    private void setupLeftChatBubble(final CustomContentContainer container) {

        container.overrideCustomPaint(grphcs -> {

            final int containerWidth = container.getWidth();
            final int containerHeight = container.getHeight();

            int rounding = 20;
            Graphics2D g2d = (Graphics2D) grphcs;

            final Color backgroundColor = Color.WHITE;

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(backgroundColor);
            g2d.fillRoundRect(13, 0, containerWidth - 13 - 1, containerHeight - 1, rounding, rounding);

            g2d.setColor(borderColor);
            g2d.drawRoundRect(13, 0, containerWidth - 13 - 1, containerHeight - 1, rounding, rounding);

            g2d.setColor(backgroundColor);
            g2d.fillPolygon(new int[]{0, 13, 25}, new int[]{containerHeight, containerHeight - 13, containerHeight}, 3);

            g2d.setColor(borderColor);
            g2d.drawLine(0, containerHeight - 1, 25, containerHeight - 1);
            g2d.drawLine(0, containerHeight - 1, 13, containerHeight - 13);
        });
    }

    private void setupRightChatBubble(final CustomContentContainer container) {

        container.overrideCustomPaint(grphcs -> {

            final int containerWidth = container.getWidth();
            final int containerHeight = container.getHeight();

            int rounding = 20;
            Graphics2D g2d = (Graphics2D) grphcs;

            final Color backgroundColor = Color.WHITE;

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(backgroundColor);
            g2d.fillRoundRect(0, 0, containerWidth - 13, containerHeight - 1, rounding, rounding);

            g2d.setColor(getBorderColor());
            g2d.drawRoundRect(0, 0, containerWidth - 13, containerHeight - 1, rounding, rounding);

            g2d.setColor(backgroundColor);
            g2d.fillPolygon(new int[]{containerWidth - 1, containerWidth - 28, containerWidth - 13},
                            new int[]{containerHeight - 1, containerHeight - 1, containerHeight - 13}, 3);

            g2d.setColor(getBorderColor());
            g2d.drawLine(containerWidth - 30, containerHeight - 1, containerWidth, containerHeight - 1);
            g2d.drawLine(containerWidth - 13, containerHeight - 13, containerWidth, containerHeight);
        });
    }

    public Color getBorderColor() {

        return borderColor;
    }
}