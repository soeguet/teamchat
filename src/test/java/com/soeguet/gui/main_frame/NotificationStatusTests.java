package com.soeguet.gui.main_frame;

import com.soeguet.util.NotificationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class NotificationStatusTests {

    @Mock
    ChatMainFrameImpl chatMainFrame;
    @Mock
    JCheckBoxMenuItem internal;
    @Mock
    JCheckBoxMenuItem external;
    @Mock
    JCheckBoxMenuItem all;



    @BeforeEach
    void setUp() {

        chatMainFrame = mock(ChatMainFrameImpl.class);
        internal = mock(JCheckBoxMenuItem.class);
        external = mock(JCheckBoxMenuItem.class);
        all = mock(JCheckBoxMenuItem.class);

        doCallRealMethod().when(chatMainFrame).getNotificationStatus();
    }

    @Test
    void testInternalNotificationOnlyStatus() {

        //all notifications no
        when(chatMainFrame.getAllNotificationsMenuItem()).thenReturn(all);
        when(chatMainFrame.getAllNotificationsMenuItem().isSelected()).thenReturn(false);

        //internal yes
        when(chatMainFrame.getInternalNotificationsMenuItem()).thenReturn(internal);
        when(chatMainFrame.getInternalNotificationsMenuItem().isSelected()).thenReturn(true);

        //external no
        when(chatMainFrame.getExternalNotificationsMenuItem()).thenReturn(external);
        when(chatMainFrame.getExternalNotificationsMenuItem().isSelected()).thenReturn(false);

        assertEquals(NotificationStatus.INTERNAL_ONLY, chatMainFrame.getNotificationStatus());
    }


    @Test
    void testExternalNotificationOnlyStatus() {

        //all notifications no
        when(chatMainFrame.getAllNotificationsMenuItem()).thenReturn(all);
        when(chatMainFrame.getAllNotificationsMenuItem().isSelected()).thenReturn(false);

        //internal yes
        when(chatMainFrame.getInternalNotificationsMenuItem()).thenReturn(internal);
        when(chatMainFrame.getInternalNotificationsMenuItem().isSelected()).thenReturn(false);

        //external no
        when(chatMainFrame.getExternalNotificationsMenuItem()).thenReturn(external);
        when(chatMainFrame.getExternalNotificationsMenuItem().isSelected()).thenReturn(true);

        assertEquals(NotificationStatus.EXTERNAL_ONLY, chatMainFrame.getNotificationStatus());
    }



    @Test
    void testNoNotificationStatus() {

        //all notifications no
        when(chatMainFrame.getAllNotificationsMenuItem()).thenReturn(all);
        when(chatMainFrame.getAllNotificationsMenuItem().isSelected()).thenReturn(false);

        //internal yes
        when(chatMainFrame.getInternalNotificationsMenuItem()).thenReturn(internal);
        when(chatMainFrame.getInternalNotificationsMenuItem().isSelected()).thenReturn(false);

        //external no
        when(chatMainFrame.getExternalNotificationsMenuItem()).thenReturn(external);
        when(chatMainFrame.getExternalNotificationsMenuItem().isSelected()).thenReturn(false);

        assertEquals(NotificationStatus.ALL_DENIED, chatMainFrame.getNotificationStatus());
    }



    @Test
    void testAllNotificationStatus() {

        //all notifications no
        when(chatMainFrame.getAllNotificationsMenuItem()).thenReturn(all);
        when(chatMainFrame.getAllNotificationsMenuItem().isSelected()).thenReturn(false);

        //internal yes
        when(chatMainFrame.getInternalNotificationsMenuItem()).thenReturn(internal);
        when(chatMainFrame.getInternalNotificationsMenuItem().isSelected()).thenReturn(true);

        //external no
        when(chatMainFrame.getExternalNotificationsMenuItem()).thenReturn(external);
        when(chatMainFrame.getExternalNotificationsMenuItem().isSelected()).thenReturn(true);

        assertEquals(NotificationStatus.ALL_ALLOWED, chatMainFrame.getNotificationStatus());
    }


    @Test
    void testAllBlockedStatus() {

        //all notifications no
        when(chatMainFrame.getAllNotificationsMenuItem()).thenReturn(all);
        when(chatMainFrame.getAllNotificationsMenuItem().isSelected()).thenReturn(true);

        //internal yes
        when(chatMainFrame.getInternalNotificationsMenuItem()).thenReturn(internal);
        when(chatMainFrame.getInternalNotificationsMenuItem().isSelected()).thenReturn(true);

        //external no
        when(chatMainFrame.getExternalNotificationsMenuItem()).thenReturn(external);
        when(chatMainFrame.getExternalNotificationsMenuItem().isSelected()).thenReturn(true);

        assertEquals(NotificationStatus.ALL_DENIED, chatMainFrame.getNotificationStatus());
    }
}