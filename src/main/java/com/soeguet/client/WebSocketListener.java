package com.soeguet.client;

import java.nio.ByteBuffer;

public interface WebSocketListener {

  void onMessageReceived(String message);

  void onByteBufferMessageReceived(ByteBuffer bytes);

  void onCloseReconnect();
}
