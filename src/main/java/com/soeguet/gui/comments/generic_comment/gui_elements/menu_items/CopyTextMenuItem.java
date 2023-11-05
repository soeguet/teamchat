package com.soeguet.gui.comments.generic_comment.gui_elements.menu_items;

import com.soeguet.gui.comments.generic_comment.gui_elements.textpanes.CustomSimpleTextPane;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CopyTextMenuItem extends JMenuItem implements MouseListener {

    // variables -- start
    private final CustomSimpleTextPane textPane;
    // variables -- end

    // constructors -- start
    public CopyTextMenuItem(final CustomSimpleTextPane textPane, final String menuItemName) {

        super(menuItemName);

        this.textPane = textPane;

        super.addMouseListener(this);
    }
    // constructors -- end

    private String extractTextForClipboard() {

        if (textPane.getSelectedText() != null && !textPane.getSelectedText().isBlank()) {

            return textPane.getSelectedText();

        } else if (textPane.getText() != null && !textPane.getText().isBlank()) {

            return textPane.getText();

        } else {

            JOptionPane.showMessageDialog(SwingUtilities.getRootPane(textPane), "Nothing to copy!", "No text " +
                                                                                                    "selected",
                                          JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
    }

    @Override
    public void mouseClicked(final MouseEvent e) {

    }

    @Override
    public void mousePressed(final MouseEvent e) {

        String selectedText = extractTextForClipboard();

        if (selectedText != null) {

            final StringSelection stringSelection = new StringSelection(selectedText);
            final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);

            JOptionPane.showMessageDialog(SwingUtilities.getRootPane(textPane),
                                          "\"%s\" %scopied to clipboard".formatted(selectedText,
                                                                                   System.lineSeparator()),
                                          "Copied " + "to clipboard", JOptionPane.INFORMATION_MESSAGE);
        }

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