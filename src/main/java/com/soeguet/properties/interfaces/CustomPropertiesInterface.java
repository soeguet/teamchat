package com.soeguet.properties.interfaces;

import com.soeguet.properties.CustomUserProperties;

public interface CustomPropertiesInterface {

    boolean checkIfConfigFileExists();

    void loadProperties();

    void populateHashMapWithNewValues();

    void saveProperties();

    void addCustomerToHashSet(final CustomUserProperties customUserProperties);

    CustomUserProperties getCustomUserProperties(final String username);
}