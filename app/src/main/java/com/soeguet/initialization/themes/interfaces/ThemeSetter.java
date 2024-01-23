package com.soeguet.initialization.themes.interfaces;

/**
 * The ThemeSetter interface defines the contract for setting different themes. It provides methods
 * to set the light, dark, Darcula, and IntelliJ themes.
 */
public interface ThemeSetter {

  /** Sets the dark to the "Light" theme. */
  void setLight();

  /** Sets the dark to the "Dark" theme. */
  void setDark();

  /** Sets the dark to the "Darcula" theme. */
  void setDarcula();

  /** Sets the theme to the default "IntelliJ" theme. */
  void setIntelliJ();
}
