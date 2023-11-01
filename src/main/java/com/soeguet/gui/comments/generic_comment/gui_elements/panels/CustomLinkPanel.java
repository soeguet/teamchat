package com.soeguet.gui.comments.generic_comment.gui_elements.panels;

import com.soeguet.gui.comments.generic_comment.gui_elements.textpane.CustomLinkCommentTextPane;
import com.soeguet.gui.comments.generic_comment.gui_elements.textpane.CustomLinkTextPane;
import com.soeguet.gui.comments.util.WrapEditorKit;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.MessageModel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class CustomLinkPanel extends JPanel {

    // variables -- start
    private final MessageModel messageModel;
    // variables -- end

    // constructors -- start
    public CustomLinkPanel(final BaseModel messageModel) {

        this.messageModel = (MessageModel) messageModel;
        super.setOpaque(false);
    }
    // constructors -- end

    /**
     Sets the layout manager of the current instance.
     */
    public void setLayoutManager() {

        super.setLayout(new MigLayout("debug",
                                      //columns
                                      "[fill,grow]",
                                      //rows
                                      "[fill,grow][fill,grow][fill,grow]"));
    }

    public void addLinkToPanel() {

        CustomLinkTextPane customLinkTextPane = new CustomLinkTextPane(messageModel);
        customLinkTextPane.create();
        this.add(customLinkTextPane, "cell 0 1, growy");
    }

    public void addLinkComment() {

        CustomLinkCommentTextPane customTextPane = new CustomLinkCommentTextPane(true, messageModel);
        customTextPane.create();

        if (!customTextPane.getText().isEmpty()) {
            this.add(customTextPane, "cell 0 2, growy");
        }
    }

    public void addQuoteToLinkPanel() {

        CustomQuotePanel customQuotePanel = new CustomQuotePanel(messageModel);

        customQuotePanel.setLayoutManager();
        customQuotePanel.createQuotedTextPane();

        this.add(customQuotePanel, "cell 0 0, growy");
    }
}