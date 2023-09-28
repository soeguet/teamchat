package com.soeguet.cache.manager;

import com.soeguet.cache.CustomCache;
import com.soeguet.cache.implementations.ActiveNotificationQueue;
import com.soeguet.cache.implementations.MessageQueue;
import com.soeguet.cache.implementations.WaitingNotificationQueue;

import java.util.concurrent.ConcurrentHashMap;

public class CacheManager {

    private final ConcurrentHashMap<String, CustomCache> cacheList;

    public CacheManager() {

        cacheList = new ConcurrentHashMap<>();
    }

    public synchronized CustomCache getCache(String cacheName) {

        if (!cacheList.containsKey(cacheName)) {

            switch (cacheName) {

                case "WaitingNotificationQueue" -> cacheList.put(cacheName, new WaitingNotificationQueue());

                case "ActiveNotificationQueue" -> cacheList.put(cacheName, new ActiveNotificationQueue());

                case "MessageQueue" -> cacheList.put(cacheName, new MessageQueue());

                default -> throw new IllegalArgumentException("Cache not found");
            }
        }

        return cacheList.get(cacheName);
    }


}