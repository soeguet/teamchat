package com.soeguet.gui.notification_panel.generated;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import net.miginfocom.swing.*;
/*
 * Created by JFormDesigner on Sat Sep 23 19:01:49 CEST 2023
 */



/**
 * @author soeguet
 */
public abstract class Notification extends JDialog {
	public Notification(Window owner) {
		super(owner);
		initComponents();
	}

	public JPanel getPanel1() {
		return this.form_panel1;
	}

	public JPanel getNotificationMainPanel() {
		return this.form_notificationMainPanel;
	}

	public JPanel getPanel4() {
		return this.form_panel4;
	}

	public JPanel getPanel5() {
		return this.form_panel5;
	}

	public JButton getNotificationReplySendButton() {
		return this.form_notificationReplySendButton;
	}

	protected abstract void notificationReplySendActionPerformed(ActionEvent e);

	protected abstract void thisWindowGainedFocus(WindowEvent e);

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
		// Generated using JFormDesigner non-commercial license
		this.form_panel1 = new JPanel();
		this.form_notificationMainPanel = new JPanel();
		this.form_panel4 = new JPanel();
		this.form_panel5 = new JPanel();
		this.form_notificationReplySendButton = new JButton();

		//======== this ========
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		setAutoRequestFocus(false);
		setBackground(null);
		setType(Window.Type.POPUP);
		addWindowFocusListener(new WindowAdapter() {
			@Override
			public void windowGainedFocus(WindowEvent e) {
				thisWindowGainedFocus(e);
			}
		});
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== form_panel1 ========
		{
			this.form_panel1.setLayout(new BorderLayout(5, 5));

			//======== form_notificationMainPanel ========
			{
				this.form_notificationMainPanel.setLayout(new BorderLayout(5, 5));
			}
			this.form_panel1.add(this.form_notificationMainPanel, BorderLayout.CENTER);

			//======== form_panel4 ========
			{
				this.form_panel4.setLayout(new BorderLayout());

				//======== form_panel5 ========
				{
					this.form_panel5.setLayout(new MigLayout(
						"fill,insets 0,hidemode 3",
						// columns
						"[center]",
						// rows
						"[center]"));

					//---- form_notificationReplySendButton ----
					this.form_notificationReplySendButton.setText(">");
					this.form_notificationReplySendButton.setPreferredSize(new Dimension(40, 40));
					this.form_notificationReplySendButton.setMinimumSize(new Dimension(40, 40));
					this.form_notificationReplySendButton.setMaximumSize(new Dimension(40, 40));
					this.form_notificationReplySendButton.addActionListener(e -> notificationReplySendActionPerformed(e));
					this.form_panel5.add(this.form_notificationReplySendButton, "cell 0 0,align center center,grow 0 0");
				}
				this.form_panel4.add(this.form_panel5, BorderLayout.CENTER);
			}
			this.form_panel1.add(this.form_panel4, BorderLayout.EAST);
		}
		contentPane.add(this.form_panel1, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
	// Generated using JFormDesigner non-commercial license
	protected JPanel form_panel1;
	protected JPanel form_notificationMainPanel;
	protected JPanel form_panel4;
	protected JPanel form_panel5;
	protected JButton form_notificationReplySendButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}