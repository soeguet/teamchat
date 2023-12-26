package com.soeguet.gui.notification_panel.generated;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;



/**
 * @author soeguet
 */
public abstract class Notification extends JDialog {
	public Notification(Window owner) {
		super(owner);
		initComponents();
	}


	protected abstract void notificationAllPanelMouseClicked(MouseEvent e);

	protected abstract void notificationReplySendActionPerformed(ActionEvent e);

	public JPanel getNotificationAllPanel() {
		return this.form_notificationAllPanel;
	}

	public JPanel getNotificationMainPanel() {
		return this.form_notificationMainPanel;
	}

	public JLabel getNameLabel() {
		return this.form_nameLabel;
	}

	public JScrollPane getScrollPane1() {
		return this.form_scrollPane1;
	}

	public JTextPane getNotificationMainMessage() {
		return this.form_notificationMainMessage;
	}

	public JButton getNotificationReplySendButton() {
		return this.form_notificationReplySendButton;
	}

	public JButton getCloseAllNotificationsButton() {
		return this.form_closeAllNotificationsButton;
	}

	public JPanel getPanel7() {
		return this.form_panel7;
	}

	public JPanel getPanel8() {
		return this.form_panel8;
	}

	public JPanel getPanel9() {
		return this.form_panel9;
	}

	public JPanel getPanel10() {
		return this.form_panel10;
	}

	public JPanel getVSpacer1() {
		return this.form_vSpacer1;
	}

	public JPanel getHSpacer1() {
		return this.form_hSpacer1;
	}

	public JPanel getVSpacer2() {
		return this.form_vSpacer2;
	}

	public JPanel getHSpacer2() {
		return this.form_hSpacer2;
	}

	public JPanel getVSpacer3() {
		return this.form_vSpacer3;
	}

	public JPanel getVSpacer4() {
		return this.form_vSpacer4;
	}

	public JPanel getHSpacer3() {
		return this.form_hSpacer3;
	}

	public JPanel getHSpacer4() {
		return this.form_hSpacer4;
	}

	protected abstract void closeAllNotificationsActionPerformed(ActionEvent e);

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
		// Generated using JFormDesigner non-commercial license
		this.form_notificationAllPanel = new JPanel();
		this.form_panel9 = new JPanel();
		this.form_nameLabel = new JLabel();
		this.form_closeAllNotificationsButton = new JButton();
		this.form_notificationMainPanel = new JPanel();
		this.form_panel7 = new JPanel();
		this.form_panel10 = new JPanel();
		this.form_scrollPane1 = new JScrollPane();
		this.form_notificationMainMessage = new JTextPane();
		this.form_vSpacer1 = new JPanel(null);
		this.form_hSpacer1 = new JPanel(null);
		this.form_vSpacer2 = new JPanel(null);
		this.form_hSpacer2 = new JPanel(null);
		this.form_panel8 = new JPanel();
		this.form_notificationReplySendButton = new JButton();
		this.form_vSpacer3 = new JPanel(null);
		this.form_vSpacer4 = new JPanel(null);
		this.form_hSpacer3 = new JPanel(null);
		this.form_hSpacer4 = new JPanel(null);

		//======== this ========
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		setAutoRequestFocus(false);
		setBackground(null);
		setType(Window.Type.POPUP);
		setMinimumSize(new Dimension(400, 100));
		setPreferredSize(new Dimension(400, 100));
		setFocusableWindowState(false);
		setFocusable(false);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== form_notificationAllPanel ========
		{
			this.form_notificationAllPanel.setPreferredSize(new Dimension(400, 170));
			this.form_notificationAllPanel.setMinimumSize(new Dimension(400, 170));
			this.form_notificationAllPanel.setFocusable(false);
			this.form_notificationAllPanel.setRequestFocusEnabled(false);
			this.form_notificationAllPanel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					notificationAllPanelMouseClicked(e);
				}
			});
			this.form_notificationAllPanel.setLayout(new BorderLayout(5, 5));

			//======== form_panel9 ========
			{
				this.form_panel9.setRequestFocusEnabled(false);
				this.form_panel9.setFocusable(false);
				this.form_panel9.setLayout(new BorderLayout(5, 5));

				//---- form_nameLabel ----
				this.form_nameLabel.setText("sender");
				this.form_nameLabel.setFocusable(false);
				this.form_nameLabel.setRequestFocusEnabled(false);
				this.form_nameLabel.setVerifyInputWhenFocusTarget(false);
				this.form_nameLabel.setForeground(Color.black);
				this.form_nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
				this.form_nameLabel.setAlignmentX(0.5F);
				this.form_panel9.add(this.form_nameLabel, BorderLayout.CENTER);

				//---- form_closeAllNotificationsButton ----
				this.form_closeAllNotificationsButton.setText("x");
				this.form_closeAllNotificationsButton.setRequestFocusEnabled(false);
				this.form_closeAllNotificationsButton.setFocusable(false);
				this.form_closeAllNotificationsButton.addActionListener(e -> closeAllNotificationsActionPerformed(e));
				this.form_panel9.add(this.form_closeAllNotificationsButton, BorderLayout.EAST);
			}
			this.form_notificationAllPanel.add(this.form_panel9, BorderLayout.NORTH);

			//======== form_notificationMainPanel ========
			{
				this.form_notificationMainPanel.setMinimumSize(new Dimension(400, 170));
				this.form_notificationMainPanel.setPreferredSize(new Dimension(400, 170));
				this.form_notificationMainPanel.setRequestFocusEnabled(false);
				this.form_notificationMainPanel.setFocusable(false);
				this.form_notificationMainPanel.setLayout(new BorderLayout());

				//======== form_panel7 ========
				{
					this.form_panel7.setRequestFocusEnabled(false);
					this.form_panel7.setFocusable(false);
					this.form_panel7.setLayout(new BorderLayout(5, 5));

					//======== form_panel10 ========
					{
						this.form_panel10.setRequestFocusEnabled(false);
						this.form_panel10.setFocusable(false);
						this.form_panel10.setLayout(new BorderLayout());

						//======== form_scrollPane1 ========
						{
							this.form_scrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
							this.form_scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
							this.form_scrollPane1.setPreferredSize(new Dimension(74, 72));
							this.form_scrollPane1.setMinimumSize(new Dimension(16, 72));
							this.form_scrollPane1.setRequestFocusEnabled(false);
							this.form_scrollPane1.setFocusable(false);

							//---- form_notificationMainMessage ----
							this.form_notificationMainMessage.setText("text");
							this.form_notificationMainMessage.setMinimumSize(new Dimension(62, 35));
							this.form_notificationMainMessage.setPreferredSize(new Dimension(62, 35));
							this.form_scrollPane1.setViewportView(this.form_notificationMainMessage);
						}
						this.form_panel10.add(this.form_scrollPane1, BorderLayout.CENTER);

						//---- form_vSpacer1 ----
						this.form_vSpacer1.setRequestFocusEnabled(false);
						this.form_vSpacer1.setFocusable(false);
						this.form_panel10.add(this.form_vSpacer1, BorderLayout.SOUTH);

						//---- form_hSpacer1 ----
						this.form_hSpacer1.setRequestFocusEnabled(false);
						this.form_hSpacer1.setFocusable(false);
						this.form_panel10.add(this.form_hSpacer1, BorderLayout.WEST);

						//---- form_vSpacer2 ----
						this.form_vSpacer2.setRequestFocusEnabled(false);
						this.form_vSpacer2.setFocusable(false);
						this.form_panel10.add(this.form_vSpacer2, BorderLayout.NORTH);

						//---- form_hSpacer2 ----
						this.form_hSpacer2.setRequestFocusEnabled(false);
						this.form_hSpacer2.setFocusable(false);
						this.form_panel10.add(this.form_hSpacer2, BorderLayout.EAST);
					}
					this.form_panel7.add(this.form_panel10, BorderLayout.CENTER);

					//======== form_panel8 ========
					{
						this.form_panel8.setRequestFocusEnabled(false);
						this.form_panel8.setFocusable(false);
						this.form_panel8.setLayout(new BorderLayout());

						//---- form_notificationReplySendButton ----
						this.form_notificationReplySendButton.setText(">");
						this.form_notificationReplySendButton.setPreferredSize(new Dimension(30, 30));
						this.form_notificationReplySendButton.setMinimumSize(new Dimension(30, 30));
						this.form_notificationReplySendButton.setMaximumSize(new Dimension(30, 30));
						this.form_notificationReplySendButton.setToolTipText("reply");
						this.form_notificationReplySendButton.setRequestFocusEnabled(false);
						this.form_notificationReplySendButton.setFocusable(false);
						this.form_notificationReplySendButton.addActionListener(e -> notificationReplySendActionPerformed(e));
						this.form_panel8.add(this.form_notificationReplySendButton, BorderLayout.CENTER);

						//---- form_vSpacer3 ----
						this.form_vSpacer3.setRequestFocusEnabled(false);
						this.form_vSpacer3.setFocusable(false);
						this.form_panel8.add(this.form_vSpacer3, BorderLayout.PAGE_START);

						//---- form_vSpacer4 ----
						this.form_vSpacer4.setRequestFocusEnabled(false);
						this.form_vSpacer4.setFocusable(false);
						this.form_panel8.add(this.form_vSpacer4, BorderLayout.PAGE_END);

						//---- form_hSpacer3 ----
						this.form_hSpacer3.setRequestFocusEnabled(false);
						this.form_hSpacer3.setFocusable(false);
						this.form_panel8.add(this.form_hSpacer3, BorderLayout.LINE_END);

						//---- form_hSpacer4 ----
						this.form_hSpacer4.setRequestFocusEnabled(false);
						this.form_hSpacer4.setFocusable(false);
						this.form_panel8.add(this.form_hSpacer4, BorderLayout.LINE_START);
					}
					this.form_panel7.add(this.form_panel8, BorderLayout.EAST);
				}
				this.form_notificationMainPanel.add(this.form_panel7, BorderLayout.CENTER);
			}
			this.form_notificationAllPanel.add(this.form_notificationMainPanel, BorderLayout.CENTER);
		}
		contentPane.add(this.form_notificationAllPanel, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
	// Generated using JFormDesigner non-commercial license
	protected JPanel form_notificationAllPanel;
	protected JPanel form_panel9;
	protected JLabel form_nameLabel;
	protected JButton form_closeAllNotificationsButton;
	protected JPanel form_notificationMainPanel;
	protected JPanel form_panel7;
	protected JPanel form_panel10;
	protected JScrollPane form_scrollPane1;
	protected JTextPane form_notificationMainMessage;
	protected JPanel form_vSpacer1;
	protected JPanel form_hSpacer1;
	protected JPanel form_vSpacer2;
	protected JPanel form_hSpacer2;
	protected JPanel form_panel8;
	protected JButton form_notificationReplySendButton;
	protected JPanel form_vSpacer3;
	protected JPanel form_vSpacer4;
	protected JPanel form_hSpacer3;
	protected JPanel form_hSpacer4;
	// JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
