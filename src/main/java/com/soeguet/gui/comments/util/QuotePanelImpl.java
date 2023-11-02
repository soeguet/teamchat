package com.soeguet.gui.comments.util;

import com.soeguet.gui.comments.util.generated.QuotePanel;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuotePanelImpl extends QuotePanel {

    private final Logger logger = Logger.getLogger(QuotePanelImpl.class.getName());
    private final MainFrameGuiInterface mainFrame;

    public QuotePanelImpl(MainFrameGuiInterface mainFrame, String text, String sender, String time) {

        this.mainFrame = mainFrame;

        this.getQuoteText().setEditorKit(new WrapEditorKit());

        createTextOnPane(this.getQuoteText(), text);
        this.getQuoteSender().setText(sender);
        this.getQuoteTime().setText(time);
    }


    private void createTextOnPane(JTextPane actualTextPane, String text) {

        StyledDocument doc = actualTextPane.getStyledDocument();

        processText(actualTextPane, doc, text);
    }

    private void processText(JTextPane actualTextPane, StyledDocument styledDocument, String text) {

        try {
            for (String word : text.split(" ")) {

                if (mainFrame.getEmojiList().containsKey(word)) {
                    processEmoji(actualTextPane, styledDocument, word);

                } else {

                    styledDocument.insertString(styledDocument.getLength(), word + " ", null);
                }

            }

        } catch (Exception e) {

            logger.log(Level.WARNING, e.getMessage(), e);
            throw new RuntimeException();
        }
    }


    /**
     * Process an emoji in the given text pane and insert it into the styled document.
     *
     * @param actualTextPane The JTextPane in which the emoji is to be inserted.
     * @param doc The StyledDocument where the emoji is to be inserted.
     * @param word The emoji word or code to be processed.
     */
    private void processEmoji(JTextPane actualTextPane, StyledDocument doc, String word) {

        Style style = createImageStyle(actualTextPane, word);

        try {

            doc.insertString(doc.getLength(), " ", style);

        } catch (BadLocationException ex) {

            logger.log(Level.WARNING, ex.getMessage(), ex);
            throw new RuntimeException();
        }
    }


    /**
     * Creates a style for inserting an image into a JTextPane.
     *
     * @param actualTextPane The JTextPane in which the image will be inserted.
     * @param word The word or code representing the image.
     * @return The style with the image icon set.
     */
    private Style createImageStyle(JTextPane actualTextPane, String word) {

        Style style = actualTextPane.addStyle("Image", null);

        ImageIcon emojiImage = mainFrame.getEmojiList().get(word);
        ImageIcon imageIcon = new ImageIcon(emojiImage.getImage());
        imageIcon.setDescription(emojiImage.getDescription());
        StyleConstants.setIcon(style, imageIcon);

        return style;
    }
}