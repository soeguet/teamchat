package com.soeguet.gui.comments.generic_comment.gui_elements.textpanes;

import com.soeguet.gui.comments.generic_comment.gui_elements.menu_items.CopyTextMenuItem;
import com.soeguet.gui.comments.util.EmojiSwingWorker;
import com.soeguet.gui.comments.util.WrapEditorKit;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.StyledDocument;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CustomSimpleTextPane extends JTextPane implements MouseListener {

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

        super.addMouseListener(this);
    }
    // constructors -- end

    public void replaceEmojiDescriptionWithActualImageIcon(final String message) {

        new EmojiSwingWorker(this, mainFrame.getEmojiList(), message).execute();
    }

    @Override
    public void mouseClicked(final MouseEvent e) {

        if (SwingUtilities.isRightMouseButton(e)) {

            JPopupMenu popupMenu = new JPopupMenu();
            CopyTextMenuItem copy = new CopyTextMenuItem(this,"Copy");
            popupMenu.add(copy);
            popupMenu.show(this, e.getX(), e.getY());
        }
    }

    @Override
    public void mousePressed(final MouseEvent e) {

    }

    @Override
    public void mouseReleased(final MouseEvent e) {

    }

    @Override
    public void mouseEntered(final MouseEvent e) {

    }

    @Override
    public void mouseExited(final MouseEvent e) {

    }
}