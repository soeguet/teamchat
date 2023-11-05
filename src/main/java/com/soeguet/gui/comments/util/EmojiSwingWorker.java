package com.soeguet.gui.comments.util;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.util.HashMap;
import java.util.List;

public class EmojiSwingWorker extends SwingWorker<Void, Object> {

    // variables -- start
    private final JTextPane textPane;
    private final HashMap<String, ImageIcon> emojiList;
    private final String text;
    // variables -- end

    // constructors -- start
    public EmojiSwingWorker(JTextPane textPane, HashMap<String, ImageIcon> emojiList, String text) {

        this.textPane = textPane;
        this.emojiList = emojiList;
        this.text = text;
    }
    // constructors -- end

    @Override
    protected Void doInBackground() {

        String[] words = text.split(" ");
        StyledDocument doc = textPane.getStyledDocument();

        for (String word : words) {

            // Directly append text that's not an emoji
            if (!emojiList.containsKey(word)) {

                publish(word + " "); // Publish the word with a trailing space
                continue;
            }

            // For emojis, publish a placeholder
            ImageIcon emojiIcon = emojiList.get(word);

            if (emojiIcon != null) {

                // Using ImageIcon.toString() as a placeholder for the ImageIcon
                publish(emojiIcon);
            }
        }
        return null;
    }

    @Override
    protected void process(List<Object> chunks) {

        try {

            StyledDocument doc = textPane.getStyledDocument();

            for (Object chunk : chunks) {

                if (chunk instanceof ImageIcon imageIcon) {

                    // Insert the ImageIcon
                    textPane.insertIcon(imageIcon);

                } else if (chunk instanceof String string) {

                    // Insert the text
                    doc.insertString(doc.getLength(), string, null);
                }
            }

        } catch (BadLocationException e) {

            e.printStackTrace();
        }
    }

    @Override
    protected void done() {
        // Any finalization can be done here, this runs on the EDT.
    }
}