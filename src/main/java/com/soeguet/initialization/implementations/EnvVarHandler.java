package com.soeguet.initialization.implementations;

import com.soeguet.initialization.interfaces.EnvDataProviderInterface;
import com.soeguet.initialization.interfaces.EnvVarHandlerInterface;
import com.soeguet.initialization.interfaces.UserInteractionInterface;
import com.soeguet.model.EnvVariables;

public class EnvVarHandler implements EnvVarHandlerInterface {

    private final EnvDataProviderInterface envDataProvider;
    private final UserInteractionInterface userInteraction;

    public EnvVarHandler(final EnvDataProviderInterface envDataProvider, final UserInteractionInterface userInteraction) {

        this.envDataProvider = envDataProvider;
        this.userInteraction = userInteraction;
    }

    @Override
    public EnvVariables collectEnvVariables() {

        //timeAndUsername
        final String username = retrieveUsername();
        validateUsernameInput(username);

        //chat ip and port
        final String chatIp = envDataProvider.getEnvData("CHAT_IP");
        final String chatPort = envDataProvider.getEnvData("CHAT_PORT");

        return new EnvVariables(username, chatIp, chatPort);
    }


    private String retrieveUsername() {

        String chatUsername = envDataProvider.getEnvData("CHAT_USERNAME");

        return (chatUsername != null && !chatUsername.isEmpty()) ? chatUsername : userInteraction.askForUsername();
    }

    private void validateUsernameInput(String username) {

        if (username == null || username.isEmpty()) {

            userInteraction.showError("Username must not be empty");
        }
    }
}