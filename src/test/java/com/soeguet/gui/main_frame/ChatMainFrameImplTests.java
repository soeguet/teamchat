package com.soeguet.gui.main_frame;

import com.soeguet.util.NotificationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ChatMainFrameImplTests {

    @Mock
    ChatMainFrameImpl chatMainFrame;

    @BeforeEach
    void setUp() {

        chatMainFrame = mock(ChatMainFrameImpl.class);
        doCallRealMethod().when(chatMainFrame).setScrollPaneMargins();
        doCallRealMethod().when(chatMainFrame).getJSCROLLPANE_MARGIN_RIGHT_BORDER();
        doCallRealMethod().when(chatMainFrame).getJSCROLLPANE_MARGIN_BOTTOM_BORDER();
    }

    @Test
    void testSetScrollPaneMarginsWindows() {

        when(chatMainFrame.getOSName()).thenReturn("Windows 10");

        chatMainFrame.setScrollPaneMargins();

        assertEquals(63, chatMainFrame.getJSCROLLPANE_MARGIN_BOTTOM_BORDER());
        assertEquals(20, chatMainFrame.getJSCROLLPANE_MARGIN_RIGHT_BORDER());
    }

    @Test
    void testSetScrollPaneMarginsLinuxKDE() {

        when(chatMainFrame.getOSName()).thenReturn("Linux");
        when(chatMainFrame.getDesktopEnv()).thenReturn("KDE");

        chatMainFrame.setScrollPaneMargins();

        assertEquals(56, chatMainFrame.getJSCROLLPANE_MARGIN_BOTTOM_BORDER());
        assertEquals(4, chatMainFrame.getJSCROLLPANE_MARGIN_RIGHT_BORDER());
    }

    @Test
    void testSetScrollPaneMarginsLinuxGnome() {

        when(chatMainFrame.getOSName()).thenReturn("Linux");
        when(chatMainFrame.getDesktopEnv()).thenReturn("GNOME");

        chatMainFrame.setScrollPaneMargins();

        assertEquals(27, chatMainFrame.getJSCROLLPANE_MARGIN_BOTTOM_BORDER());
        assertEquals(4, chatMainFrame.getJSCROLLPANE_MARGIN_RIGHT_BORDER());
    }


    @Test
    void testJCheckMenuItemsInGeneral() {

        JCheckBoxMenuItem mockMenuItem = mock(JCheckBoxMenuItem.class);

        when(chatMainFrame.getInternalNotificationsMenuItem()).thenReturn(mockMenuItem);
        when(chatMainFrame.getInternalNotificationsMenuItem().isSelected()).thenReturn(true);
        assertTrue(chatMainFrame.getInternalNotificationsMenuItem().isSelected());
    }
}