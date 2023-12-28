package com.soeguet;

import com.soeguet.gui.main_frame.ChatMainFrameImpl;
import com.soeguet.initialization.ProgramInit;
import com.soeguet.initialization.enums.Themes;
import com.soeguet.initialization.implementations.EnvDataProviderImpl;
import com.soeguet.initialization.implementations.EnvVarHandler;
import com.soeguet.initialization.implementations.UserInteractionImpl;
import com.soeguet.initialization.interfaces.EnvDataProviderInterface;
import com.soeguet.initialization.interfaces.EnvVarHandlerInterface;
import com.soeguet.initialization.interfaces.MainFrameInitInterface;
import com.soeguet.initialization.interfaces.UserInteractionInterface;
import com.soeguet.initialization.themes.implementations.ThemeManagerImpl;
import com.soeguet.initialization.themes.implementations.ThemeSetterImpl;
import com.soeguet.initialization.themes.interfaces.ThemeManager;
import com.soeguet.initialization.themes.interfaces.ThemeSetter;
import com.soeguet.model.EnvVariables;

public class Main {

    public static void main(String... args) {

        // set up dependencies
        final ThemeSetter themeSetter = new ThemeSetterImpl();
        final ThemeManager themeManager = new ThemeManagerImpl(themeSetter);
        // FEATURE make themes configurable
        themeManager.applyTheme(Themes.INTELLIJ);

        final UserInteractionInterface userInteraction = new UserInteractionImpl();
        final EnvDataProviderInterface envDataProvider = new EnvDataProviderImpl();
        final EnvVarHandlerInterface envVarHandler =
                new EnvVarHandler(envDataProvider, userInteraction);

        // collect environment variables from system or user input
        final EnvVariables environmentVariables = envVarHandler.collectEnvVariables();

        // initialize program and pass dependencies
        final MainFrameInitInterface mainFrame = new ChatMainFrameImpl();
        final ProgramInit programInit = new ProgramInit(mainFrame);

        programInit.initializeGUI(environmentVariables);
    }
}
