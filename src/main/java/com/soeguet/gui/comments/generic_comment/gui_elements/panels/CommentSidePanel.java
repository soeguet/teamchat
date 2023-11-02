package com.soeguet.gui.comments.generic_comment.gui_elements.panels;

import com.soeguet.gui.comments.generic_comment.util.Side;
import com.soeguet.gui.comments.generic_comment.util.SideHandler;
import com.soeguet.gui.comments.util.CommentTypeEnum;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class CommentSidePanel extends JPanel {

    // variables -- start
    private final Side side;
    private JLabel nameLabel;
    private JLabel timeLabel;
    private JButton interactionButton;
    // variables -- end
    // constructors -- start
    public CommentSidePanel(final CommentTypeEnum commentType) {

        side = new SideHandler().determineSide(commentType);
        nameLabel = new JLabel();
        timeLabel = new JLabel("10:00");

        this.componentConfig();
    }
    // constructors -- end

    private void componentConfig() {

        //timestamp
        final JLabel timeStampLabel = this.getTimeLabel();
        final Font timeStampLabelFont = timeStampLabel.getFont();
        timeStampLabel.setEnabled(false);
        timeStampLabel.setFont(new Font(timeStampLabelFont.getName(), Font.ITALIC, timeStampLabelFont.getSize()));

        //name
        final JLabel nameLabel = this.getNameLabel();
        final Font nameLabelFont = nameLabel.getFont();
        nameLabel.setEnabled(false);
        nameLabel.setFont(new Font(nameLabelFont.getName(), Font.BOLD, nameLabelFont.getSize()));

        //button
        final JButton interactionButton = this.getInteractionButton();
        //        interactionButton.setBorder(BorderFactory.createEmptyBorder());
        interactionButton.setBackground(null);
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

        setLayout(new MigLayout("",
                                // columns
                                "[][]",
                                // rows
                                "push[][]"));

    }

    // getter & setter -- start
    public JButton getInteractionButton() {

        return interactionButton;
    }

    public void setInteractionButton(final JButton interactionButton) {

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