package com.soeguet.gui.interrupt_dialog.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.soeguet.gui.interrupt_dialog.interfaces.InterruptHandlerInterface;
import com.soeguet.gui.main_frame.interfaces.MainFrameInterface;

import javax.swing.*;
import java.io.IOException;

public class InterruptHandler implements InterruptHandlerInterface {

    private final MainFrameInterface mainFrame;

    public InterruptHandler(final MainFrameInterface mainFrame) {

        this.mainFrame = mainFrame;
    }

    /**
     * Force the chat GUI to the front and perform a visual effect.
     *
     * @param clientUserName the username to check if it matches the current username of the main frame
     */
    @Override
    public void forceChatGuiToFront(final String clientUserName) {

        if (clientUserName.equals(mainFrame.getUsername())) {

            if (mainFrame instanceof JFrame gui) {

                gui.setAlwaysOnTop(true);
                gui.toFront();

                SwingUtilities.invokeLater(() -> {

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
     * Extracts the usernames from the provided JSON message.
     *
     * @param message the byte array containing the JSON message
     * @return the JSON node containing the usernames
     */
    @Override
    public JsonNode extractJsonNodeUserNames(final byte[] message) {

        try {

            final JsonNode jsonNode = mainFrame.getObjectMapper().readTree(message);

            return jsonNode.get("usernames");

        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }
}