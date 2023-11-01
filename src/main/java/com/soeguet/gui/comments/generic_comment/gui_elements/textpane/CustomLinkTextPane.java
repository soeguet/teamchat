package com.soeguet.gui.comments.generic_comment.gui_elements.textpane;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.gui.comments.util.LinkWrapEditorKit;
import com.soeguet.gui.comments.util.WrapEditorKit;
import com.soeguet.gui.option_pane.links.dtos.LinkTransferDTO;
import com.soeguet.model.jackson.MessageModel;

import javax.swing.*;

public class CustomLinkTextPane extends JTextPane {

    // variables -- start
    private final MessageModel messageModel;
    // variables -- end

    // constructors -- start
    public CustomLinkTextPane(MessageModel messageModel) {

        this.messageModel = messageModel;

        super.setEditorKit(new LinkWrapEditorKit());
    }
    // constructors -- end

    public void create() {

        LinkTransferDTO linkCommentRecord = extractLinkFromMessageModel(messageModel);
        final String hyperlinkHtml =
                ("<a href=\"%s\" style=\"text-decoration:underline; color:blue; font-size:15;\">%s</a>").formatted(linkCommentRecord.link(), linkCommentRecord.link());
        super.setText(hyperlinkHtml);
    }

    private LinkTransferDTO extractLinkFromMessageModel(final MessageModel messageModel) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(messageModel.getMessage(), LinkTransferDTO.class);

        } catch (JsonProcessingException e) {

            throw new RuntimeException(e);
        }
    }
}