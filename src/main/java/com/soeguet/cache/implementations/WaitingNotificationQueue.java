package com.soeguet.cache.implementations;

import com.soeguet.cache.CustomCache;

import java.util.concurrent.LinkedBlockingDeque;

public class WaitingNotificationQueue implements CustomCache<String> {

    private final LinkedBlockingDeque<String> notificationWaitingQueue;

    public WaitingNotificationQueue() {

        notificationWaitingQueue = new LinkedBlockingDeque<>();
    }

    @Override
    public void invalidateCache() {

        notificationWaitingQueue.clear();
    }

    @Override
    public void addLast(final String message) {

        notificationWaitingQueue.addLast(message);
    }

    @Override
    public String pollFirst() {

        return notificationWaitingQueue.pollFirst();
    }

    @Override
    public void removeAll() {

        notificationWaitingQueue.clear();
    }

    @Override
    public boolean isEmpty() {

        return notificationWaitingQueue.isEmpty();
    }
}