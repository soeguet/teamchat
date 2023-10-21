package com.soeguet.properties;

public class CustomUserProperties {

    private String username;
    private String nickname;
    private String borderColor;

    /**
     Creates a new instance of the CustomUserProperties class.
     */
    public CustomUserProperties() {

    }

    /**
     Constructs an instance of the CustomUserProperties class with the specified username.

     @param username the username of the user
     */
    public CustomUserProperties(String username) {

        this.username = username;
    }

    /**
     Gets the username of the user.

     @return the username of the user as a string.
     */
    public String getUsername() {

        return username;
    }

    /**
     Sets the username of the user.

     @param username the new username to be set for the user.
     */
    public void setUsername(String username) {

        this.username = username;
    }

    /**
     Returns the nickname of the user.

     @return the nickname of the user.
     */
    public String getNickname() {

        return nickname;
    }

    /**
     Sets the nickname of the user.

     @param nickname the nickname to be set.
     */
    public void setNickname(String nickname) {

        this.nickname = nickname;
    }
}