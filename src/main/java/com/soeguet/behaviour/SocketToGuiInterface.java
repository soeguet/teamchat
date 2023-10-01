package com.soeguet.behaviour;

import java.nio.ByteBuffer;

public interface SocketToGuiInterface {

    void onMessage(String message);

    void onMessage(byte[] message);
}