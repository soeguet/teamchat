package com.soeguet.generic_comment.gui_elements.textpanes;

import com.soeguet.model.jackson.LinkModel;
import com.soeguet.util.LinkWrapEditorKit;

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

  @Override
  public void create() {

    super.setText(linkModel.getComment());
  }
}
