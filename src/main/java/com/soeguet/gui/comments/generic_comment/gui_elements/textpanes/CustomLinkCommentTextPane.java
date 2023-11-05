package com.soeguet.gui.comments.generic_comment.gui_elements.textpanes;

import com.soeguet.gui.comments.util.LinkWrapEditorKit;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.model.jackson.LinkModel;

import java.util.logging.Logger;

public class CustomLinkCommentTextPane extends CustomTextPane {

    // variables -- start
    private final LinkModel linkModel;
    // variables -- end

    // constructors -- start
    public CustomLinkCommentTextPane(MainFrameGuiInterface mainFrame, final boolean lineWrap,
                                     final LinkModel linkModel) {

        // FIXME: 02.11.23 -> this is a hack
        super(mainFrame, lineWrap, linkModel.getComment());
        super.setContentType("text/html");
        super.setEditorKit(new LinkWrapEditorKit());
        this.linkModel = linkModel;
    }
    // constructors -- end

    public boolean setUp() {

        return linkModel.getComment().isEmpty();
    }

    @Override
    public void create() {

        super.setText(linkModel.getComment());
    }
}