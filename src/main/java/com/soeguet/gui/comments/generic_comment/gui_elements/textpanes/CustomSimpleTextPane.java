package com.soeguet.gui.comments.generic_comment.gui_elements.textpanes;

import com.soeguet.gui.comments.util.WrapEditorKit;

import javax.swing.*;

public class CustomSimpleTextPane extends JTextPane {

    // constructors -- start
    public CustomSimpleTextPane() {

        super.setEditorKit(new WrapEditorKit());
        super.setOpaque(false);
        super.setBackground(null);
        super.setOpaque(false);
    }
    // constructors -- end
}