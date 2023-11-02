package com.soeguet.gui.comments.generic_comment.gui_elements.labels;

import com.soeguet.model.jackson.MessageModel;

import javax.swing.*;
import java.awt.Font;

public class CustomQuoteTopLabel extends JLabel {

    // variables -- start
    private final MessageModel messageModel;
    // variables -- end

    // constructors -- start
    public CustomQuoteTopLabel(final MessageModel messageModel) {

        super();
        this.messageModel = messageModel;
    }
    // constructors -- end

    public void create() {

        // FIXME: 02.11.23
        super.setText("%s - %s".formatted("messageModel.getQuotedMessageSender()", "messageModel.getQuotedMessageTime()"));
        super.setFont(new Font(super.getFont().getName(), Font.ITALIC, 11));
        super.setEnabled(false);
    }
}