package com.soeguet.main_panel.interfaces;

import com.soeguet.cache.manager.CacheManager;
import com.soeguet.model.jackson.BaseModel;

public interface MessageDisplayHandlerInterface {

  String pollMessageFromCache();

  void processAndDisplayMessage(BaseModel baseModel);

  void setCacheManager(CacheManager cacheManager);

  void updateExistingMessage(BaseModel baseModel);
}
