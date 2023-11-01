package com.soeguet.gui.comments.generic_comment.gui_elements.textpane;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.gui.option_pane.links.dtos.LinkTransferDTO;
import com.soeguet.model.jackson.MessageModel;

public class CustomLinkCommentTextPane extends CustomTextPane {

    // variables -- start
    private final MessageModel messageModel;
    private LinkTransferDTO linkCommentRecord;
    // variables -- end

    // constructors -- start
    public CustomLinkCommentTextPane(final boolean lineWrap, final MessageModel messageModel) {

        super(lineWrap, messageModel);
        this.messageModel = messageModel;
    }
    // constructors -- end

    private LinkTransferDTO extractLinkFromMessageModel(final MessageModel messageModel) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(messageModel.getMessage(), LinkTransferDTO.class);

        } catch (JsonProcessingException e) {

            throw new RuntimeException(e);
        }
    }

    public boolean setUp() {

        linkCommentRecord = extractLinkFromMessageModel(messageModel);

        return linkCommentRecord.comment().isEmpty();
    }

    // overrides -- start
    @Override
    public void create() {

        super.setText(linkCommentRecord.comment());
    }
    // overrides -- end
}