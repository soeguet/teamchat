package com.soeguet.gui.comments.generic_comment.gui_elements.textpane;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.gui.option_pane.links.dtos.LinkTransferDTO;
import com.soeguet.model.jackson.MessageModel;

public class CustomLinkCommentTextPane extends CustomTextPane {

    // variables -- start
    private final MessageModel messageModel;
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

    // overrides -- start
    @Override
    public void create() {

        LinkTransferDTO linkCommentRecord = extractLinkFromMessageModel(this.messageModel);
        super.setText(linkCommentRecord.comment());
    }
    // overrides -- end
}