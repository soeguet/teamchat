package com.soeguet.gui.comments.generic_comment.gui_elements.panels;

import com.soeguet.gui.comments.generic_comment.gui_elements.textpanes.CustomLinkCommentTextPane;
import com.soeguet.gui.comments.generic_comment.gui_elements.textpanes.CustomLinkTextPane;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.LinkModel;
import com.soeguet.model.jackson.QuoteModel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class CustomLinkPanel extends JPanel {

    private final MainFrameGuiInterface mainFrame;
    // variables -- start
    private final LinkModel linkModel;
    // variables -- end

    // constructors -- start
    public CustomLinkPanel(MainFrameGuiInterface mainFrame, final BaseModel baseModel) {

        this.mainFrame = mainFrame;

        this.linkModel = (LinkModel) baseModel;
        super.setOpaque(false);
    }
    // constructors -- end

    /**
     Sets the layout manager of the current instance.
     */
    public void setLayoutManager() {

        super.setLayout(new MigLayout("",
                                      //columns
                                      "[fill,grow]",
                                      //rows
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

        CustomReferencePanel customQuotePanel = new CustomReferencePanel(mainFrame,linkModel);

        customQuotePanel.setLayoutManager();
        customQuotePanel.createQuotedTextPane();

        this.add(customQuotePanel, "cell 0 0, growy");
    }
}