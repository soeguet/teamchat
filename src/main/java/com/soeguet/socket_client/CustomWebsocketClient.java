package com.soeguet.socket_client;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;

import com.soeguet.behaviour.interfaces.GuiFunctionalityInterface;
import com.soeguet.behaviour.interfaces.SocketToGuiInterface;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.gui.popups.PopupPanelImpl;
import com.soeguet.gui.popups.interfaces.PopupInterface;

public class CustomWebsocketClient extends WebSocketClient {

    private final Logger logger = Logger.getLogger(CustomWebsocketClient.class.getName());
    private final MainFrameGuiInterface mainFrame;
    private final SocketToGuiInterface socketToGuiInterface;

    public CustomWebsocketClient(URI serverUri, MainFrameGuiInterface mainFrame, final GuiFunctionalityInterface guiFunctionality) {

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

        PopupInterface popup = new PopupPanelImpl(mainFrame);
        popup.getMessageTextField().setText("Connected to server");
        popup.configurePopupPanelPlacement();
        popup.initiatePopupTimer(2_000);
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

        PopupInterface popup = new PopupPanelImpl(mainFrame);
        popup.getMessageTextField().setText("Connection closed." + System.lineSeparator() + reason + " " + code);
        popup.configurePopupPanelPlacement();
        popup.initiatePopupTimer(2_000);
    }

    @Override
    public void onError(Exception ex) {

        logger.info("onError");
        logger.info(ex.getMessage());

        PopupInterface popup = new PopupPanelImpl(mainFrame);
        popup.getMessageTextField().setText("Error: " + ex.getMessage());
        popup.configurePopupPanelPlacement();
        popup.initiatePopupTimer(2_000);


        throw new RuntimeException(ex);
    }

    @Override
    public void onMessage(final ByteBuffer bytes) {

        socketToGuiInterface.onMessage(bytes.array());
    }
}
