package com.soeguet.gui.main_panel;

public class MessageDisplayHandlerTest {
//
//    @Test
//    public void forceNullForPollingFromCache() {
//
//        CacheManager cacheManager = Mockito.mock(CacheManager.class);
//
//        MessageDisplayHandlerInterface messageDisplayHandlerInterface =
//                new MessageDisplayHandler(null, null);
//        messageDisplayHandlerInterface.setCacheManager(cacheManager);
//
//        assertNull(messageDisplayHandlerInterface.pollMessageFromCache());
//    }
//
//    @Test
//    public void pollFromEmptyCache() {
//
//        MessageDisplayHandlerInterface messageDisplayHandlerInterface =
//                new MessageDisplayHandler(null, null);
//        messageDisplayHandlerInterface.setCacheManager(new CacheManager());
//
//        assertNull(messageDisplayHandlerInterface.pollMessageFromCache());
//    }
//
//    @Test
//    public void pollFromFilledCache() {
//
//        CacheManager cacheManager = Mockito.mock(CacheManager.class);
//        MessageQueue messageQueue = new MessageQueue();
//        messageQueue.addLast("test");
//
//        Mockito.when(cacheManager.getCache("messageQueue")).thenReturn(messageQueue);
//
//        MessageDisplayHandlerInterface messageDisplayHandlerInterface =
//                new MessageDisplayHandler(null, null);
//        messageDisplayHandlerInterface.setCacheManager(cacheManager);
//
//        assertEquals("test", messageDisplayHandlerInterface.pollMessageFromCache());
//    }
}