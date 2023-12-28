package com.soeguet.cache.factory;

import com.soeguet.cache.manager.CacheManager;

public class CacheManagerFactory {

    private static CacheManager cacheManager;

    public static CacheManager getCacheManager() {

        if (cacheManager == null) {

            cacheManager = new CacheManager();
        }

        return cacheManager;
    }
}
