package com.soeguet;

import com.soeguet.initialization.ProgramInit;
import com.soeguet.initialization.enums.Themes;
import com.soeguet.initialization.implementations.EnvDataProviderImpl;
import com.soeguet.initialization.implementations.UserInteractionImpl;
import com.soeguet.initialization.interfaces.EnvDataProvider;
import com.soeguet.initialization.interfaces.UserInteraction;
import com.soeguet.initialization.themes.implementations.ThemeManagerImpl;
import com.soeguet.initialization.themes.implementations.ThemeSetterImpl;
import com.soeguet.initialization.themes.interfaces.ThemeManager;
import com.soeguet.initialization.themes.interfaces.ThemeSetter;
import com.soeguet.model.EnvVariables;

public class Main {

    public static void main(String... args) {

        //set up dependencies
        final ThemeSetter themeSetter = new ThemeSetterImpl();
        final ThemeManager themeManager = new ThemeManagerImpl(themeSetter);
        final UserInteraction userInteraction = new UserInteractionImpl();
        final EnvDataProvider envDataProvider = new EnvDataProviderImpl();

        //initialize program and pass dependencies
        ProgramInit programInit = new ProgramInit(envDataProvider, userInteraction, themeManager);
        final EnvVariables environmentVariables = programInit.collectEnvVariables();

        //FEATURE make themes configurable
        programInit.setTheme(Themes.INTELLIJ);
        programInit.initializeGUI(environmentVariables);
    }
}