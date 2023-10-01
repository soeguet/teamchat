package com.soeguet.behaviour;

public interface SocketToGuiInterface {

    void onMessage(String message);

    void onMessage(byte[] message);
}