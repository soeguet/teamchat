package com.soeguet.cache.implementations;

import com.soeguet.cache.CustomCache;
import com.soeguet.model.jackson.BaseModel;

import java.util.concurrent.LinkedBlockingDeque;

public class ActiveNotificationQueue implements CustomCache<BaseModel> {

    private final LinkedBlockingDeque<BaseModel> activeNotificationQueue;

    public ActiveNotificationQueue() {

        activeNotificationQueue = new LinkedBlockingDeque<>(3);
    }

    @Override
    public void invalidateCache() {

        activeNotificationQueue.clear();
    }

    @Override
    public void addLast(BaseModel baseModel) throws IllegalStateException {

        activeNotificationQueue.addLast(baseModel);
    }

    @Override
    public String pollFirst() {

        return null;
    }

    @Override
    public void removeAll() {

        activeNotificationQueue.clear();
    }

    @Override
    public boolean isEmpty() {

        return activeNotificationQueue.isEmpty();
    }

    public int getRemainingCapacity() {

        return activeNotificationQueue.remainingCapacity();
    }

    public BaseModel popFirst() {

        return activeNotificationQueue.pollFirst();
    }

    public boolean remove(final BaseModel baseModel) {

        return activeNotificationQueue.remove(baseModel);
    }
}