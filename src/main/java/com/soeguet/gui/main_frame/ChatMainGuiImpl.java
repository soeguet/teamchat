package com.soeguet.gui.main_frame;


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

public class ChatMainGuiImpl extends ChatPanel {

    private final Logger logger = Logger.getLogger(ChatMainGuiImpl.class.getName());
    private CustomWebsocketClient websocketClient;

    public ChatMainGuiImpl() {
        try {
            websocketClient = new CustomWebsocketClient(new URI("ws://127.0.0.1:8100"));
            websocketClient.connect();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

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

    @Override
    protected void textEditorPaneKeyPressed(KeyEvent e) {

//        testMethod();

        logMethod(e, "ChatGuiImpl.textEditorPaneKeyPressed");
    }

    @Override
    protected void textEditorPaneKeyReleased(KeyEvent e) {

        logMethod(e, "ChatGuiImpl.textEditorPaneKeyReleased");
    }

    @Override
    protected void emojiButton(ActionEvent e) {

        logMethod(e, "ChatGuiImpl.emojiButton");
    }

    /**
     * Handles the event when the mouse presses the exit menu item.
     * Set the default close operation for the current JFrame to EXIT_ON_CLOSE
     * and dispose the current JFrame.
     *
     * @param e the MouseEvent object that triggered this event
     */
    @Override
    protected void exitMenuItemMousePressed(MouseEvent e) {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.dispose();
    }

    @Override
    protected void thisMouseClicked(MouseEvent e) {

        logMethod(e, "ChatGuiImpl.thisMouseClicked");
    }

    @Override
    protected void pictureButtonMouseClicked(MouseEvent e) {

        logMethod(e, "ChatGuiImpl.pictureButtonMouseClicked");
    }

    @Override
    protected void sendButton(ActionEvent e) {

        System.out.println("jTextPane. = " + getTextEditorPane().getText());
        logMethod(e, "ChatGuiImpl.sendButton");
    }
}
