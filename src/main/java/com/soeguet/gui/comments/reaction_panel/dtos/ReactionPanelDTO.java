package com.soeguet.gui.comments.reaction_panel.dtos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.socket_client.CustomWebsocketClient;
import com.soeguet.socket_client.interfaces.ClientController;

public record ReactionPanelDTO(Long id, String clientName, CustomWebsocketClient websocketClient, ObjectMapper objectMapper) {}