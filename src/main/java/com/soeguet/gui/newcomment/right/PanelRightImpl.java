package com.soeguet.gui.newcomment.right;

import java.awt.Dimension;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JTextPane;

import com.pre.model.MessageModel;
import com.soeguet.gui.main_frame.MainGuiElementsInterface;

public class PanelRightImpl extends PanelRight {

    private final MainGuiElementsInterface mainFrame;

    private MessageModel messageModel;

    public PanelRightImpl(JFrame mainFrame, MessageModel messageModel) {

        this.mainFrame = (MainGuiElementsInterface) mainFrame;
        this.messageModel = messageModel;

        populateChatBubble();
    }

    private void populateChatBubble() {

        checkForQuotes(messageModel);
        addActualMessage(messageModel);
    }

    private void addActualMessage(MessageModel messageModel) {

        JTextPane actualTextPane = createTextPane();

        actualTextPane.setText(messageModel.getMessage());

        this.getPanel1().add(actualTextPane, "cell 0 1, wrap");
    }

    private void checkForQuotes(MessageModel messageModel) {

        if (messageModel.getQuotedMessageText() == null) {
            return;
        }

        String quotedText = messageModel.getQuotedMessageText();
        String quotedChatParticipant = messageModel.getQuotedMessageSender();
        String quotedTime = messageModel.getQuotedMessageTime();

        createQuotedSectionInChatBubble(quotedText, quotedChatParticipant, quotedTime);
    }

    private void createQuotedSectionInChatBubble(String quotedText, String quotedChatParticipant, String quotedTime) {

        JTextPane quotedTextPane = createTextPane();
        quotedTextPane.setText(quotedText);

        this.getPanel1().add(quotedTextPane, "cell 0 0, wrap");
    }

    private JTextPane createTextPane() {

        JTextPane jTextPane = new JTextPane();

        jTextPane.setEditable(false);
        jTextPane.setOpaque(false);
        jTextPane.setMinimumSize(new Dimension(5, 5));

        return jTextPane;
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
