package com.soeguet.gui.comments.generic_comment.gui_elements.panels;

import com.soeguet.gui.comments.util.LinkWrapEditorKit;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.MessageModel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class CustomLinkPanel extends JPanel {

    // variables -- start
    private final BaseModel baseModel;
    // variables -- end

    // constructors -- start
    public CustomLinkPanel(final BaseModel baseModel) {

        this.baseModel = baseModel;
    }
    // constructors -- end

    public void setLayoutManager() {

        super.setLayout(new MigLayout("",
                                      //columns
                                      "[]",
                                      //rows
                                      "[][][]"));
    }

    public void addLinkToPanel() {

        if (baseModel instanceof MessageModel messageModel) {
            JTextPane jTextPane = new JTextPane();
            jTextPane.setEditorKit(new LinkWrapEditorKit());
            jTextPane.setContentType("text/html");
            System.out.println("messageModel.getMessage() = " + messageModel.getMessage());
            jTextPane.setText(messageModel.getMessage());
        }
    }
}