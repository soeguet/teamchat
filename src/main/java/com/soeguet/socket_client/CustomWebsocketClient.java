package com.soeguet.socket_client;

import com.soeguet.gui.main_frame.MainFrameInterface;
import com.soeguet.gui.popups.PopupPanelImpl;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;

import javax.swing.*;
import java.net.URI;
import java.util.logging.Logger;

public class CustomWebsocketClient extends WebSocketClient {

    private final Logger logger = Logger.getLogger(CustomWebsocketClient.class.getName());
    private final MainFrameInterface mainFrame;

    public CustomWebsocketClient(URI serverUri, MainFrameInterface mainFrame) {

        super(serverUri);
        this.mainFrame = mainFrame;
    }

    @Override
    public void onWebsocketPong(WebSocket conn, Framedata f) {

        super.onWebsocketPong(conn, f);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {

        logger.info("onOpen");

        new PopupPanelImpl(mainFrame, "Connected to server").implementPopup(2000);
    }

    @Override
    public void onMessage(String message) {

        mainFrame.getGuiFunctionality().onMessage(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {

        logger.info("onClose");
        logger.info(String.valueOf(code));
        logger.info(reason);
        logger.info(String.valueOf(remote));

        new PopupPanelImpl(mainFrame, "Connection closed." + System.lineSeparator() + reason + " " + code).implementPopup(2000);
    }

    @Override
    public void onError(Exception ex) {

        logger.info("onError");
        logger.info(ex.getMessage());

        new PopupPanelImpl(mainFrame, "Error: " + ex.getMessage()).implementPopup(2000);
    }
}