package com.soeguet.properties;

public class PropertiesRegister {

  // variables -- start
  private static final PropertiesRegister CUSTOM_USER_PROPERTIES_INSTANCE =
      new PropertiesRegister();
  private String username;
  private String nickname;
  private String borderColor;
  // variables -- end

  // constructors -- start

  /** Creates a new instance of the CustomUserProperties class. */
  private PropertiesRegister() {}

  public boolean checkIfUsernameMatches(String username) {

    return this.username.equals(username);
  }

  /**
   * Constructs an instance of the CustomUserProperties class with the specified timeAndUsername.
   *
   * @param username the timeAndUsername of the user
   */
  public PropertiesRegister(String username) {

    this.username = username;
  }
  // constructors -- end

  // getter & setter -- start
  public String getBorderColor() {

    return borderColor;
  }

  public void setBorderColor(String borderColor) {

    this.borderColor = borderColor;
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

  public static PropertiesRegister getPropertiesInstance() {

    return CUSTOM_USER_PROPERTIES_INSTANCE;
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
  // getter & setter -- end
}
