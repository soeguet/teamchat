package com.soeguet.gui.main_frame;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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

            Assertions.assertEquals("Windows 10", result);
        } finally {
            System.setProperty("os.name", originalOsName);
        }
    }

    @Test
    void testIfSystemDesktopEnvironmentIsDetected() {

        Mockito.when(chatMainFrame.getDesktopEnv()).thenReturn("GNOME");

        String result = chatMainFrame.getDesktopEnv();

        Assertions.assertEquals("GNOME", result);

    }

    @Test
    void testIfClientControllerIsInitializedProperly(){

        chatMainFrame.initGuiFunctionality();
        chatMainFrame.initializeClientController();

        Assertions.assertNotNull(chatMainFrame.getClientController());
    }
}