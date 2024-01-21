package com.soeguet.interrupt_dialog.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.interrupt_dialog.interfaces.InterruptHandlerInterface;
import com.soeguet.main_frame.ChatMainFrameImpl;
import com.soeguet.properties.PropertiesRegister;

import javax.swing.*;
import java.io.IOException;

public class InterruptHandler implements InterruptHandlerInterface {


    /**
     * Initializes an InterruptHandler object with the provided MainFrameInterface.
     */
    public InterruptHandler() {

    }

    /**
     * Force the chat GUI to the front and perform a visual effect.
     *
     * @param clientUserName the timeAndUsername to check if it matches the current timeAndUsername
     *     of the main frame
     */
    @Override
    public void forceChatGuiToFront(final String clientUserName) {

        if (clientUserName.equals(PropertiesRegister.getCustomUserPropertiesInstance().getUsername())) {

            ChatMainFrameImpl chatMainFrame = ChatMainFrameImpl.getMainFrameInstance();

            if (chatMainFrame instanceof JFrame gui) {

                gui.setAlwaysOnTop(true);
                gui.toFront();

                SwingUtilities.invokeLater(
                        () -> {
                            try {

                                Thread.sleep(100);
                                gui.setLocation(gui.getLocation().x, gui.getLocation().y + 100);
                                Thread.sleep(100);
                                gui.setLocation(gui.getLocation().x + 100, gui.getLocation().y);
                                Thread.sleep(100);
                                gui.setLocation(gui.getLocation().x, gui.getLocation().y - 100);
                                Thread.sleep(100);
                                gui.setLocation(gui.getLocation().x - 100, gui.getLocation().y);
                                Thread.sleep(100);

                                gui.setLocationRelativeTo(null);

                            } catch (InterruptedException e) {

                                throw new RuntimeException(e);
                            }
                        });

                gui.requestFocus();
                gui.setAlwaysOnTop(false);
            }
        }
    }

    /**
     Extracts the usernames from the provided JSON message.

     @param message
     the byte array containing the JSON message

     @return the JSON node containing the usernames
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