/*
 * Created by JFormDesigner on Sat Mar 04 17:35:49 CET 2023
 */

package com.pre.gui.emoji;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

/**
 * @author soeguet
 */
public abstract class EmojiWindow extends JFrame {

    public EmojiWindow() {
        super();
        initComponents();
    }

    protected abstract void thisWindowLostFocus(WindowEvent e);

    protected abstract void thisMouseDragged(MouseEvent e);

    protected abstract void btnFocusGained(FocusEvent e);

    protected abstract void btnFocusLost(FocusEvent e);

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner non-commercial license

        //======== this ========
        setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        setAlwaysOnTop(true);
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(0, 10));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner non-commercial license
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
