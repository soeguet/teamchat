package com.soeguet.gui.comments.generic_comment.gui_elements.textpanes;

import com.soeguet.gui.comments.util.LinkWrapEditorKit;
import com.soeguet.model.jackson.LinkModel;

import javax.swing.*;

public class CustomLinkTextPane extends JTextPane {

    // variables -- start
    private final LinkModel linkModel;
    // variables -- end

    // constructors -- start
    public CustomLinkTextPane(LinkModel linkModel) {

        this.linkModel = linkModel;
        super.setOpaque(false);
        super.setEditable(false);
        super.setBackground(null);
        super.setEditorKit(new LinkWrapEditorKit());
    }
    // constructors -- end

    public void create() {

        String hyperlinkHtml;

        if (linkModel.getComment().isEmpty()) {

            hyperlinkHtml = """
                    <a href="%s"
                    style="text-decoration:underline; color:blue; font-size:15;">
                    %s
                    </a>
                    """.formatted(linkModel.getLink(), linkModel.getLink());

        } else {

            // contains additional <p></p> tags
            hyperlinkHtml = """
                    <a href="%s"
                    style="text-decoration:underline; color:blue; font-size:15;">
                    %s
                    </a>
                    <p>%s</p>
                    """.formatted(linkModel.getLink(), linkModel.getLink(), linkModel.getComment());
        }

        super.setText(hyperlinkHtml);
    }
}