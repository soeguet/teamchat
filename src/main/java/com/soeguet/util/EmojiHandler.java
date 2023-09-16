package com.soeguet.util;

import com.soeguet.gui.main_frame.MainGuiElementsInterface;

import javax.swing.*;
import javax.swing.text.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmojiHandler {

    private final JFrame mainFrame;
    private final Logger logger = Logger.getLogger(EmojiHandler.class.getName());

    public EmojiHandler(JFrame mainFrame) {

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

        if (!(mainFrame instanceof MainGuiElementsInterface)) {
            return;
        }

        MainGuiElementsInterface gui = (MainGuiElementsInterface) mainFrame;

        StyledDocument doc = actualTextPane.getStyledDocument();

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
     Processes the insertion of an emoji in the given JTextPane.

     @param actualTextPane the JTextPane in which the emoji should be inserted
     @param gui            the MainGuiElementsInterface implementation providing GUI elements
     @param doc            the StyledDocument to insert the emoji into
     @param word           the emoji word to insert
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
     Creates a Style instance for displaying an image in a JTextPane.

     @param actualTextPane the JTextPane instance where the image style will be applied
     @param gui            the MainGuiElementsInterface instance that provides access to emoji images
     @param word           the word associated with the desired image

     @return a Style instance with the image set as an icon
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
