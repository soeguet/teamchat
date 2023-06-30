package com.soeguet.client;

import static com.soeguet.gui.ChatImpl.client;
import static com.soeguet.gui.ChatImpl.mapOfIps;

import com.soeguet.config.Settings;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.exceptions.InvalidFrameException;
import org.java_websocket.framing.Framedata;
import org.java_websocket.framing.FramedataImpl1;
import org.java_websocket.handshake.ServerHandshake;
import org.jetbrains.annotations.Nullable;

@Slf4j
public class CustomWebsocketClient extends WebSocketClient {

  @Nullable private static CustomWebsocketClient instance;
  private WebSocketListener listener;

  private CustomWebsocketClient(URI serverUri) {

    super(serverUri);
  }

  public static CustomWebsocketClient getInstance() {

    if (instance == null) {

      Settings settings = Settings.getInstance();

      try {

        instance =
            new CustomWebsocketClient(
                new URI("ws://" + settings.getIp() + ":" + settings.getPort()));
      } catch (URISyntaxException e) {
        throw new RuntimeException(e);
      }
    }
    return instance;
  }

  public static void resetClient() {

    instance = null;
  }

  @Override
  public void onWebsocketPong(WebSocket conn, Framedata f) {
    super.onWebsocketPong(conn, f);
  }

  @Override
  public void onWebsocketPing(WebSocket conn, Framedata f) {

    f.append(
        new FramedataImpl1(f.getOpcode()) {
          @Override
          public void isValid() throws InvalidDataException {
            if (!isFin()) {
              throw new InvalidFrameException("Control frame can't have fin==false set");
            }
            if (isRSV1()) {
              throw new InvalidFrameException("Control frame can't have rsv1==true set");
            }
            if (isRSV2()) {
              throw new InvalidFrameException("Control frame can't have rsv2==true set");
            }
            if (isRSV3()) {
              throw new InvalidFrameException("Control frame can't have rsv3==true set");
            }
          }

          @Override
          public ByteBuffer getPayloadData() {
            assert client != null;
            return ByteBuffer.wrap(
                mapOfIps
                    .get(client.getLocalSocketAddress().getAddress().getHostAddress())
                    .getBytes());
          }
        });
    super.onWebsocketPing(conn, f);
  }

  public void addListener(WebSocketListener listener) {

    this.listener = listener;
  }

  @Override
  public void onOpen(ServerHandshake handshakedata) {}

  @Override
  public void onMessage(String message) {
    // proceed with normal message
    if (listener != null) {
      listener.onMessageReceived(message);
    }
  }

  @Override
  public void onMessage(ByteBuffer bytes) {
    listener.onByteBufferMessageReceived(bytes);
  }

  @Override
  public void onClose(int code, String reason, boolean remote) {
    listener.onCloseReconnect();
  }

  @Override
  public void onError(Exception ex) {
    log.info("an error occured");
    log.info(ex.getMessage());
  }
}
