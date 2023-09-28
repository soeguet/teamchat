package com.soeguet.cache.implementations;

import com.soeguet.cache.CustomCache;
import com.soeguet.model.jackson.BaseModel;

import java.util.concurrent.LinkedBlockingDeque;

public class ActiveNotificationQueue implements CustomCache {

    private final LinkedBlockingDeque<BaseModel> activeNotificationQueue;

    public ActiveNotificationQueue() {

        activeNotificationQueue = new LinkedBlockingDeque<>(3);
    }

    public int getRemainingCapacity() {

        return activeNotificationQueue.remainingCapacity();
    }

    public void addLast(BaseModel baseModel) {

        activeNotificationQueue.addLast(baseModel);
    }

    public BaseModel popFirst() {

        return activeNotificationQueue.pollFirst();
    }

    public boolean remove(final BaseModel baseModel) {

        return activeNotificationQueue.remove(baseModel);
    }

    public boolean isEmpty() {

        return activeNotificationQueue.isEmpty();
    }
}