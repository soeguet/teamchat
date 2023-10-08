package com.soeguet.emoji;

import com.soeguet.gui.main_frame.ChatMainFrameImpl;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class EmojiInitializer {

    private final Logger logger = Logger.getLogger(EmojiInitializer.class.getName());

    /**
     Creates a list of ImageIcons for emojis.

     @return an ArrayList of ImageIcons representing emojis
     */
    public HashMap<String, ImageIcon> createEmojiList() {

        HashMap<String, ImageIcon> imageIcons = new HashMap<>();
        CodeSource src = ChatMainFrameImpl.class.getProtectionDomain().getCodeSource();

        if (src != null) {

            try (ZipInputStream zip = new ZipInputStream(src.getLocation().openStream())) {

                processZipEntries(imageIcons, zip);

            } catch (IOException e) {

                logger.log(Level.WARNING, e.getMessage(), e);
            }
        }

        return imageIcons;
    }

    /**
     Processes the entries in a zip file and adds the emoji images to the provided ArrayList.

     @param imageIcons the ArrayList to store the emoji images
     @param zip        the ZipInputStream representing the zip file to process

     @throws IOException if an I/O error occurs while reading the zip file
     */
    private void processZipEntries(HashMap<String, ImageIcon> imageIcons, ZipInputStream zip) throws IOException {

        ZipEntry ze;

        while ((ze = zip.getNextEntry()) != null) {

            String entryName = ze.getName();

            if (isEmojiEntry(entryName)) {

                createAndAddImageIcon(imageIcons, entryName);
            }
        }
    }

    /**
     Checks if the given entry name is an emoji entry.

     @param entryName the name of the entry to check

     @return true if the entry name starts with "emojis/" and is not equal to "emojis/", false
     otherwise
     */
    private boolean isEmojiEntry(String entryName) {

        return entryName.startsWith("emojis/") && !entryName.equals("emojis/");
    }

    /**
     Creates an ImageIcon from a given entry name and adds it to the given list of ImageIcons.

     @param imageIcons the list of ImageIcons to add the created ImageIcon to
     @param entryName  the name of the entry to create the ImageIcon from
     */
    private void createAndAddImageIcon(HashMap<String, ImageIcon> imageIcons, String entryName) {

        URL emojiUrl = getClass().getResource("/" + entryName);
        assert emojiUrl != null;
        ImageIcon imageIcon = new ImageIcon(emojiUrl);
        imageIcon.setDescription(entryName.replace("emojis/", "").replace(".png", ""));
        imageIcons.put(imageIcon.getDescription(), imageIcon);
    }
}