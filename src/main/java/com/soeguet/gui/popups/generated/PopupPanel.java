package com.soeguet.gui.popups.generated;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
/*
 * Created by JFormDesigner on Mon Sep 11 21:41:02 CEST 2023
 */



/**
 * @author soeguet
 */
public class PopupPanel extends JPanel {
	public PopupPanel() {
		initComponents();
	}

	public JPanel getMessagePanel() {
		return this.form_messagePanel;
	}

	public JTextField getMessageTextField() {
		return this.form_messageTextField;
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
		// Generated using JFormDesigner non-commercial license
		this.form_messagePanel = new JPanel();
		this.form_messageTextField = new JTextField();

		//======== this ========
		setBorder(new EtchedBorder());
		setBackground(new Color(0xff6600));
		setLayout(new BorderLayout());

		//======== form_messagePanel ========
		{
			this.form_messagePanel.setLayout(new BorderLayout(5, 5));

			//---- form_messageTextField ----
			this.form_messageTextField.setText("test");
			this.form_messageTextField.setHorizontalAlignment(SwingConstants.CENTER);
			this.form_messageTextField.setEnabled(false);
			this.form_messageTextField.setEditable(false);
			this.form_messageTextField.setDisabledTextColor(new Color(0xff9900));
			this.form_messagePanel.add(this.form_messageTextField, BorderLayout.CENTER);
		}
		add(this.form_messagePanel, BorderLayout.CENTER);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
	// Generated using JFormDesigner non-commercial license
	protected JPanel form_messagePanel;
	protected JTextField form_messageTextField;
	// JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
