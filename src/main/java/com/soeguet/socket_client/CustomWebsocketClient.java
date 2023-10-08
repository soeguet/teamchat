package com.soeguet.socket_client;

import com.soeguet.behaviour.interfaces.GuiFunctionality;
import com.soeguet.behaviour.interfaces.SocketToGuiInterface;
import com.soeguet.gui.main_frame.MainFrameInterface;
import com.soeguet.gui.popups.PopupPanelImpl;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

public class CustomWebsocketClient extends WebSocketClient {

    private final Logger logger = Logger.getLogger(CustomWebsocketClient.class.getName());
    private final MainFrameInterface mainFrame;
    private final SocketToGuiInterface socketToGuiInterface;

    public CustomWebsocketClient(URI serverUri, MainFrameInterface mainFrame, final GuiFunctionality guiFunctionality) {

        super(serverUri);
        this.mainFrame = mainFrame;

        if (guiFunctionality instanceof SocketToGuiInterface socket) {

            this.socketToGuiInterface = socket;

        } else {

            throw new IllegalArgumentException("socketToGuiInterface must implement SocketToGuiInterface");
        }
    }

    @Override
    public void onWebsocketPing(final WebSocket conn, final Framedata f) {

        //remove typing label
        mainFrame.getTypingLabel().setText(" ");

        super.onWebsocketPing(conn, f);
    }

    @Override
    public void onWebsocketPong(WebSocket conn, Framedata f) {

        //remove typing label
        mainFrame.getTypingLabel().setText(" ");

        super.onWebsocketPong(conn, f);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {

        logger.info("onOpen");

        new PopupPanelImpl(mainFrame, "Connected to server").implementPopup(2000);
    }

    @Override
    public void onMessage(String message) {

        socketToGuiInterface.onMessage(message);
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

    @Override
    public void onMessage(final ByteBuffer bytes) {

        socketToGuiInterface.onMessage(bytes.array());
    }
}