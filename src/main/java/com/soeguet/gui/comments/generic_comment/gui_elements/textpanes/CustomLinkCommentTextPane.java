package com.soeguet.gui.comments.generic_comment.gui_elements.textpanes;

import com.soeguet.gui.comments.util.LinkWrapEditorKit;
import com.soeguet.model.jackson.LinkModel;

public class CustomLinkCommentTextPane extends CustomTextPane {

    // variables -- start
    private final LinkModel linkModel;
    // variables -- end

    // constructors -- start
    public CustomLinkCommentTextPane(final boolean lineWrap, final LinkModel linkModel) {

        // FIXME: 02.11.23 -> this is a hack
        super(lineWrap, linkModel.getComment());
        super.setContentType("text/html");
        super.setEditorKit(new LinkWrapEditorKit());
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