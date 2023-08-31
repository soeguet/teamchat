package com.soeguet.socket_client;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.logging.Logger;

public class CustomWebsocketClient extends WebSocketClient {

    private final Logger logger = Logger.getLogger(CustomWebsocketClient.class.getName());

    public CustomWebsocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onWebsocketPong(WebSocket conn, Framedata f) {
        super.onWebsocketPong(conn, f);
    }



    @Override
    public void onOpen(ServerHandshake handshakedata) {

        logger.info("onOpen");
    }

    @Override
    public void onMessage(String message) {

        logger.info("onMessage");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {

        logger.info("onClose");
        logger.info(String.valueOf(code));
        logger.info(reason);
        logger.info(String.valueOf(remote));
    }

    @Override
    public void onError(Exception ex) {

        logger.info("onError");
        logger.info(ex.getMessage());
    }
}
