package com.soeguet.properties.interfaces;

public interface CustomPropertiesInterface {

    void checkIfConfigFileExists();

    void loadProperties();

    void populateHashMapWithNewValues();
}