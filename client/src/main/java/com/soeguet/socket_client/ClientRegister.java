package com.soeguet.socket_client;

public class ClientRegister {

    private static final ClientRegister CLIENT_INSTANCE = new ClientRegister();

    /** The name of the client which was last posted on the main panel. */
    private  String lastSenderName;

    /** The last time someone posted on the main panel. */
    private  String lastTimeStamp;

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