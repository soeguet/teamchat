package com.soeguet.interrupt_dialog.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.interrupt_dialog.interfaces.InterruptHandlerInterface;
import com.soeguet.main_frame.ChatMainFrameImpl;
import com.soeguet.properties.PropertiesRegister;
import java.io.IOException;
import javax.swing.*;

public class InterruptHandler implements InterruptHandlerInterface {

  /** Initializes an InterruptHandler object with the provided MainFrameInterface. */
  public InterruptHandler() {}

  /**
   * Force the chat GUI to the front and perform a visual effect.
   *
   * @param clientUserName the timeAndUsername to check if it matches the current timeAndUsername of
   *     the main frame
   */
  @Override
  public void forceChatGuiToFront(final String clientUserName) {

    if (clientUserName.equals(PropertiesRegister.getPropertiesInstance().getUsername())) {

      ChatMainFrameImpl chatMainFrame = ChatMainFrameImpl.getMainFrameInstance();

      chatMainFrame.setAlwaysOnTop(true);
      chatMainFrame.toFront();

      SwingUtilities.invokeLater(
          () -> {
            try {

              Thread.sleep(100);
              chatMainFrame.setLocation(
                  chatMainFrame.getLocation().x, chatMainFrame.getLocation().y + 100);
              Thread.sleep(100);
              chatMainFrame.setLocation(
                  chatMainFrame.getLocation().x + 100, chatMainFrame.getLocation().y);
              Thread.sleep(100);
              chatMainFrame.setLocation(
                  chatMainFrame.getLocation().x, chatMainFrame.getLocation().y - 100);
              Thread.sleep(100);
              chatMainFrame.setLocation(
                  chatMainFrame.getLocation().x - 100, chatMainFrame.getLocation().y);
              Thread.sleep(100);

              chatMainFrame.setLocationRelativeTo(null);

            } catch (InterruptedException e) {

              throw new RuntimeException(e);
            }
          });

      chatMainFrame.requestFocus();
      chatMainFrame.setAlwaysOnTop(false);
    }
  }

  /**
   * Extracts the usernames from the provided JSON message.
   *
   * @param message the byte array containing the JSON message
   * @return the JSON node containing the usernames
   */
  @Override
  public JsonNode extractJsonNodeUsernames(final byte[] message) {

    try {

      ObjectMapper objectMapper = new ObjectMapper();
      final JsonNode jsonNode = objectMapper.readTree(message);

      return jsonNode.get("usernames");

    } catch (IOException e) {

      throw new RuntimeException(e);
    }
  }
}
