package com.soeguet.properties.interfaces;

import com.soeguet.properties.dto.CustomUserPropertiesDTO;

public interface CustomPropertiesInterface {

    boolean checkIfConfigFileExists();

    void loadProperties();

    void populateHashMapWithNewValues();

    void saveProperties();

    void addCustomerToHashSet(final CustomUserPropertiesDTO customUserProperties);

    CustomUserPropertiesDTO getCustomUserProperties(final String username);
}
