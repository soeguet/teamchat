package com.soeguet.behaviour;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.behaviour.interfaces.GuiFunctionalityInterface;
import com.soeguet.gui.main_frame.ChatMainFrameImpl;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.model.MessageTypes;
import com.soeguet.model.jackson.MessageModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class GuiFunctionalityTest {

    @Test
    void convertUserTextToJSON() throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();

        MainFrameGuiInterface mainFrame = Mockito.mock(ChatMainFrameImpl.class);
        GuiFunctionalityInterface guiFunctionality = new GuiFunctionalityImpl(mainFrame);

        Mockito.when(mainFrame.getObjectMapper()).thenReturn(objectMapper);
        Mockito.when(mainFrame.getUsername()).thenReturn("test");

        MessageModel messageModel = new MessageModel();
        messageModel.setMessage("test");
        messageModel.setSender("test");
        messageModel.setMessageType(MessageTypes.NORMAL);

        String expected = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(messageModel);

        Assertions.assertEquals(expected, guiFunctionality.convertUserTextToJSON(messageModel));
    }
}