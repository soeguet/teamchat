package com.soeguet.gui.main_frame;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.gui.behaviour.GuiFunctionality;
import com.soeguet.socket_client.CustomWebsocketClient;

import javax.swing.*;

public interface MainGuiElementsInterface {

    JTextPane getTextEditorPane();

    CustomWebsocketClient getWebsocketClient();

    JPanel getMainTextPanel();

    JScrollPane getMainTextBackgroundScrollPane();

    ObjectMapper getObjectMapper();

    GuiFunctionality getGuiFunctionality();
}
