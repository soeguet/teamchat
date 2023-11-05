package com.soeguet.gui.comments.generic_comment.gui_elements.textpanes;

import com.soeguet.gui.comments.util.EmojiSwingWorker;
import com.soeguet.gui.comments.util.WrapEditorKit;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;

import javax.swing.*;

public class CustomSimpleTextPane extends JTextPane {

    // variables -- start
    private final MainFrameGuiInterface mainFrame;
    // variables -- end

    // constructors -- start
    public CustomSimpleTextPane(MainFrameGuiInterface mainFrame) {

        this.mainFrame = mainFrame;

        super.setEditorKit(new WrapEditorKit());
        super.setOpaque(false);
        super.setBackground(null);
        super.setOpaque(false);
        super.setFocusable(true);
        super.setEditable(false);
        super.setRequestFocusEnabled(true);
        super.requestFocus();
        super.requestFocusInWindow();
    }
    // constructors -- end

    public void replaceEmojiDescriptionWithActualImageIcon(final String message) {

        new EmojiSwingWorker(this, mainFrame.getEmojiList(), message).execute();
    }
}