package com.soeguet.gui;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.EventObject;
import java.util.logging.Logger;

public class ChatGuiImpl extends ChatPanel {

    private final Logger logger = Logger.getLogger(ChatGuiImpl.class.getName());
    JTextPane jTextPane;

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

        form_mainTextPanel.removeAll();
        JPanel jPanel = new JPanel();
        String text = form_textEditorPane.getText();
        System.out.println("text = " + text);
        //        JLabel jLabel = new JLabel(text);

        jTextPane = new JTextPane();

        StyledDocument doc = (StyledDocument) jTextPane.getDocument();
        Style style = doc.addStyle("ImageStyle", null);
        StyleConstants.setComponent(style, new JLabel(new ImageIcon("/home/soeguet/Code/Java/teamchat/src/main/resources/emojis/$+1f4ac$+.png")));
        try {
            doc.insertString(doc.getLength(), "abc", null);
            doc.insertString(doc.getLength(), "XXX", style);
            doc.insertString(doc.getLength(), form_textEditorPane.getText(), null);
        } catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }

        //        jLabel.setFont(new Font("JetBrainsMono NF", Font.PLAIN, 12));

        jPanel.add(jTextPane);
        jPanel.setSize(new Dimension(300, 300));
        form_mainTextPanel.add(jPanel);
        jPanel.setVisible(true);
        revalidate();

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

    @Override
    protected void exitMenuItemMousePressed(MouseEvent e) {

        logMethod(e, "ChatGuiImpl.exitMenuItemMousePressed");
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

        System.out.println("jTextPane. = " + jTextPane.getText());
        logMethod(e, "ChatGuiImpl.sendButton");
    }
}
