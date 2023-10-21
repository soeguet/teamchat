package com.soeguet;

import com.soeguet.initialization.ProgramInit;
import com.soeguet.initialization.enums.Themes;
import com.soeguet.initialization.implementations.EnvDataProviderImpl;
import com.soeguet.initialization.implementations.EnvVarHandler;
import com.soeguet.initialization.implementations.UserInteractionImpl;
import com.soeguet.initialization.interfaces.EnvDataProviderInterface;
import com.soeguet.initialization.interfaces.EnvVarHandlerInterface;
import com.soeguet.initialization.interfaces.UserInteractionInterface;
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

        final UserInteractionInterface userInteraction = new UserInteractionImpl();
        final EnvDataProviderInterface envDataProvider = new EnvDataProviderImpl();
        EnvVarHandlerInterface envVarHandler = new EnvVarHandler(envDataProvider, userInteraction);
        final EnvVariables environmentVariables = envVarHandler.collectEnvVariables();

        //initialize program and pass dependencies
        ProgramInit programInit = new ProgramInit( themeManager);

        //FEATURE make themes configurable
        programInit.setTheme(Themes.INTELLIJ);
        programInit.initializeGUI(environmentVariables);
    }
}