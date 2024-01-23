package com.soeguet.generic_comment.gui_elements.panels;

import com.soeguet.generic_comment.gui_elements.textpanes.CustomLinkTextPane;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.LinkModel;
import com.soeguet.model.jackson.QuoteModel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

public class CustomLinkPanel extends JPanel {

  // variables -- start
  private final LinkModel linkModel;

  // variables -- end

  // constructors -- start
  public CustomLinkPanel(final BaseModel baseModel) {

    this.linkModel = (LinkModel) baseModel;
    super.setOpaque(false);
  }

  // constructors -- end

  /** Sets the layout manager of the current instance. */
  public void setLayoutManager() {

    super.setLayout(
        new MigLayout(
            "",
            // columns
            "[fill,grow]",
            // rows
            "[fill,grow][fill,grow]"));
  }

  public void addLinkToPanel() {

    CustomLinkTextPane customLinkTextPane = new CustomLinkTextPane(linkModel);
    customLinkTextPane.create();
    this.add(customLinkTextPane, "cell 0 1, growy");
  }

  public void addQuoteToLinkPanel() {

    // FIXME: 02.11.23
    final QuoteModel<? extends BaseModel> quotedMessage = linkModel.getQuotedMessage();

    if (quotedMessage == null) {
      return;
    }

    CustomReferencePanel customQuotePanel = new CustomReferencePanel(linkModel);

    customQuotePanel.setLayoutManager();
    customQuotePanel.createQuotedTextPane();

    this.add(customQuotePanel, "cell 0 0, growy");
  }
}
