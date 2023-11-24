package com.soeguet.gui.comments.generic_comment.gui_elements.buttons;

import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * A custom close button that extends JButton and implements MouseListener.
 * It provides functionality for removing a JComponent from its parent container
 * when clicked and re-requesting focus on the main text editor pane.
 */
public class CustomCloseButton extends JButton implements MouseListener {

    private final MainFrameGuiInterface mainFrame;
    private final JComponent component;

    /**
     * Creates a custom close button.
     *
     * @param mainFrame The reference to the MainFrameGuiInterface.
     * @param component The reference to the JComponent.
     * @param text The text to set for the button.
     */
    public CustomCloseButton(final MainFrameGuiInterface mainFrame, JComponent component, String text) {

        this.mainFrame = mainFrame;

        this.component = component;
        this.addMouseListener(this);
        super.setText(text);
    }

    /**
     * Invoked when the mouse button has been clicked (pressed and released) on this component.
     *
     * @param e The MouseEvent representing the mouse click event.
     */
    @Override
    public void mouseClicked(final MouseEvent e) {

        component.removeAll();
        component.setVisible(false);

        //re-request focus
        mainFrame.getTextEditorPane().setFocusable(true);
        mainFrame.getTextEditorPane().requestFocus();
    }

    @Override
    public void mousePressed(final MouseEvent e) {

    }

    @Override
    public void mouseReleased(final MouseEvent e) {

    }

    @Override
    public void mouseEntered(final MouseEvent e) {

    }

    @Override
    public void mouseExited(final MouseEvent e) {

    }
}