package com.pre.gui.reply;

import com.pre.model.MessageModel;

public class CustomReplyDialog extends CustomReply {

    private final MessageModel messageModel;

    public CustomReplyDialog(MessageModel messageModel) {

        super(null);

        this.messageModel = messageModel;
    }
}
