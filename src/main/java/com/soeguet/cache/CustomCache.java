package com.soeguet.cache;

public interface CustomCache<T> {

    void invalidateCache();

    void addLast(T data);

    String pollFirst();

    void removeAll();

    boolean isEmpty();
}
