package com.soeguet.emoji;

import com.soeguet.gui.main_frame.MainFrameInterface;

import javax.swing.*;
import javax.swing.text.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmojiHandler {

    private final MainFrameInterface mainFrame;
    private final Logger logger = Logger.getLogger(EmojiHandler.class.getName());

    public EmojiHandler(MainFrameInterface mainFrame) {

        this.mainFrame = mainFrame;
    }

    /**
     * Replaces image icons with emoji descriptions in a JTextPane.
     *
     * @param jTextPane the JTextPane in which to replace the image icons
     */
    public void replaceImageIconWithEmojiDescription(JTextPane jTextPane) {

        Element root = jTextPane.getStyledDocument().getDefaultRootElement();

        try {
            findImagesInElement(root);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     Finds image icons within an element and replaces them with their corresponding emoji descriptions.

     @param element the element to search for image icons

     @throws BadLocationException if an invalid location is encountered while inserting the emoji description
     */
    private void findImagesInElement(Element element) throws BadLocationException {

        for (int i = 0; i < element.getElementCount(); i++) {
            Element childElement = element.getElement(i);
            AttributeSet attributes = childElement.getAttributes();

            if (StyleConstants.getIcon(attributes) != null) {

                ImageIcon foundIcon = (ImageIcon) StyleConstants.getIcon(attributes);
                String description = foundIcon.getDescription();

                childElement.getDocument().insertString(childElement.getEndOffset(), description + " ", null);
            }

            findImagesInElement(childElement);
        }
    }


    /**
     Process the given text in order to replace any known emoji words with their corresponding emoji icons within a JTextPane.@param actualTextPane the JTextPane where the text is being processed

     @param text the text to be processed
     */
    public void replaceEmojiDescriptionWithActualImageIcon(JTextPane actualTextPane, String text) {

        StyledDocument doc = actualTextPane.getStyledDocument();

        try {
            for (String word : text.split(" ")) {

                if (mainFrame.getEmojiList().containsKey(word)) {
                    processEmoji(actualTextPane, mainFrame, doc, word);

                } else {

                    doc.insertString(doc.getLength(), word + " ", null);
                }

            }

        } catch (Exception e) {

            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    /**
     *
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
     * Creates a style with an image icon for a given word.
     *
     * @param actualTextPane the JTextPane where the style will be applied
     * @param gui the MainFrameInterface object for accessing the emoji list
     * @param word the word for which the image style is created
     * @return the created style with the image icon
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