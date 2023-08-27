package com.soeguet.client;

import com.soeguet.config.Settings;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.exceptions.InvalidFrameException;
import org.java_websocket.framing.Framedata;
import org.java_websocket.framing.FramedataImpl1;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import static com.soeguet.gui.ChatImpl.client;
import static com.soeguet.gui.ChatImpl.mapOfIps;

public class CustomWebsocketClient extends WebSocketClient {

    private final static Logger LOG = Logger.getLogger(CustomWebsocketClient.class.getName());
    private static volatile CustomWebsocketClient instance;
    private WebSocketListener listener;

    private CustomWebsocketClient(URI serverUri) {

        super(serverUri);
    }

    public static CustomWebsocketClient getInstance() {

        if (instance == null) {
            instance = createNewInstance();
        }
        return instance;
    }

    private static CustomWebsocketClient createNewInstance() {

        Settings settings = Settings.getInstance();
        try {
            return new CustomWebsocketClient(new URI("ws://" + settings.getIp() + ":" + settings.getPort()));
        } catch (URISyntaxException e) {
            LOG.warning("Error creating URI: " + e.getMessage());
            throw new IllegalArgumentException("Invalid URI syntax", e);
        }
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

        f.append(new FramedataImpl1(f.getOpcode()) {

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
                return ByteBuffer.wrap(mapOfIps.get(client.getLocalSocketAddress().getAddress().getHostAddress()).getBytes());
            }
        });
        super.onWebsocketPing(conn, f);
    }

    public void addListener(WebSocketListener listener) {

        this.listener = listener;
    }

    @Override
    public void onOpen(ServerHandshake handshakeData) {

        LOG.info(String.valueOf(handshakeData.getHttpStatus()));
        LOG.info(handshakeData.getHttpStatusMessage());
    }

    @Override
    public void onMessage(String message) {
        // proceed with a normal message
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

        LOG.info("an error occurred");
        LOG.info(ex.getMessage());
    }
}
