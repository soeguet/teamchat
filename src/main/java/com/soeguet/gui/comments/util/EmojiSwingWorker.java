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
    public Throwable exceptionNow() {

        throw new RuntimeException("Exception in SwingWorker");
    }

    @Override
    protected Void doInBackground() {

        StringBuilder textBuilder = new StringBuilder();

        // else the swingworker crashes with larger texts
        final int chunkSize = 100;
        int wordCounter = 0;

        String[] words = text.split(" ");

        for (String word : words) {

            if (emojiList.containsKey(word)) {

                wordCounter = processEmoji(word, textBuilder, wordCounter);

            } else {

                wordCounter = processWord(word, textBuilder, wordCounter, chunkSize);
            }
        }

        //publish last block if not empty
        if (!textBuilder.isEmpty()) {
            publish(textBuilder.toString());
        }

        return null;

    }

    private int processWord(final String word, final StringBuilder textBuilder, int wordCounter, final int chunkSize) {

        textBuilder.append(word).append(" ");
        wordCounter++;

        // Veröffentliche in größeren Blöcken
        if (wordCounter >= chunkSize) {

            wordCounter = publishAndResetText(textBuilder);
        }
        return wordCounter;
    }

    private int processEmoji(final String word, final StringBuilder textBuilder, int wordCounter) {

        ImageIcon emojiIcon = emojiList.get(word);

        if (emojiIcon != null) {

            //publish everything up until now
            if (!textBuilder.isEmpty()) {

                wordCounter = publishAndResetText(textBuilder);
            }

            // Veröffentliche das ImageIcon
            publish(emojiIcon);
        }
        return wordCounter;
    }

    private int publishAndResetText(StringBuilder textBuilder){

        publish(textBuilder.toString());
        textBuilder.setLength(0);
        return  0;
    }

    @Override
    protected void process(List<Object> chunks) {


        SwingUtilities.invokeLater(()-> {

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

            } catch (Exception e) {

                throw new RuntimeException(e);
            }
        });
    }
}