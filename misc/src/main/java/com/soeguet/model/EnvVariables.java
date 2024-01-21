package com.soeguet.model;

public class EnvVariables {

    String chatUsername;
    String chatIp;
    String chatPort;

    public EnvVariables() {}

    public EnvVariables(final String chatUsername) {

        this.chatUsername = chatUsername;
    }

    public EnvVariables(final String chatUsername, final String chatIp, final String chatPort) {

        this.chatUsername = chatUsername;
        this.chatIp = chatIp;
        this.chatPort = chatPort;
    }

    public String getChatUsername() {

        return chatUsername;
    }

    public void setChatUsername(final String chatUsername) {

        this.chatUsername = chatUsername;
    }

    public String getChatIp() {

        return chatIp == null ? "" : chatIp;
    }

    public void setChatIp(final String chatIp) {

        this.chatIp = chatIp;
    }

    public String getChatPort() {

        return chatPort == null ? "" : chatPort;
    }

    public void setChatPort(final String chatPort) {

        this.chatPort = chatPort;
    }

    @Override
    public String toString() {

        return "EnvVariables{"
                + "chatUsername='"
                + chatUsername
                + '\''
                + ", chatIp='"
                + chatIp
                + '\''
                + ", chatPort='"
                + chatPort
                + '\''
                + '}';
    }
}
