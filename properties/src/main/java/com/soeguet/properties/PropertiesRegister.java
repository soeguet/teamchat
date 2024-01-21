package com.soeguet.properties;

public class PropertiesRegister {

    private static final PropertiesRegister CUSTOM_USER_PROPERTIES_INSTANCE = new PropertiesRegister();
    private String username;
    private String nickname;
    private String borderColor;

    /** Creates a new instance of the CustomUserProperties class. */
    private PropertiesRegister() {}

    public static PropertiesRegister getCustomUserPropertiesInstance() {

        return CUSTOM_USER_PROPERTIES_INSTANCE;
    }

    /**
     * Constructs an instance of the CustomUserProperties class with the specified timeAndUsername.
     *
     * @param username the timeAndUsername of the user
     */
    public PropertiesRegister(String username) {

        this.username = username;
    }

    /**
     * Gets the timeAndUsername of the user.
     *
     * @return the timeAndUsername of the user as a string.
     */
    public String getUsername() {

        return username;
    }

    /**
     * Sets the timeAndUsername of the user.
     *
     * @param username the new timeAndUsername to be set for the user.
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

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }
}