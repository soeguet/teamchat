package com.soeguet.generic_comment.gui_elements.textpanes;

import com.soeguet.model.jackson.MessageModel;
import com.soeguet.util.WrapEditorKit;

import javax.swing.*;
import java.awt.*;

public class CustomReplyPreviewTopInformationTextPane extends JTextPane {

    // variables -- start
    private final MessageModel messageModel;

    // variables -- end

    // constructors -- start
    public CustomReplyPreviewTopInformationTextPane(final MessageModel messageModel) {

        super();
        super.setEditorKit(new WrapEditorKit());
        this.messageModel = messageModel;
    }

    // constructors -- end

    public void createQuoteTopTextPane() {

        // FIXME: 02.11.23
        super.setText(
                "%s - %s"
                        .formatted(
                                "messageModel.getQuotedMessageSender()",
                                "messageModel.getQuotedMessageTime" + "()"));
        super.setFont(new Font(super.getFont().getName(), Font.ITALIC, 11));
        super.setEnabled(false);
    }

    public void createReplyTopTextPane() {

        super.setText("%s - %s".formatted(messageModel.getSender(), messageModel.getTime()));
        super.setFont(new Font(super.getFont().getName(), Font.ITALIC, 11));
        super.setEnabled(false);
    }
}