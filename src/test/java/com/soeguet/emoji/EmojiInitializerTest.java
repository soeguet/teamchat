package com.soeguet.emoji;

import com.soeguet.emoji.interfaces.EmojiInitializerInterface;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class EmojiInitializerTest {

    @Test
    void emojiListShouldBeSizeZeroSinceItsNotTheJarVersion() {

        EmojiInitializerInterface emojiInitializer = new EmojiInitializer();

        final HashMap<String, ImageIcon> emojiList = emojiInitializer.createEmojiList();

        assertEquals(emojiList.size(), 0, "Should be zero since the test is not run from the jar file.");
    }
}