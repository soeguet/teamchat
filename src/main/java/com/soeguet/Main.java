package com.soeguet;

import com.soeguet.initialization.ProgramInit;
import com.soeguet.initialization.enums.Themes;
import com.soeguet.initialization.implementations.EnvDataProviderImpl;
import com.soeguet.initialization.implementations.UserInteractionImpl;
import com.soeguet.initialization.themes.implementations.ThemeManagerImpl;
import com.soeguet.initialization.themes.implementations.ThemeSetterImpl;
import com.soeguet.model.EnvVariables;

public class Main {

    public static void main(String... args) {

        final ThemeSetterImpl themeSetterImpl = new ThemeSetterImpl();
        final ThemeManagerImpl themeManagerImpl = new ThemeManagerImpl(themeSetterImpl);
        final UserInteractionImpl userInteractionImpl = new UserInteractionImpl();
        final EnvDataProviderImpl envDataProvider = new EnvDataProviderImpl();

        ProgramInit programInit = new ProgramInit(envDataProvider, userInteractionImpl, themeManagerImpl);
        final EnvVariables environmentVariables = programInit.collectEnvVariables();
        programInit.setTheme(Themes.INTELLIJ);
        programInit.initializeGUI(environmentVariables);
    }
}