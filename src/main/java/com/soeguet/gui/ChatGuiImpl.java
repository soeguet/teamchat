package com.soeguet.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.EventObject;
import java.util.logging.Logger;

public class ChatGuiImpl extends ChatPanel {

    private final Logger logger = Logger.getLogger(ChatGuiImpl.class.getName());

    public ChatGuiImpl() {

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

        logMethod(e,"ChatGuiImpl.thisComponentResized");
    }

    @Override
    protected void propertiesMenuItemMousePressed(MouseEvent e) {

        logMethod(e,"ChatGuiImpl.propertiesMenuItemMousePressed");
    }

    @Override
    protected void resetConnectionMenuItemMousePressed(MouseEvent e) {

        logMethod(e,"ChatGuiImp.resetConnectionMenuItemMousePressed");
    }

    @Override
    protected void participantsMenuItemMousePressed(MouseEvent e) {

        logMethod(e,"ChatGuiImpl.participantsMenuItemMousePressed");
    }

    @Override
    protected void mainTextPanelMouseClicked(MouseEvent e) {

        logMethod(e,"ChatGuiImpl.mainTextPanelMouseClicked");
    }

    @Override
    protected void textEditorPaneMouseClicked(MouseEvent e) {

        logMethod(e,"ChatGuiImpl.textEditorPaneMouseClicked");
    }

    @Override
    protected void textEditorPaneKeyPressed(KeyEvent e) {

        logMethod(e,"ChatGuiImpl.textEditorPaneKeyPressed");
    }

    @Override
    protected void textEditorPaneKeyReleased(KeyEvent e) {

        logMethod(e,"ChatGuiImpl.textEditorPaneKeyReleased");
    }

    @Override
    protected void emojiButton(ActionEvent e) {

        logMethod(e,"ChatGuiImpl.emojiButton");
    }

    @Override
    protected void exitMenuItemMousePressed(MouseEvent e) {

        logMethod(e,"ChatGuiImpl.exitMenuItemMousePressed");
    }

    @Override
    protected void thisMouseClicked(MouseEvent e) {

        logMethod(e,"ChatGuiImpl.thisMouseClicked");
    }

    @Override
    protected void pictureButtonMouseClicked(MouseEvent e) {

        logMethod(e,"ChatGuiImpl.pictureButtonMouseClicked");
    }

    @Override
    protected void sendButton(ActionEvent e) {

        logMethod(e,"ChatGuiImpl.sendButton");
    }
}
