package com.soeguet.gui.interrupt_dialog.generated;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
/*
 * Created by JFormDesigner on Sat Oct 14 20:46:32 CEST 2023
 */



/**
 * @author soeguet
 */
public abstract class InterruptDialog extends JDialog {
	public InterruptDialog(Window owner) {
		super(owner);
		initComponents();
	}

	protected abstract void okButtonActionPerformed(ActionEvent e);

	protected abstract void cancelButtonActionPerformed(ActionEvent e);

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
		// Generated using JFormDesigner non-commercial license
		form_dialogPane = new JPanel();
		form_label1 = new JLabel();
		form_contentPanel = new JPanel();
		form_panel2 = new JScrollPane();
		form_checkBoxPanel = new JPanel();
		form_buttonBar = new JPanel();
		form_okButton = new JButton();
		form_cancelButton = new JButton();

		//======== this ========
		setTitle("interrupt");
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== form_dialogPane ========
		{
			form_dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
			form_dialogPane.setLayout(new BorderLayout());

			//---- form_label1 ----
			form_label1.setText("which user do you intent do interrupt?");
			form_dialogPane.add(form_label1, BorderLayout.NORTH);

			//======== form_contentPanel ========
			{
				form_contentPanel.setLayout(new BorderLayout(5, 5));

				//======== form_panel2 ========
				{

					//======== form_checkBoxPanel ========
					{
						form_checkBoxPanel.setLayout(new GridLayout(2, 1, 5, 5));
					}
					form_panel2.setViewportView(form_checkBoxPanel);
				}
				form_contentPanel.add(form_panel2, BorderLayout.CENTER);
			}
			form_dialogPane.add(form_contentPanel, BorderLayout.CENTER);

			//======== form_buttonBar ========
			{
				form_buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
				form_buttonBar.setLayout(new GridBagLayout());
				((GridBagLayout)form_buttonBar.getLayout()).columnWidths = new int[] {0, 85, 80};
				((GridBagLayout)form_buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};

				//---- form_okButton ----
				form_okButton.setText("OK");
				form_okButton.addActionListener(e -> okButtonActionPerformed(e));
				form_buttonBar.add(form_okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 5), 0, 0));

				//---- form_cancelButton ----
				form_cancelButton.setText("Cancel");
				form_cancelButton.addActionListener(e -> cancelButtonActionPerformed(e));
				form_buttonBar.add(form_cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
			}
			form_dialogPane.add(form_buttonBar, BorderLayout.SOUTH);
		}
		contentPane.add(form_dialogPane, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
	// Generated using JFormDesigner non-commercial license
	protected JPanel form_dialogPane;
	protected JLabel form_label1;
	protected JPanel form_contentPanel;
	protected JScrollPane form_panel2;
	protected JPanel form_checkBoxPanel;
	protected JPanel form_buttonBar;
	protected JButton form_okButton;
	protected JButton form_cancelButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}