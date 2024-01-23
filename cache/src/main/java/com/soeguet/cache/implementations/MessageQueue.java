package com.soeguet.cache.implementations;

import com.soeguet.cache.CustomCache;
import java.util.concurrent.LinkedBlockingDeque;

public class MessageQueue implements CustomCache<String> {
  private final LinkedBlockingDeque<String> messageQueue;

  public MessageQueue() {

    messageQueue = new LinkedBlockingDeque<>();
  }

  @Override
  public void invalidateCache() {

    messageQueue.clear();
  }

  @Override
  public String pollFirst() {

    return messageQueue.pollFirst();
  }

  @Override
  public void removeAll() {

    messageQueue.clear();
  }

  @Override
  public void addLast(final String data) {

    messageQueue.addLast(data);
  }

  @Override
  public boolean isEmpty() {

    return messageQueue.isEmpty();
  }
}
