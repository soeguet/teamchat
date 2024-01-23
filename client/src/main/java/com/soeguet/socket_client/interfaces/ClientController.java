package com.soeguet.socket_client.interfaces;

import com.soeguet.socket_client.CustomWebsocketClient;

public interface ClientController {

  void determineWebsocketURI();

  void connectToWebsocket();

  void serverInformationOptionPane();

  void closeConnection();

  void prepareReconnection();

  CustomWebsocketClient getWebsocketClient();
}
