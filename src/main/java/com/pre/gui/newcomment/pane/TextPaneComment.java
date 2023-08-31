/*
 * Created by JFormDesigner on Sat Mar 04 00:01:51 CET 2023
 */

package com.pre.gui.newcomment.pane;

import java.awt.Insets;

import javax.swing.JTextPane;

/**
 * @author Osman
 */

public abstract class TextPaneComment extends JTextPane {

    public TextPaneComment() {
        super();
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
		// Generated using JFormDesigner non-commercial license
		setBackground(null);
		//---- this ----
		setMinimumSize(null);
		setPreferredSize(null);
		setMaximumSize(null);
		setMargin(new Insets(10, 16, 10, 16));
		setEditable(false);
		setFont(this.getFont().deriveFont(this.getFont().getSize() + 3f));
		setOpaque(false);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
	// Generated using JFormDesigner non-commercial license
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
