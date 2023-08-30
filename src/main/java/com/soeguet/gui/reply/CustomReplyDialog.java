package com.soeguet.gui.reply;

import com.soeguet.model.MessageModel;

public class CustomReplyDialog extends CustomReply {

    private final MessageModel messageModel;

    public CustomReplyDialog(MessageModel messageModel) {

        super(null);

        this.messageModel = messageModel;
    }
}
