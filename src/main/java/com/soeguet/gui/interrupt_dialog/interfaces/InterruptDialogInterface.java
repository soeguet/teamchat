package com.soeguet.gui.interrupt_dialog.interfaces;

import com.soeguet.properties.CustomUserProperties;

import java.util.HashMap;

public interface InterruptDialogInterface {

    void populateDialogWithAllRegisteredClients(HashMap<String, CustomUserProperties> clientPropertiesMap);

    void pack();

    void setVisible(boolean b);
}