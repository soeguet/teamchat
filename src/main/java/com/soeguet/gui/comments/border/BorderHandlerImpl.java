package com.soeguet.gui.comments.border;

import com.soeguet.gui.comments.border.interfaces.BorderHandlerInterface;
import com.soeguet.gui.comments.interfaces.CommentInterface;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;

import java.awt.*;

public class BorderHandlerImpl implements BorderHandlerInterface {

    private final Color highlightBorderColor = Color.RED;
    private final MainFrameGuiInterface mainFrame;
    private final CommentInterface comment;
    private Color borderColor;

    public BorderHandlerImpl(final MainFrameGuiInterface mainFrame, final CommentInterface comment) {

        this.mainFrame = mainFrame;
        this.comment = comment;
    }

    @Override
    public void saveBorderColor(final Color color) {

        this.borderColor = color;
    }

    @Override
    public void highlightBorder() {

        comment.setBorderColor(highlightBorderColor);
        mainFrame.repaint();
    }

    @Override
    public void revertBorderColor() {

        comment.setBorderColor(borderColor);
        mainFrame.repaint();
    }
}