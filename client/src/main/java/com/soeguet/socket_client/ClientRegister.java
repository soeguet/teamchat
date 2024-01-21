package com.soeguet.socket_client;

public class ClientRegister {

    private static final ClientRegister CLIENT_INSTANCE = new ClientRegister();

    /** The name of the client which was last posted on the main panel. */
    private String lastSenderName;

    /** The last time someone posted on the main panel. */
    private String lastTimeStamp;
    private CustomWebsocketClient customWebsocketClient;

    public void send(final String messageString) {

        this.getCustomWebsocketClient().send(messageString);
    }

    public void send(final byte[] messageBytes) {

        this.getCustomWebsocketClient().send(messageBytes);
    }

    public String getLastSenderName() {

        return lastSenderName;
    }

    public void setLastSenderName(final String lastSenderName) {

        this.lastSenderName = lastSenderName;
    }

    public String getLastTimeStamp() {

        return lastTimeStamp;
    }

    public void setLastTimeStamp(final String lastTimeStamp) {

        this.lastTimeStamp = lastTimeStamp;
    }

    public CustomWebsocketClient getCustomWebsocketClient() {

        if (this.customWebsocketClient == null) {

            throw new IllegalStateException("CustomWebsocketClient is null");
        }

        return this.customWebsocketClient;
    }

    public void setCustomWebsocketClient(final CustomWebsocketClient customWebsocketClient) {

        this.customWebsocketClient = customWebsocketClient;
    }

    private boolean startup = false;

    private ClientRegister() {

    }

    public static ClientRegister getWebSocketClientInstance() {

        return CLIENT_INSTANCE;
    }

    public String getLastMessageSenderName() {

        return lastSenderName;
    }

    public void setLastMessageSenderName(final String lastMessageSenderName) {

        lastSenderName = lastMessageSenderName;
    }

    public String getLastMessageTimeStamp() {

        return lastTimeStamp;
    }

    public void setLastMessageTimeStamp(final String lastMessageTimeStamp) {

        lastTimeStamp = lastMessageTimeStamp;
    }

    public boolean isStartup() {

        return startup;
    }

    public void setStartup(final boolean startup) {

        this.startup = startup;
    }
}