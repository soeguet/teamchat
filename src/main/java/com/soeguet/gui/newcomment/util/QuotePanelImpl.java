package com.soeguet.gui.newcomment.util;

import com.soeguet.gui.main_frame.MainGuiElementsInterface;
import com.soeguet.gui.newcomment.util.generated.QuotePanel;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuotePanelImpl extends QuotePanel {

    private final Logger logger = Logger.getLogger(QuotePanelImpl.class.getName());
    private final JFrame mainFrame;

    public QuotePanelImpl(JFrame mainFrame, String text, String sender, String time) {

        this.mainFrame = mainFrame;

        this.getQuoteText().setEditorKit(new WrapEditorKit());

        createTextOnPane(this.getQuoteText(),text);
        this.getQuoteSender().setText(sender);
        this.getQuoteTime().setText(time);
    }

    private void createTextOnPane(JTextPane actualTextPane, String text) {

        if (!(mainFrame instanceof MainGuiElementsInterface)) {
            return;
        }

        MainGuiElementsInterface gui = (MainGuiElementsInterface) mainFrame;
        StyledDocument doc = actualTextPane.getStyledDocument();

        processText(actualTextPane, gui, doc,text);
    }

    private void processText(JTextPane actualTextPane, MainGuiElementsInterface gui, StyledDocument doc, String text) {

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
     * Processes the insertion of an emoji in the given JTextPane.
     *
     * @param actualTextPane the JTextPane in which the emoji should be inserted
     * @param gui the MainGuiElementsInterface implementation providing GUI elements
     * @param doc the StyledDocument to insert the emoji into
     * @param word the emoji word to insert
     */
    private void processEmoji(JTextPane actualTextPane, MainGuiElementsInterface gui, StyledDocument doc, String word) {

        Style style = createImageStyle(actualTextPane, gui, word);

        try {

            doc.insertString(doc.getLength(), " ", style);

        } catch (BadLocationException ex) {

            logger.log(Level.WARNING, ex.getMessage(), ex);
        }
    }

    /**
     * Creates a Style instance for displaying an image in a JTextPane.
     *
     * @param actualTextPane the JTextPane instance where the image style will be applied
     * @param gui the MainGuiElementsInterface instance that provides access to emoji images
     * @param word the word associated with the desired image
     * @return a Style instance with the image set as an icon
     */
    private static Style createImageStyle(JTextPane actualTextPane, MainGuiElementsInterface gui, String word) {

        Style style = actualTextPane.addStyle("Image", null);

        ImageIcon emojiImage = gui.getEmojiList().get(word);
        ImageIcon imageIcon = new ImageIcon(emojiImage.getImage());
        imageIcon.setDescription(emojiImage.getDescription());
        StyleConstants.setIcon(style, imageIcon);

        return style;
    }
}
