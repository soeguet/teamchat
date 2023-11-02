package com.soeguet.gui.comments.generic_comment.gui_elements.textpane;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.gui.option_pane.links.dtos.LinkTransferDTO;
import com.soeguet.model.jackson.LinkModel;
import com.soeguet.model.jackson.MessageModel;

public class CustomLinkCommentTextPane extends CustomTextPane {

    // variables -- start
    private final LinkModel linkModel;
    // variables -- end

    // constructors -- start
    public CustomLinkCommentTextPane(final boolean lineWrap, final LinkModel linkModel) {

        // FIXME: 02.11.23 -> this is a hack
        super(lineWrap, linkModel.getComment());
        this.linkModel = linkModel;
    }
    // constructors -- end


    public boolean setUp() {

        return linkModel.getComment().isEmpty();
    }

    // overrides -- start
    @Override
    public void create() {

        super.setText(linkModel.getComment());
    }
    // overrides -- end
}