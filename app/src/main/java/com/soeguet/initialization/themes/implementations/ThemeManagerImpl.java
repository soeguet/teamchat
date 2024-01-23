package com.soeguet.initialization.themes.implementations;

import com.soeguet.initialization.enums.Themes;
import com.soeguet.initialization.themes.interfaces.ThemeManager;
import com.soeguet.initialization.themes.interfaces.ThemeSetter;

/** An implementation of the ThemeManager interface that applies theme settings to a ThemeSetter. */
public class ThemeManagerImpl implements ThemeManager {

  private final ThemeSetter themeSetter;

  /**
   * Creates a new ThemeManagerImpl with the specified ThemeSetter.
   *
   * @param themeSetter the ThemeSetter to be used by the ThemeManagerImpl
   */
  public ThemeManagerImpl(final ThemeSetter themeSetter) {

    this.themeSetter = themeSetter;
  }

  /**
   * Applies the specified theme setting to the ThemeSetter.
   *
   * @param themeSetting the theme setting to be applied
   * @throws IllegalArgumentException if themeSetting is null
   */
  @Override
  public void applyTheme(final Themes themeSetting) {

    if (themeSetting == null) {
      throw new IllegalArgumentException("Theme setting must not be null");
    }

    switch (themeSetting.name()) {
      case "LIGHT" -> themeSetter.setLight();

      case "DARK" -> themeSetter.setDark();

      case "DARCULA" -> themeSetter.setDarcula();

      default -> themeSetter.setIntelliJ();
    }
  }
}
