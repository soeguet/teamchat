package com.soeguet.gui.comments.generic_comment.gui_elements.menu_items;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.StringWriter;

public class CopyTextMenuItem extends JMenuItem implements MouseListener {

    // variables -- start
    private final JTextPane textPane;
    // variables -- end

    // constructors -- start
    public CopyTextMenuItem(final JTextPane textPane, final String menuItemName) {

        super(menuItemName);

        this.textPane = textPane;

        super.addMouseListener(this);
    }
    // constructors -- end

    private String extractTextForClipboard() {

        if (textPane.getSelectedText() != null && !textPane.getSelectedText().isBlank()) {

            return textPane.getSelectedText();

        } else if (textPane.getText() != null && !textPane.getText().isBlank()) {

            return extractOnlyPlainTextFromTextPane();

        } else {

            JOptionPane.showMessageDialog(SwingUtilities.getRootPane(textPane), "Nothing to copy!", ("No text " +
                                                                                                     "selected").formatted(), JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
    }

    private String extractOnlyPlainTextFromTextPane() {

        try {
            // if the document is an html document
            HTMLDocument document = (HTMLDocument) textPane.getDocument();
            HTMLEditorKit editorKit = new HTMLEditorKit();
            StringWriter writer = new StringWriter();

            try {

                editorKit.write(writer, document, document.getStartPosition().getOffset(), document.getLength());
                String textOnly = writer.toString();

                return textOnly.replaceAll("<[^>]+>", "").trim();

            } catch (Exception e) {

                throw new RuntimeException(e);
            }

        } catch (ClassCastException e) {

            // if the document is not a html document
            return textPane.getText();
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

            if (selectedText.length() > 50) {

                selectedText = "%s [...]".formatted(selectedText.substring(0, 50));
            }

            JOptionPane.showMessageDialog(SwingUtilities.getRootPane(textPane),
                                          "\"%s\" %s copied to clipboard".formatted(selectedText,
                                                                                    System.lineSeparator()), "Copied " +
                                                                                                             "to clipboard", JOptionPane.INFORMATION_MESSAGE);
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