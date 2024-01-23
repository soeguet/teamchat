package com.soeguet.initialization.themes.implementations;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.soeguet.initialization.themes.interfaces.ThemeSetter;

/**
 * Concrete implementation of the {@link ThemeSetter} interface. This class provides methods to set
 * different themes for the application using various look and feel libraries.
 */
public class ThemeSetterImpl implements ThemeSetter {

  /** Sets the light theme for the application using FlatLightLaf. */
  @Override
  public void setLight() {

    FlatLightLaf.setup();
  }

  /** Sets the dark theme for the application using FlatDarkLaf. */
  @Override
  public void setDark() {

    FlatDarkLaf.setup();
  }

  /** Sets the Darcula theme for the application using FlatDarculaLaf. */
  @Override
  public void setDarcula() {

    FlatDarculaLaf.setup();
  }

  /** Sets the IntelliJ theme for the application using FlatIntelliJLaf. */
  @Override
  public void setIntelliJ() {

    FlatIntelliJLaf.setup();
  }
}
