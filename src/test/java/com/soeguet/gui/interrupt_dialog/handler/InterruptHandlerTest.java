package com.soeguet.gui.interrupt_dialog.handler;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;

class InterruptHandlerTest {

    @Test
    void extractJsonNodeUsernames() {

        InterruptHandler interruptHandler = new InterruptHandler(null);

        String json = "{\"usernames\":[\"user1\",\"user2\"]}";

        final JsonNode jsonNode = interruptHandler.extractJsonNodeUsernames(json.getBytes());

        assertEquals("user1", jsonNode.get(0).asText());
        assertEquals("user2", jsonNode.get(1).asText());
    }
}
