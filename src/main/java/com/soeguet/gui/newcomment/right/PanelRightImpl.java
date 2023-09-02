package com.soeguet.gui.newcomment.right;

import com.pre.model.MessageModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class PanelRightImpl extends PanelRight {

    private final JFrame mainFrame;

    private MessageModel messageModel;

    public PanelRightImpl(JFrame mainFrame, MessageModel messageModel) {

        this.mainFrame = mainFrame;
        this.messageModel = messageModel;

        populateChatBubble();
    }

    private void populateChatBubble() {

        JTextPane jTextPane = new JTextPane();
        jTextPane.setMinimumSize(new Dimension(5,1));
        jTextPane.setEditable(false);
        jTextPane.setOpaque(false);
        jTextPane.setText(messageModel.getMessage());
        this.getPanel1().add(jTextPane);
    }

    @Override
    protected void replyButtonClicked(MouseEvent e) {

    }

    @Override
    protected void actionLabelMouseEntered(MouseEvent e) {

    }

    @Override
    protected void actionLabelMouseClicked(MouseEvent e) {

    }

    @Override
    protected void actionLabelMouseExited(MouseEvent e) {

    }
}
