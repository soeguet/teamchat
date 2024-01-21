package com.soeguet.gui.main_frame;

public class MainFrameTests {
//
//    ChatMainFrameImpl chatMainFrame = Mockito.spy(ChatMainFrameImpl.class);
//
//    @Test
//    void testIfButtonEmojisLoadCorrectly() {
//
//        chatMainFrame.setButtonIcons();
//
//        Assertions.assertNotNull(chatMainFrame.getSendButton().getIcon());
//        Assertions.assertNotNull(chatMainFrame.getPictureButton().getIcon());
//        Assertions.assertNotNull(chatMainFrame.getEmojiButton().getIcon());
//    }
//
//    @Test
//    void testIfSystemOsIsDetected() {
//
//        String originalOsName = System.getProperty("os.name");
//
//        try {
//            System.setProperty("os.name", "Windows 10");
//
//            String result = chatMainFrame.getOSName();
//
//            assertEquals("Windows 10", result);
//        } finally {
//            System.setProperty("os.name", originalOsName);
//        }
//    }
//
//    @Test
//    void testIfLoadUsernameFromEnvVariablesWorks() {
//
//        EnvVariables envVariables = Mockito.spy(EnvVariables.class);
//        when(envVariables.getChatUsername()).thenReturn("Paul");
//
//        ChatMainFrameImpl frame = new ChatMainFrameImpl();
//        frame.setEnvVariables(envVariables);
//
//        frame.loadUsernameFromEnvVariables(envVariables);
//
//        assertEquals(
//                "Paul", frame.getUsername(), "should return \"Paul\" from environment variables");
//    }
//
//    @Test
//    void testIfEmojiHandlerInitializationWorksProperly() {
//
//        chatMainFrame.initEmojiHandler();
//
//        Assertions.assertNotNull(chatMainFrame.getEmojiHandler());
//    }
//
//    @Test
//    void testIfEmojiListInitializationWorksProperly() {
//
//        // mock dependency
//        EmojiInitializer mockEmojiInitializer = mock(EmojiInitializer.class);
//
//        // mock hashmap
//        HashMap<String, ImageIcon> fakeEmojiList = new HashMap<>();
//        fakeEmojiList.put("fakeEmoji", new ImageIcon());
//
//        // return hashmap
//        when(mockEmojiInitializer.createEmojiList()).thenReturn(fakeEmojiList);
//
//        // call method
//        chatMainFrame.initEmojiList(mockEmojiInitializer);
//
//        // assert
//        assertFalse(chatMainFrame.getEmojiList().isEmpty());
//    }
//
//    @Test
//    void testIfVersionIsReadCorrectly() {
//
//        ChatMainFrameImpl obj = Mockito.mock(ChatMainFrameImpl.class);
//        when(obj.retrieveJarVersion()).thenReturn("1.0");
//
//        assertEquals("1.0", obj.retrieveJarVersion());
//    }
//
//    @Test
//    void testIfStartUpIsRetrievedCorrectly() {
//
//        ChatMainFrameImpl obj = new ChatMainFrameImpl();
//        assertTrue(obj.isStartUp());
//    }
//
//    @Test
//    void checkIfNotificationStatusIsAllDenied() {
//
//        ChatMainFrameImpl obj = new ChatMainFrameImpl();
//        NotificationStatusHandler notificationStatusHandler = new NotificationStatusHandler(obj);
//        assertEquals(
//                notificationStatusHandler.getNotificationStatus(), NotificationStatus.ALL_DENIED);
//    }
//
//    @Test
//    void modifyStartUpValue() {
//
//        ChatMainFrameImpl obj = new ChatMainFrameImpl();
//        obj.setStartUp(false);
//        assertFalse(obj.isStartUp());
//    }
}