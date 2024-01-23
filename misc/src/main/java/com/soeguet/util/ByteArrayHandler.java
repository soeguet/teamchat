package com.soeguet.util;

import com.soeguet.util.interfaces.ByteArrayHandlerInterface;
import java.util.List;

public class ByteArrayHandler implements ByteArrayHandlerInterface {

  /**
   * Converts a list of strings to a byte array representation.
   *
   * @param selectedClients the list of strings to convert
   * @return the byte array representing the converted list
   */
  @Override
  public byte[] convertListToByteArray(final List<String> selectedClients) {

    final StringBuilder stringBuilder = formatSelectedClientsToString(selectedClients);

    String interruptRequest = "{\"type\":\"interrupt\",\"usernames\":" + stringBuilder + "}";

    return interruptRequest.getBytes();
  }

  /**
   * Formats a list of strings into a single StringBuilder object representing a formatted JSON
   * array.
   *
   * @param selectedClients the list of strings to format
   * @return the StringBuilder object containing the formatted JSON array
   */
  private StringBuilder formatSelectedClientsToString(final List<String> selectedClients) {

    StringBuilder stringBuilder = new StringBuilder();

    stringBuilder.append("[");

    selectedClients.forEach(
        client -> {
          stringBuilder.append("\"");
          stringBuilder.append(client);
          stringBuilder.append("\"");

          if (selectedClients.indexOf(client) != selectedClients.size() - 1) {
            stringBuilder.append(",");
          }
        });

    stringBuilder.append("]");
    return stringBuilder;
  }
}
