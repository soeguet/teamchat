package com.soeguet.gui.main_panel;

import com.soeguet.cache.implementations.MessageQueue;
import com.soeguet.cache.manager.CacheManager;
import com.soeguet.gui.main_panel.interfaces.MessageDisplayHandlerInterface;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MessageDisplayHandlerTest {

    @Test
    public void forceNullForPollingFromCache() {

        CacheManager cacheManager = Mockito.mock(CacheManager.class);

        MessageDisplayHandlerInterface messageDisplayHandlerInterface = new MessageDisplayHandler(null, null);
        messageDisplayHandlerInterface.setCacheManager(cacheManager);

        assertNull(messageDisplayHandlerInterface.pollMessageFromCache());
    }

    @Test
    public void pollFromEmptyCache() {

        MessageDisplayHandlerInterface messageDisplayHandlerInterface = new MessageDisplayHandler(null, null);
        messageDisplayHandlerInterface.setCacheManager(new CacheManager());

        assertNull(messageDisplayHandlerInterface.pollMessageFromCache());
    }

    @Test
    public void pollFromFilledCache() {

        CacheManager cacheManager = Mockito.mock(CacheManager.class);
        MessageQueue messageQueue = new MessageQueue();
        messageQueue.addLast("test");

        Mockito.when(cacheManager.getCache("messageQueue")).thenReturn(messageQueue);

        MessageDisplayHandlerInterface messageDisplayHandlerInterface = new MessageDisplayHandler(null, null);
        messageDisplayHandlerInterface.setCacheManager(cacheManager);

        assertEquals("test", messageDisplayHandlerInterface.pollMessageFromCache());
    }
}