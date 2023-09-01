package com.soeguet.gui.main_frame;


import com.soeguet.gui.behaviour.GuiFunctionality;
import com.soeguet.gui.main_frame.generated.ChatPanel;
import com.soeguet.socket_client.CustomWebsocketClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.EventObject;
import java.util.logging.Logger;

/**
 * This class represents a GUI implementation for a chat application.
 * It extends the ChatPanel class and implements the MainGuiInterface.
 */
public class ChatMainGuiImpl extends ChatPanel implements MainGuiInterface {

    private final Logger logger = Logger.getLogger(ChatMainGuiImpl.class.getName());

    private final GuiFunctionality guiFunctionality;

    private final CustomWebsocketClient websocketClient;

    public ChatMainGuiImpl() {

        guiFunctionality = new GuiFunctionality(this);

        try {
            websocketClient = new CustomWebsocketClient(new URI("ws://127.0.0.1:8100"));
            websocketClient.connect();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the WebSocket client.
     *
     * @return The WebSocket client.
     */
    public CustomWebsocketClient getWebsocketClient() {

        return websocketClient;
    }

    /**
     * Logs the provided event and method name.
     *
     * @param event The event to be logged.
     * @param methodName The name of the method to be logged.
     */
    private void logMethod(EventObject event, String methodName) {

        System.out.println();
        System.out.println("__ " + methodName);
        logger.info(event.toString());
        System.out.println();
    }

    @Override
    protected void thisPropertyChange(PropertyChangeEvent e) {

        logMethod(e, "ChatGuiImpl.thisPropertyChange");
    }

    @Override
    protected void thisComponentResized(ComponentEvent e) {

        //        logMethod(e,"ChatGuiImpl.thisComponentResized");
    }

    @Override
    protected void propertiesMenuItemMousePressed(MouseEvent e) {

        logMethod(e, "ChatGuiImpl.propertiesMenuItemMousePressed");
    }

    @Override
    protected void resetConnectionMenuItemMousePressed(MouseEvent e) {

        logMethod(e, "ChatGuiImp.resetConnectionMenuItemMousePressed");
    }

    @Override
    protected void participantsMenuItemMousePressed(MouseEvent e) {

        logMethod(e, "ChatGuiImpl.participantsMenuItemMousePressed");
    }

    @Override
    protected void mainTextPanelMouseClicked(MouseEvent e) {

        logMethod(e, "ChatGuiImpl.mainTextPanelMouseClicked");
    }

    @Override
    protected void textEditorPaneMouseClicked(MouseEvent e) {

        logMethod(e, "ChatGuiImpl.textEditorPaneMouseClicked");
    }

    /**
     * Called when a key is pressed in the text editor pane.
     * If the pressed key is not the Enter key, the method simply returns.
     * If the pressed key is the Enter key, it consumes the event and performs the appropriate action based on whether the Shift key is pressed or not.
     *
     * @param e The KeyEvent object representing the key press event.
     */
    @Override
    protected void textEditorPaneKeyPressed(KeyEvent e) {

        if (e.getKeyCode() != KeyEvent.VK_ENTER) {
            return;
        }

        e.consume();

        if (e.isShiftDown()) {
            appendNewLineToTextEditorPane();
            return;
        }

        handleNonShiftEnterKeyPress();
    }

    /**
     * Appends a new line to the text editor pane.
     *
     * Retrieves the current text in the text editor pane and appends a new line character at the end of it.
     *
     */
    private void appendNewLineToTextEditorPane() {

        String currentText = getTextEditorPane().getText();
        getTextEditorPane().setText(currentText + "\n");
    }

    /**
     * Handles a key press event when the enter key is pressed without pressing the shift key.
     *
     * Retrieves the content of the text editor pane, trims any leading or trailing space, and checks if it is empty.
     * If the content is empty, it clears the text editor pane. Otherwise, it calls the `clearTextPaneAndSendMessageToSocket`
     * method to clear the text pane and send the current content to a socket.
     *
     */
    private void handleNonShiftEnterKeyPress() {

        String textPaneContent = getTextEditorPane().getText().trim();
        if (textPaneContent.isEmpty()) {
            getTextEditorPane().setText("");
        } else {
            guiFunctionality.clearTextPaneAndSendMessageToSocket();
        }
    }


    @Override
    protected void textEditorPaneKeyReleased(KeyEvent e) {

    }

    @Override
    protected void emojiButton(ActionEvent e) {

        logMethod(e, "ChatGuiImpl.emojiButton");
    }

    /**
     * Handles the event when the mouse presses the exit menu item.
     * Sets the default close operation for the current JFrame to EXIT_ON_CLOSE
     * and disposes the current JFrame.
     *
     * @param e the MouseEvent object that triggered this event
     */
    @Override
    protected void exitMenuItemMousePressed(MouseEvent e) {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.dispose();
        System.exit(0);
    }

    @Override
    protected void thisMouseClicked(MouseEvent e) {

        logMethod(e, "ChatGuiImpl.thisMouseClicked");
    }

    @Override
    protected void pictureButtonMouseClicked(MouseEvent e) {

        logMethod(e, "ChatGuiImpl.pictureButtonMouseClicked");
    }

    /**
     * Handles the event when the send button is clicked.
     * Clears the text pane in the GUI and sends the message to the socket.
     *
     * @param e the ActionEvent object that triggered this event
     */
    @Override
    protected void sendButton(ActionEvent e) {

        guiFunctionality.clearTextPaneAndSendMessageToSocket();
    }
}
