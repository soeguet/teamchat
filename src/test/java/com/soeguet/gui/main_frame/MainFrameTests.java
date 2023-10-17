package com.soeguet.gui.main_frame;

import com.soeguet.emoji.EmojiInitializer;
import com.soeguet.model.EnvVariables;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.swing.*;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MainFrameTests {

    ChatMainFrameImpl chatMainFrame = Mockito.spy(ChatMainFrameImpl.class);

    @Test
    void testIfButtonEmojisLoadCorrectly() {

        chatMainFrame.setButtonIcons();

        Assertions.assertNotNull(chatMainFrame.getSendButton().getIcon());
        Assertions.assertNotNull(chatMainFrame.getPictureButton().getIcon());
        Assertions.assertNotNull(chatMainFrame.getEmojiButton().getIcon());
    }

    @Test
    void testIfSystemOsIsDetected() {

        String originalOsName = System.getProperty("os.name");

        try {
            System.setProperty("os.name", "Windows 10");

            String result = chatMainFrame.getOSName();

            assertEquals("Windows 10", result);
        } finally {
            System.setProperty("os.name", originalOsName);
        }
    }

    @Test
    void testIfLoadUsernameFromEnvVariablesWorks() {

        EnvVariables envVariables = Mockito.spy(EnvVariables.class);
        when(envVariables.getChatUsername()).thenReturn("Paul");

        ChatMainFrameImpl frame = new ChatMainFrameImpl();
        frame.setEnvVariables(envVariables);

        frame.loadUsernameFromEnvVariables();

        assertEquals("Paul", frame.getUsername(), "should return \"Paul\" from environment variables");
    }

    @Test
    void testIfEmojiHandlerInitializationWorksProperly() {

        chatMainFrame.initEmojiHandler();

        Assertions.assertNotNull(chatMainFrame.getEmojiHandler());
    }

    @Test
    void testIfEmojiListInitializationWorksProperly() {

        //mock dependency
        EmojiInitializer mockEmojiInitializer = mock(EmojiInitializer.class);

        //mock hashmap
        HashMap<String, ImageIcon> fakeEmojiList = new HashMap<>();
        fakeEmojiList.put("fakeEmoji", new ImageIcon());

        //return hashmap
        when(mockEmojiInitializer.createEmojiList()).thenReturn(fakeEmojiList);

        //call method
        chatMainFrame.initEmojiList(mockEmojiInitializer);

        //assert
        Assertions.assertFalse(chatMainFrame.getEmojiList().isEmpty());
    }

    @Test
    void testIfDefaultVersionIsReturned() {

        //mock the classloader
        ClassLoader mockClassLoader = mock(ClassLoader.class);
        when(mockClassLoader.getResourceAsStream("version.properties")).thenReturn(null);

        when(chatMainFrame.getClassLoader()).thenReturn(mockClassLoader);

        String version = chatMainFrame.retrieveJarVersion();

        assertEquals("v.?", version);
    }
}