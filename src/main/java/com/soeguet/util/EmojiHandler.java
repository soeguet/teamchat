package com.soeguet.util;

import com.soeguet.gui.main_frame.ChatMainGuiElementsImpl;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class EmojiHandler {

    private final JFrame mainFrame;
    private final Logger logger = Logger.getLogger(EmojiHandler.class.getName());

    public EmojiHandler(JFrame mainFrame) {

        this.mainFrame = mainFrame;
    }

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
                System.out.println("Found an ImageIcon!");

                ImageIcon foundIcon = (ImageIcon) StyleConstants.getIcon(attributes);
                String description = foundIcon.getDescription();
                System.out.println("Description: " + description);

                childElement.getDocument().insertString(childElement.getEndOffset(), description + " ", null);
            }

            findImagesInElement(childElement);
        }
    }







}
