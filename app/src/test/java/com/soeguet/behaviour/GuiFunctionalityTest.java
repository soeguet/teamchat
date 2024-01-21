package com.soeguet.behaviour;

class GuiFunctionalityTest {

//    @Test
//    void convertUserTextToJSON() throws JsonProcessingException {
//
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        MainFrameGuiInterface mainFrame = Mockito.mock(ChatMainFrameImpl.class);
//        GuiFunctionalityInterface guiFunctionality = new GuiFunctionalityImpl(mainFrame);
//
//        Mockito.when(mainFrame.getObjectMapper()).thenReturn(objectMapper);
//        Mockito.when(mainFrame.getUsername()).thenReturn("test");
//
//        MessageModel messageModel = new MessageModel();
//        messageModel.setMessage("test");
//        messageModel.setSender("test");
//        messageModel.setMessageType(MessageTypes.NORMAL);
//
//        String expected =
//                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(messageModel);
//
//        Assertions.assertEquals(expected, guiFunctionality.convertUserTextToJSON(messageModel));
//    }
}