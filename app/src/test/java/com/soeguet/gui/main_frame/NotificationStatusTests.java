package com.soeguet.gui.main_frame;

public class NotificationStatusTests {
//
//    @Mock ChatMainFrameImpl chatMainFrame;
//    @Mock JCheckBoxMenuItem internal;
//    @Mock JCheckBoxMenuItem external;
//    @Mock JCheckBoxMenuItem all;
//
//    @Mock NotificationStatusHandlerInterface notificationStatusHandler;
//
//    @BeforeEach
//    void setUp() {
//
//        chatMainFrame = mock(ChatMainFrameImpl.class);
//        notificationStatusHandler = new NotificationStatusHandler(chatMainFrame);
//        internal = mock(JCheckBoxMenuItem.class);
//        external = mock(JCheckBoxMenuItem.class);
//        all = mock(JCheckBoxMenuItem.class);
//
//        //        doCallRealMethod().when(chatMainFrame).getNotificationStatus();
//    }
//
//    @Test
//    void testInternalNotificationOnlyStatus() {
//
//        // all notifications no
//        when(chatMainFrame.getAllNotificationMenuItem()).thenReturn(all);
//        when(chatMainFrame.getAllNotificationMenuItem().isSelected()).thenReturn(false);
//
//        // internal yes
//        when(chatMainFrame.getInternalNotificationsMenuItem()).thenReturn(internal);
//        when(chatMainFrame.getInternalNotificationsMenuItem().isSelected()).thenReturn(true);
//
//        // external no
//        when(chatMainFrame.getExternalNotificationsMenuItem()).thenReturn(external);
//        when(chatMainFrame.getExternalNotificationsMenuItem().isSelected()).thenReturn(false);
//
//        assertEquals(
//                NotificationStatus.INTERNAL_ONLY,
//                notificationStatusHandler.getNotificationStatus());
//    }
//
//    @Test
//    void testExternalNotificationOnlyStatus() {
//
//        // all notifications no
//        when(chatMainFrame.getAllNotificationMenuItem()).thenReturn(all);
//        when(chatMainFrame.getAllNotificationMenuItem().isSelected()).thenReturn(false);
//
//        // internal yes
//        when(chatMainFrame.getInternalNotificationsMenuItem()).thenReturn(internal);
//        when(chatMainFrame.getInternalNotificationsMenuItem().isSelected()).thenReturn(false);
//
//        // external no
//        when(chatMainFrame.getExternalNotificationsMenuItem()).thenReturn(external);
//        when(chatMainFrame.getExternalNotificationsMenuItem().isSelected()).thenReturn(true);
//
//        assertEquals(
//                NotificationStatus.EXTERNAL_ONLY,
//                notificationStatusHandler.getNotificationStatus());
//    }
//
//    @Test
//    void testNoNotificationStatus() {
//
//        // all notifications no
//        when(chatMainFrame.getAllNotificationMenuItem()).thenReturn(all);
//        when(chatMainFrame.getAllNotificationMenuItem().isSelected()).thenReturn(false);
//
//        // internal yes
//        when(chatMainFrame.getInternalNotificationsMenuItem()).thenReturn(internal);
//        when(chatMainFrame.getInternalNotificationsMenuItem().isSelected()).thenReturn(false);
//
//        // external no
//        when(chatMainFrame.getExternalNotificationsMenuItem()).thenReturn(external);
//        when(chatMainFrame.getExternalNotificationsMenuItem().isSelected()).thenReturn(false);
//
//        assertEquals(
//                NotificationStatus.ALL_DENIED, notificationStatusHandler.getNotificationStatus());
//    }
//
//    @Test
//    void testAllNotificationStatus() {
//
//        // all notifications no
//        when(chatMainFrame.getAllNotificationMenuItem()).thenReturn(all);
//        when(chatMainFrame.getAllNotificationMenuItem().isSelected()).thenReturn(false);
//
//        // internal yes
//        when(chatMainFrame.getInternalNotificationsMenuItem()).thenReturn(internal);
//        when(chatMainFrame.getInternalNotificationsMenuItem().isSelected()).thenReturn(true);
//
//        // external no
//        when(chatMainFrame.getExternalNotificationsMenuItem()).thenReturn(external);
//        when(chatMainFrame.getExternalNotificationsMenuItem().isSelected()).thenReturn(true);
//
//        assertEquals(
//                NotificationStatus.ALL_ALLOWED, notificationStatusHandler.getNotificationStatus());
//    }
//
//    @Test
//    void testAllBlockedStatus() {
//
//        // all notifications no
//        when(chatMainFrame.getAllNotificationMenuItem()).thenReturn(all);
//        when(chatMainFrame.getAllNotificationMenuItem().isSelected()).thenReturn(true);
//
//        // internal yes
//        when(chatMainFrame.getInternalNotificationsMenuItem()).thenReturn(internal);
//        when(chatMainFrame.getInternalNotificationsMenuItem().isSelected()).thenReturn(true);
//
//        // external no
//        when(chatMainFrame.getExternalNotificationsMenuItem()).thenReturn(external);
//        when(chatMainFrame.getExternalNotificationsMenuItem().isSelected()).thenReturn(true);
//
//        assertEquals(
//                NotificationStatus.ALL_DENIED, notificationStatusHandler.getNotificationStatus());
//    }
}