package com.soeguet.properties;

public class CustomUserProperties {

    private String username;
    private String nickname;
    private String borderColor;

    /**
     * Creates a new instance of the CustomUserProperties class.
     */
    public CustomUserProperties() {

    }

    /**
     * Constructs an instance of the CustomUserProperties class with the specified username.
     *
     * @param username the username of the user
     */
    public CustomUserProperties(String username) {

        this.username = username;
    }

    /**
     * Returns the border color as an integer value.
     *
     * @return the border color as an integer value. If the border color is null, returns -1.
     */
    public int getBorderColor() {

        return borderColor == null ? -1 : Integer.parseInt(borderColor);
    }

    /**
     * Sets the border color to the specified integer value.
     *
     * @param borderColor the border color to set as an integer value.
     */
    public void setBorderColor(int borderColor) {

        this.borderColor = String.valueOf(borderColor);
    }

    /**
     * Gets the username of the user.
     *
     * @return the username of the user as a string.
     */
    public String getUsername() {

        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username the new username to be set for the user.
     */
    public void setUsername(String username) {

        this.username = username;
    }

    /**
     * Returns the nickname of the user.
     *
     * @return the nickname of the user.
     */
    public String getNickname() {

        return nickname;
    }

    /**
     * Sets the nickname of the user.
     *
     * @param nickname the nickname to be set.
     */
    public void setNickname(String nickname) {

        this.nickname = nickname;
    }
}
