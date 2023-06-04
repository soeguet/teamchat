package com.soeguet.gui.util;

import static com.soeguet.gui.ChatImpl.emojiListFull;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import org.jetbrains.annotations.NotNull;

public class EmojiConverter {

  public static void replaceWithEmoji(@NotNull JTextPane textPaneComment, String message) {

    textPaneComment.setText(message);

    for (String emoji : emojiListFull) {

      int stringLength = emoji.length();

      Path imagePath = new File("src/main/resources/emojis/" + emoji + ".png").toPath();
      ImageIcon emojiIcon;

      if (Files.exists(imagePath)) {

        emojiIcon = new ImageIcon("src/main/resources/emojis/" + emoji + ".png");

      } else {

        emojiIcon =
            new ImageIcon(
                Objects.requireNonNull(
                    EmojiConverter.class.getResource("/emojis/" + emoji + ".png")));
      }

      int offset = 0;

      while (offset != -1) {

        offset =
            textPaneComment
                .getText()
                .indexOf(
                    emoji, offset); // search for the code with the corresponding emoji character

        if (offset != -1) {

          textPaneComment.select(offset, offset + stringLength);
          textPaneComment.insertIcon(emojiIcon);
          textPaneComment.insertComponent(new JLabel(""));

          textPaneComment.revalidate();
          textPaneComment.repaint();
        }
      }
    }
  }

  public static void emojiListInit() {

    ClassLoader classLoader = EmojiConverter.class.getClassLoader();
    Enumeration<URL> resources;
    try {
      resources = classLoader.getResources("emojis/");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    while (resources.hasMoreElements()) {

      URL resourceUrl = resources.nextElement();

      // if the resources are WITHIN the .jar
      if (resourceUrl.getProtocol().equals("jar")) {

        try {

          JarURLConnection jarURLConnection = (JarURLConnection) resourceUrl.openConnection();
          try (JarFile jarFile = jarURLConnection.getJarFile()) {
            Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {

              JarEntry entry = entries.nextElement();
              String entryName = entry.getName();

              if (entryName.startsWith("emojis/") && entryName.endsWith(".png")) {

                String emojiName = entryName.replace(".png", "");
                emojiName = emojiName.replace("emojis/", "");

                emojiListFull.add(emojiName);
              }
            }
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      // if the resources are OUTSIDE the .jar
      else {

        File folder2 = new File("./src/main/resources/emojis");
        File[] files = folder2.listFiles();

        if (files != null) {

          for (File file : files) {

            if (file.getName().endsWith(".png")) {

              String emojiName = file.getName().replace(".png", "");
              emojiName = emojiName.replace("emojis/", "");

              emojiListFull.add(emojiName);
            }
          }
        }
      }
    }
  }

  @NotNull
  public static String checkTextForEmojis(@NotNull JTextPane textEditorPane) {

    StyledDocument doc = textEditorPane.getStyledDocument();

    int length = doc.getLength();

    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < length; i++) {

      Element element = doc.getCharacterElement(i);

      if (element.getName().equals("icon")) {
        ImageIcon icon = (ImageIcon) StyleConstants.getIcon(element.getAttributes());
        sb.append(icon.getDescription());
      } else {
        try {
          sb.append(doc.getText(i, 1));
        } catch (BadLocationException re) {
          re.printStackTrace();
        }
      }
    }

    return sb.toString();
  }
}
