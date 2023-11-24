package com.soeguet.gui.comments.reaction_panel.dtos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.socket_client.CustomWebsocketClient;

public record ReactionPanelDTO(BaseModel baseModel, String username, CustomWebsocketClient websocketClient, ObjectMapper objectMapper) {}