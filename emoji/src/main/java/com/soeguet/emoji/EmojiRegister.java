package com.soeguet.emoji;

import com.soeguet.emoji.interfaces.EmojiInitializerInterface;
import java.util.HashMap;
import javax.swing.*;

public class EmojiRegister {

  /** Hashmap of all available emojis. */
  private HashMap<String, ImageIcon> emojiList;

  private EmojiRegister() {}

  private static final EmojiRegister emojiRegister = new EmojiRegister();

  public static EmojiRegister getEmojiRegisterInstance() {

    return emojiRegister;
  }

  /**
   * Initializes the emoji list.
   *
   * <p>This method initializes the emoji list by creating a new instance of the EmojiInitializer
   * class and calling the createEmojiList() method to obtain the list of emojis. The emoji list is
   * then assigned to the emojiList instance variable of the current object.
   *
   * <p>The EmojiInitializer class is responsible for creating and initializing the list of emojis.
   */
  public void initEmojiList(final EmojiInitializerInterface emojiInitializer) {

    this.emojiList = emojiInitializer.createEmojiList();
  }

  /**
   * Returns a HashMap containing the list of emojis and their corresponding image icons.
   *
   * <p>This method returns the emojiList HashMap, which contains the emojis and their corresponding
   * image icons. The key in the HashMap represents the emoji text, and the value represents the
   * corresponding image icon.
   *
   * @return the HashMap containing the list of emojis and their corresponding image icons
   */
  public HashMap<String, ImageIcon> getEmojiList() {

    return this.emojiList;
  }
}
