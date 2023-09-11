package com.soeguet.socket_client;

import com.soeguet.behaviour.SocketToGuiInterface;
import com.soeguet.gui.main_frame.MainGuiElementsInterface;
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
    private final JFrame mainFrame;

    private SocketToGuiInterface socketToGuiInterface;
    private JPanel messagePanel;

    public CustomWebsocketClient(URI serverUri, JFrame mainFrame) {

        super(serverUri);
        this.mainFrame = mainFrame;
    }

    public void setSocketToGuiInterface(SocketToGuiInterface socketToGuiInterface) {

        this.socketToGuiInterface = socketToGuiInterface;
    }

    @Override
    public void onWebsocketPong(WebSocket conn, Framedata f) {

        super.onWebsocketPong(conn, f);
    }


    @Override
    public void onOpen(ServerHandshake handshakedata) {

        logger.info("onOpen");

        createPopup("Connected to server");
    }

    private void createPopup(String message) {

        SwingUtilities.invokeLater(() -> {

            PopupPanelImpl popupPanel = new PopupPanelImpl(mainFrame, message);

            messagePanel = popupPanel;
            popupPanel.implementPopup();
        });
    }


    @Override
    public void onMessage(String message) {

        System.out.println(message);
        if (!message.startsWith("{")) {

            return;
        }

        if (mainFrame instanceof MainGuiElementsInterface) {

            ((MainGuiElementsInterface) mainFrame).getGuiFunctionality().onMessage(message);
        }

//        System.out.println("message = " + message);
//        logger.info("onMessage");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {

        logger.info("onClose");
        logger.info(String.valueOf(code));
        logger.info(reason);
        logger.info(String.valueOf(remote));

        createPopup(reason + " " + code);
    }

    @Override
    public void onError(Exception ex) {

        logger.info("onError");
        logger.info(ex.getMessage());

        createPopup("Error: " + ex.getMessage() + ", " + ex.getCause());
    }
}
