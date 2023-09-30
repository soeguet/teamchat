package com.soeguet.cache;

import java.util.List;

public interface CustomCache<T> {

    void invalidateCache();

    void addLast(T data);

    String pollFirst();

    void removeAll();

    boolean isEmpty();

}