package com.soeguet.initialization.implementations;

import com.soeguet.initialization.enums.Themes;
import com.soeguet.initialization.themes.implementations.ThemeManagerImpl;
import com.soeguet.initialization.themes.interfaces.ThemeSetter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ThemeManagerImplTest {

    private ThemeSetter mockThemeSetter;
    private ThemeManagerImpl themeManager;

    @BeforeEach
    public void setUp() {
        mockThemeSetter = Mockito.mock(ThemeSetter.class);
        themeManager = new ThemeManagerImpl(mockThemeSetter);
    }

    @Test
    public void testApplyThemeLight() {
        themeManager.applyTheme(Themes.LIGHT);
        Mockito.verify(mockThemeSetter, Mockito.times(1)).setLight();
    }

    @Test
    public void testApplyThemeDark() {
        themeManager.applyTheme(Themes.DARK);
        Mockito.verify(mockThemeSetter, Mockito.times(1)).setDark();
    }

    @Test
    public void testApplyThemeDarcula() {
        themeManager.applyTheme(Themes.DARCULA);
        Mockito.verify(mockThemeSetter, Mockito.times(1)).setDarcula();
    }

    @Test
    public void testApplyThemeDefault() {
        themeManager.applyTheme(Themes.INTELLIJ);
        Mockito.verify(mockThemeSetter, Mockito.times(1)).setIntelliJ();
    }
}
