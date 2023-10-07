package com.soeguet.initialization.themes.interfaces;

import com.soeguet.initialization.enums.Themes;

/**
 * Interface for managing the theme settings of the UI.
 */
public interface ThemeManager {

    /**
     * Applies the specified theme setting to the UI.
     *
     * @param themeSetting the theme to be applied
     */
    void applyTheme(Themes themeSetting);
}