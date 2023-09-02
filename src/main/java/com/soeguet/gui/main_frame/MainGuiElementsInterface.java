package com.soeguet.gui.main_frame;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.behaviour.GuiFunctionality;
import com.soeguet.socket_client.CustomWebsocketClient;

import javax.swing.*;

public interface MainGuiElementsInterface {

    JTextPane getTextEditorPane();

    CustomWebsocketClient getWebsocketClient();

    /**
     * Returns the main text panel.
     *
     * @return the JPanel representing the main text panel, on which chat bubbles are displayed
     */
    JPanel getMainTextPanel();

    JScrollPane getMainTextBackgroundScrollPane();

    ObjectMapper getObjectMapper();

    GuiFunctionality getGuiFunctionality();
}
