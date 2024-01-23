package com.soeguet.interfaces;

public interface SocketToGuiInterface {

  void onMessage(String message);

  void onMessage(byte[] message);
}
