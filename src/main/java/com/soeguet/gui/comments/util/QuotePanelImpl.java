package com.soeguet.gui.comments.util;

import com.soeguet.gui.main_frame.MainFrameInterface;
import com.soeguet.gui.comments.util.generated.QuotePanel;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuotePanelImpl extends QuotePanel {

    private final Logger logger = Logger.getLogger(QuotePanelImpl.class.getName());
    private final MainFrameInterface mainFrame;

    public QuotePanelImpl(MainFrameInterface mainFrame, String text, String sender, String time) {

        this.mainFrame = mainFrame;

        this.getQuoteText().setEditorKit(new WrapEditorKit());

        createTextOnPane(this.getQuoteText(),text);
        this.getQuoteSender().setText(sender);
        this.getQuoteTime().setText(time);
    }

    private void createTextOnPane(JTextPane actualTextPane, String text) {

        StyledDocument doc = actualTextPane.getStyledDocument();

        processText(actualTextPane, mainFrame, doc,text);
    }

    private void processText(JTextPane actualTextPane, MainFrameInterface gui, StyledDocument doc, String text) {

        try {
            for (String word : text.split(" ")) {

                if (gui.getEmojiList().containsKey(word)) {
                    processEmoji(actualTextPane, gui, doc, word);

                } else {

                    doc.insertString(doc.getLength(), word + " ", null);
                }

            }

        } catch (Exception e) {

            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    /**
     * Processes and inserts an emoji image into a styled document.
     *
     * @param actualTextPane The JTextPane where the emoji image will be inserted.
     * @param gui The MainFrameInterface that provides the necessary GUI functionalities.
     * @param doc The StyledDocument where the emoji image will be inserted.
     * @param word The word associated with the emoji image.
     */
    private void processEmoji(JTextPane actualTextPane, MainFrameInterface gui, StyledDocument doc, String word) {

        Style style = createImageStyle(actualTextPane, gui, word);

        try {

            doc.insertString(doc.getLength(), " ", style);

        } catch (BadLocationException ex) {

            logger.log(Level.WARNING, ex.getMessage(), ex);
        }
    }

    /**
     * Creates a Style with an icon for inserting an image in a JTextPane.
     *
     * @param actualTextPane the JTextPane in which the image should be inserted
     * @param gui the MainFrameInterface implementation providing GUI elements
     * @param word the word associated with the image to be inserted
     * @return the Style with the icon set for the image
     */
    private static Style createImageStyle(JTextPane actualTextPane, MainFrameInterface gui, String word) {

        Style style = actualTextPane.addStyle("Image", null);

        ImageIcon emojiImage = gui.getEmojiList().get(word);
        ImageIcon imageIcon = new ImageIcon(emojiImage.getImage());
        imageIcon.setDescription(emojiImage.getDescription());
        StyleConstants.setIcon(style, imageIcon);

        return style;
    }
}