package com.soeguet.initialization.implementations;

import com.soeguet.initialization.interfaces.EnvDataProviderInterface;

/** Implementation of the EnvDataProvider interface for retrieving environment variable values. */
public class EnvDataProviderImpl implements EnvDataProviderInterface {

    /**
     * Retrieves the value of the specified environment variable.
     *
     * @param key the name of the environment variable to retrieve
     * @return the value of the specified environment variable, or null if it does not exist
     */
    @Override
    public String getEnvData(final String key) {

        return System.getenv(key);
    }
}
