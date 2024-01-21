package com.soeguet.generic_comment.gui_elements.panels;

import com.soeguet.enums.CommentTypeEnum;
import com.soeguet.generic_comment.gui_elements.buttons.CustomInteractionButton;
import com.soeguet.generic_comment.util.Side;
import com.soeguet.generic_comment.util.SideHandler;
import java.awt.*;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public class CommentSidePanel extends JPanel {

    // variables -- start
    private final Side side;
    private JLabel nameLabel;
    private JLabel timeLabel;
    private CustomInteractionButton interactionButton;

    // variables -- end

    // constructors -- start
    public CommentSidePanel(final CommentTypeEnum commentType) {

        side = new SideHandler().determineSide(commentType);
        nameLabel = new JLabel();
        timeLabel = new JLabel();

        this.componentConfig();
    }

    // constructors -- end

    public void setSidePanelLayoutManager() {

        /*

        SCHEMA: SIDE PANEL

        left
        [[name][name]]
        [button][time]

        right
        [[name][name]]
        [time][button]

        ___
        [ ">>"SIDEPANEL"<<" [MAIN CONTENT PANEL] ]

         */

        if (side == Side.LEFT) {
            super.setLayout(
                    new MigLayout(
                            "insets 0",
                            // columns
                            "[][]10",
                            // rows - push is needed -> text will float
                            "push[shrink][shrink]"));
        } else {
            super.setLayout(
                    new MigLayout(
                            "insets 0",
                            // columns
                            "10[][]",
                            // rows - push is needed -> text will float
                            "push[shrink][shrink]"));
        }
    }

    private void componentConfig() {

        // timestamp
        final JLabel timeStampLabel = this.getTimeLabel();
        final Font timeStampLabelFont = timeStampLabel.getFont();
        timeStampLabel.setEnabled(false);
        timeStampLabel.setFont(
                new Font(timeStampLabelFont.getName(), Font.ITALIC, timeStampLabelFont.getSize()));

        // name
        final JLabel nameLabel = this.getNameLabel();
        final Font nameLabelFont = nameLabel.getFont();
        nameLabel.setEnabled(false);
        nameLabel.setFont(new Font(nameLabelFont.getName(), Font.BOLD, nameLabelFont.getSize()));
    }

    public void addComponents() {

        switch (this.getSide()) {
            case LEFT -> {
                add(this.getNameLabel(), "cell 0 0 2 1");
                add(this.getTimeLabel(), "cell 1 1 1 1");
                add(this.getInteractionButton(), "cell 0 1 1 1");
            }
            case RIGHT -> {
                this.getNameLabel().setHorizontalAlignment(SwingConstants.TRAILING);
                this.getNameLabel().setHorizontalTextPosition(SwingConstants.TRAILING);
                this.getTimeLabel().setHorizontalAlignment(SwingConstants.TRAILING);
                this.getTimeLabel().setHorizontalTextPosition(SwingConstants.TRAILING);
                add(this.getNameLabel(), "cell 0 0 2 1");
                add(this.getTimeLabel(), "cell 0 1 1 1");
                add(this.getInteractionButton(), "cell 1 1 1 1");
            }
        }
    }

    // getter & setter -- start

    public CustomInteractionButton getInteractionButton() {

        return interactionButton;
    }

    public void setInteractionButton(final CustomInteractionButton interactionButton) {

        this.interactionButton = interactionButton;
    }

    public JLabel getNameLabel() {

        return nameLabel;
    }

    public void setNameLabel(final JLabel nameLabel) {

        this.nameLabel = nameLabel;
    }

    public Side getSide() {

        return side;
    }

    public JLabel getTimeLabel() {

        return timeLabel;
    }

    public void setTimeLabel(final JLabel timeLabel) {

        this.timeLabel = timeLabel;
    }
    // getter & setter -- end
}