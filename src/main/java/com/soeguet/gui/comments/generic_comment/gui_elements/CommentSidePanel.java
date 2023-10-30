package com.soeguet.gui.comments.generic_comment.gui_elements;

import com.soeguet.gui.comments.generic_comment.util.Side;
import com.soeguet.gui.comments.generic_comment.util.SideHandler;
import com.soeguet.gui.comments.util.CommentTypeEnum;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class CommentSidePanel extends JPanel {

    private final Side side;
    private JLabel nameLabel;
    private JLabel timeLabel;
    private JButton interactionButton;

    public CommentSidePanel(final CommentTypeEnum commentType) {

        side = new SideHandler().determineSide(commentType);
        nameLabel = new JLabel();
        timeLabel = new JLabel();
        interactionButton = new JButton("...");
    }


    public void setLayoutManager() {

        switch (side) {
            case LEFT -> setLayout(new MigLayout("insets 0, gap 0 0",
                                                 // columns
                                                 "[fill]" + "[grow,fill]",
                                                 // rows
                                                 "[fill]" + "[grow,fill]"));
            case RIGHT -> setLayout(new MigLayout("insets 0, gap 0 0",
                                                  // columns
                                                  "[grow,fill]" + "[fill]",
                                                  // rows
                                                  "[fill]" + "[grow,fill]"));
        }
    }

    public void addComponents() {

        switch (side) {
            case LEFT -> {
                add(this.getNameLabel(), "cell 0 0");
                add(this.getTimeLabel(), "cell 1 0");
                add(this.getInteractionButton(), "cell 0 1");
            }
            case RIGHT -> {
                this.getNameLabel().setHorizontalAlignment(SwingConstants.TRAILING);
                this.getNameLabel().setHorizontalTextPosition(SwingConstants.TRAILING);
                this.getTimeLabel().setHorizontalAlignment(SwingConstants.TRAILING);
                this.getTimeLabel().setHorizontalTextPosition(SwingConstants.TRAILING);
                add(this.getNameLabel(), "cell 1 0");
                add(this.getTimeLabel(), "cell 0 0");
                add(this.getInteractionButton(), "cell 1 1");
            }
        }
    }

    public JLabel getNameLabel() {

        return nameLabel;
    }

    public void setNameLabel(final JLabel nameLabel) {

        this.nameLabel = nameLabel;
    }

    public JLabel getTimeLabel() {

        return timeLabel;
    }

    public void setTimeLabel(final JLabel timeLabel) {

        this.timeLabel = timeLabel;
    }

    public JButton getInteractionButton() {

        return interactionButton;
    }

    public void setInteractionButton(final JButton interactionButton) {

        this.interactionButton = interactionButton;
    }
}