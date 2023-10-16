package com.soeguet.gui.interrupt_dialog.interfaces;

import com.soeguet.gui.main_frame.ChatMainFrameImpl;
import com.soeguet.properties.CustomUserProperties;

import java.awt.*;
import java.util.HashMap;

public interface InterruptDialogInterface {

    void populateDialogWithAllRegisteredClients(HashMap<String, CustomUserProperties> clientPropertiesMap);

    void pack();

    void setVisible(boolean b);

    void setLocationRelativeTo(Component component);
}