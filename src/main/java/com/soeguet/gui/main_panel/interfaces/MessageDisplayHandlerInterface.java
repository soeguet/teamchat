package com.soeguet.gui.main_panel.interfaces;

import com.soeguet.model.jackson.BaseModel;

public interface MessageDisplayHandlerInterface {

    String pollMessageFromCache();

    void processAndDisplayMessage(BaseModel baseModel, String nickname);
}