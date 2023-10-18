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

    @SuppressWarnings("rawtypes")
    public synchronized CustomCache getCache(String cacheName) {

        final String lowercaseCacheName = cacheName.toLowerCase();

        if (!cacheList.containsKey(lowercaseCacheName)) {

            switch (lowercaseCacheName) {

                case "waitingnotificationqueue" -> cacheList.put(lowercaseCacheName, new WaitingNotificationQueue());

                case "activenotificationqueue" -> cacheList.put(lowercaseCacheName, new ActiveNotificationQueue());

                case "messagequeue" -> cacheList.put(lowercaseCacheName, new MessageQueue());

                case "propertiescache" -> cacheList.put(lowercaseCacheName, new MessageQueue());

                default -> throw new IllegalArgumentException("Cache not found");
            }
        }

        return cacheList.get(lowercaseCacheName);
    }

    public synchronized void invalidateCache() {

        cacheList.forEach((key, value) -> {

            value.invalidateCache();
        });
    }

}