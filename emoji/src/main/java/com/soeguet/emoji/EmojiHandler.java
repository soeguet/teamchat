package com.soeguet.emoji;

import com.soeguet.emoji.interfaces.EmojiHandlerInterface;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.text.*;

public class EmojiHandler implements EmojiHandlerInterface {

  private final Logger logger = Logger.getLogger(EmojiHandler.class.getName());

  public EmojiHandler() {}

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
   * Finds image icons within an element and replaces them with their corresponding emoji
   * descriptions.
   *
   * @param element the element to search for image icons
   * @throws BadLocationException if an invalid location is encountered while inserting the emoji
   *     description
   */
  private void findImagesInElement(Element element) throws BadLocationException {

    for (int i = 0; i < element.getElementCount(); i++) {
      Element childElement = element.getElement(i);
      AttributeSet attributes = childElement.getAttributes();

      if (StyleConstants.getIcon(attributes) != null) {

        ImageIcon foundIcon = (ImageIcon) StyleConstants.getIcon(attributes);
        String description = foundIcon.getDescription();

        childElement
            .getDocument()
            .insertString(childElement.getEndOffset(), description + " ", null);
      }

      findImagesInElement(childElement);
    }
  }

  /**
   * Process the given text in order to replace any known emoji words with their corresponding emoji
   * icons within a JTextPane.@param actualTextPane the JTextPane where the text is being processed
   *
   * @param text the text to be processed
   */
  @Override
  public void replaceEmojiDescriptionWithActualImageIcon(JTextPane actualTextPane, String text) {

    StyledDocument doc = actualTextPane.getStyledDocument();

    EmojiRegister emojiRegister = EmojiRegister.getEmojiRegisterInstance();
    final HashMap<String, ImageIcon> emojiList = emojiRegister.getEmojiList();

    try {
      for (String word : text.split(" ")) {

        if (emojiList.containsKey(word)) {
          processEmoji(actualTextPane, doc, word);

        } else {

          doc.insertString(doc.getLength(), word + " ", null);
        }
      }

    } catch (Exception e) {

      logger.log(Level.WARNING, e.getMessage(), e);
      throw new RuntimeException();
    }
  }

  /** */
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
   * Creates a style with an image icon for a given word.
   *
   * @param actualTextPane the JTextPane where the style will be applied
   * @param word the word for which the image style is created
   * @return the created style with the image icon
   */
  private Style createImageStyle(JTextPane actualTextPane, String word) {

    Style style = actualTextPane.addStyle("Image", null);

    ImageIcon emojiImage = EmojiRegister.getEmojiRegisterInstance().getEmojiList().get(word);
    ImageIcon imageIcon = new ImageIcon(emojiImage.getImage());
    imageIcon.setDescription(emojiImage.getDescription());
    StyleConstants.setIcon(style, imageIcon);

    return style;
  }
}
