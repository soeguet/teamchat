package com.soeguet.generic_comment.gui_elements.textpanes;

import com.soeguet.emoji.EmojiRegister;
import com.soeguet.generic_comment.gui_elements.menu_items.CopyTextMenuItem;
import com.soeguet.util.EmojiSwingWorker;
import com.soeguet.util.WrapEditorKit;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

public class CustomSimpleTextPane extends JTextPane implements MouseListener {

    // variables -- start

    // variables -- end

    // constructors -- start
    public CustomSimpleTextPane() {


        super.setEditorKit(new WrapEditorKit());
        super.setOpaque(false);
        super.setBackground(null);
        super.setOpaque(false);
        super.setFocusable(true);
        super.setEditable(false);
        super.setRequestFocusEnabled(true);
        super.requestFocus();
        super.requestFocusInWindow();

        super.addMouseListener(this);
    }

    // constructors -- end

    public void replaceEmojiDescriptionWithActualImageIcon(final String message) {

        EmojiRegister emojiRegister = EmojiRegister.getInstance();
        HashMap<String, ImageIcon> emojiHashMap = emojiRegister.getEmojiList();

        new EmojiSwingWorker(this, emojiHashMap, message).execute();
    }

    @Override
    public void mouseClicked(final MouseEvent e) {

        if (SwingUtilities.isRightMouseButton(e)) {

            JPopupMenu popupMenu = new JPopupMenu();
            CopyTextMenuItem copy = new CopyTextMenuItem(this, "Copy");
            popupMenu.add(copy);
            popupMenu.show(this, e.getX(), e.getY());
        }
    }

    @Override
    public void mousePressed(final MouseEvent e) {}

    @Override
    public void mouseReleased(final MouseEvent e) {}

    @Override
    public void mouseEntered(final MouseEvent e) {}

    @Override
    public void mouseExited(final MouseEvent e) {}
}