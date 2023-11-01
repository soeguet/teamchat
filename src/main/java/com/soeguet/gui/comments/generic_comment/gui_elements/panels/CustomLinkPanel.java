package com.soeguet.gui.comments.generic_comment.gui_elements.panels;

import com.soeguet.gui.comments.generic_comment.gui_elements.textpane.CustomLinkCommentTextPane;
import com.soeguet.gui.comments.generic_comment.gui_elements.textpane.CustomLinkTextPane;
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

        super.setLayout(new MigLayout("",
                                      //columns
                                      "[]",
                                      //rows
                                      "[][][]"));
    }

    /**
     Adds a CustomLinkTextPane to the panel at cell (0, 0). The CustomLinkTextPane is created using the given
     messageModel.
     */
    public void addLinkToPanel() {

        CustomLinkTextPane customLinkTextPane = new CustomLinkTextPane(messageModel);
        customLinkTextPane.create();
        this.add(new JLabel("test"), "cell 0 0");
        this.add(customLinkTextPane, "cell 0 1");
    }

    public void addLinkComment() {

        CustomLinkCommentTextPane customTextPane = new CustomLinkCommentTextPane(true, messageModel);
        customTextPane.create();

        if (!customTextPane.getText().isEmpty()) {
            this.add(customTextPane, "cell 0 2");
        }
    }

    public void addLinkQuote() {

        CustomLinkCommentTextPane customTextPane = new CustomLinkCommentTextPane(true, messageModel);
        customTextPane.create();

        if (!customTextPane.getText().isEmpty()) {
            this.add(customTextPane, "cell 0 2");
        }
    }
}