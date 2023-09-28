package com.soeguet.cache.implementations;

import com.soeguet.cache.CustomCache;

import java.util.concurrent.LinkedBlockingDeque;

public class WaitingNotificationQueue implements CustomCache {
    private final LinkedBlockingDeque<String> notificationWaitingQueue;

    public WaitingNotificationQueue() {

        notificationWaitingQueue = new LinkedBlockingDeque<>();
    }

    public void addLast(final String message) {

        notificationWaitingQueue.addLast(message);
    }

    public String pollFirst() {

        return notificationWaitingQueue.pollFirst();
    }

    public void removeAll() {

        notificationWaitingQueue.clear();
    }
}