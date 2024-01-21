package com.soeguet.dtos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.model.jackson.BaseModel;

public record ReactionPanelDTO(
        BaseModel baseModel,
        String username,
        ObjectMapper objectMapper) {}