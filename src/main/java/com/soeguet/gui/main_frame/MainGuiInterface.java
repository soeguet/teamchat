package com.soeguet.gui.main_frame;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.socket_client.CustomWebsocketClient;

import javax.swing.*;

public interface MainGuiInterface {

    JTextPane getTextEditorPane();

    CustomWebsocketClient getWebsocketClient();

    JPanel getMainTextPanel();

    JScrollPane getMainTextBackgroundScrollPane();

    ObjectMapper getObjectMapper();
}
