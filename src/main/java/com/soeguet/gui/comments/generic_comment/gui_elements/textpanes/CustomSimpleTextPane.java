package com.soeguet.gui.comments.generic_comment.gui_elements.textpanes;

import com.soeguet.emoji.EmojiHandler;
import com.soeguet.gui.comments.util.WrapEditorKit;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CustomSimpleTextPane extends JTextPane implements DocumentListener {

    private final MainFrameGuiInterface mainFrame;
    private final EmojiHandler emojiHandler;

    // constructors -- start
    public CustomSimpleTextPane(MainFrameGuiInterface mainFrame) {

        this.mainFrame = mainFrame;
        emojiHandler = new EmojiHandler(mainFrame);

        super.setEditorKit(new WrapEditorKit());
        super.setOpaque(false);
        super.setBackground(null);
        super.setOpaque(false);
        super.setFocusable(true);
        super.setRequestFocusEnabled(true);
        super.requestFocus();
        super.requestFocusInWindow();
    }
    // constructors -- end

    public void convertTextToEmojis() {

        EmojiHandler emojiHandler = new EmojiHandler(mainFrame);
        emojiHandler.replaceImageIconWithEmojiDescription(this);
    }

    @Override
    public void insertUpdate(final DocumentEvent e) {

        emojiHandler.replaceEmojiDescriptionWithActualImageIcon(this, this.getText());
    }

    @Override
    public void removeUpdate(final DocumentEvent e) {
        emojiHandler.replaceEmojiDescriptionWithActualImageIcon(this, this.getText());

    }

    @Override
    public void changedUpdate(final DocumentEvent e) {

    }
}