package com.soeguet.gui.interrupt_dialog.interfaces;

import java.awt.Component;
import java.util.HashMap;

import com.soeguet.properties.dto.CustomUserPropertiesDTO;

public interface InterruptDialogInterface {

    void populateDialogWithAllRegisteredClients(HashMap<String, CustomUserPropertiesDTO> clientPropertiesMap);

    void pack();

    void setVisible(boolean b);

    void setLocationRelativeTo(Component component);
}
