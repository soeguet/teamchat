package com.soeguet.gui.emoji;

import lombok.Getter;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

@Getter
public class EmojiImpl extends EmojiWindow {

    private JTextPane textEditorPane;
    private int lastX, lastY;

    public EmojiImpl(JTextPane textEditorPane) {
        super();
        this.textEditorPane = textEditorPane;
    }

    public EmojiImpl() {
        super();
    }

    public void setTextEditorPane(JTextPane textEditorPane) {
        this.textEditorPane = textEditorPane;
    }

    @Override
    protected void thisWindowLostFocus(WindowEvent e) {

        this.dispose();
    }

    @Override
    protected void thisMouseDragged(MouseEvent e) {

        SwingUtilities.invokeLater(
                () -> {
                    int x = e.getXOnScreen() - lastX;
                    int y = e.getYOnScreen() - lastY;

                    int newX = getLocationOnScreen().x + x;
                    int newY = getLocationOnScreen().y + y;

                    setLocation(newX, newY);

                    lastX = e.getXOnScreen();
                    lastY = e.getYOnScreen();
                });
    }

    @Override
    protected void btnFocusGained(FocusEvent e) {

        JButton button = (JButton) e.getComponent();
        button.setBorder(new LineBorder(Color.RED));
        button.setBorderPainted(true);
    }

    @Override
    protected void btnFocusLost(FocusEvent e) {

        JButton button = (JButton) e.getComponent();
        button.setBorderPainted(false);
    }
}
