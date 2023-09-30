package com.soeguet.gui.properties.generated;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
/*
 * Created by JFormDesigner on Sat Sep 16 21:17:08 CEST 2023
 */



/**
 * @author soeguet
 */
public abstract class PropertiesPanel extends JPanel {
	public PropertiesPanel() {
		initComponents();
	}

	protected abstract void thisMousePressed(MouseEvent e);

	protected abstract void thisMouseDragged(MouseEvent e);

	protected abstract void thisMouseEntered(MouseEvent e);

	protected abstract void thisMouseExited(MouseEvent e);

	protected abstract void thisFocusLost(FocusEvent e);

	protected abstract void closePropertiesPanelButtonMouseReleased(MouseEvent e);

	protected abstract void replyTextPaneFocusLost(FocusEvent e);

	protected abstract void quotePanelSenButtonMouseReleased(MouseEvent e);

	public JPanel getPanel4() {
		return form_panel4;
	}

	public JLabel getLabel1() {
		return form_label1;
	}

	public JButton getClosePropertiesPanelButton() {
		return form_closePropertiesPanelButton;
	}

	public JPanel getFormerMessagePanel() {
		return form_formerMessagePanel;
	}

	public JPanel getHSpacer1() {
		return form_hSpacer1;
	}

	public JPanel getHSpacer2() {
		return form_hSpacer2;
	}

	public JPanel getVSpacer2() {
		return form_vSpacer2;
	}

	public JPanel getPanel2() {
		return form_panel2;
	}

	public JScrollPane getScrollPane1() {
		return form_scrollPane1;
	}

	public JTabbedPane getTabbedPane() {
		return form_tabbedPane;
	}

	public JScrollPane getClientsScrollPane() {
		return form_clientsScrollPane;
	}

	public JPanel getClientsPanel() {
		return form_clientsPanel;
	}

	public JPanel getVSpacer1() {
		return form_vSpacer1;
	}

	public JPanel getPanel3() {
		return form_panel3;
	}

	public JPanel getPanel5() {
		return form_panel5;
	}

	public JComboBox<String> getClientSelectorComboBox() {
		return form_clientSelectorComboBox;
	}

	public JPanel getPanel6() {
		return form_panel6;
	}

	public JPanel getPanel7() {
		return form_panel7;
	}

	public JLabel getLabel2() {
		return form_label2;
	}

	public JTextField getUsernameTextField() {
		return form_usernameTextField;
	}

	public JPanel getPanel8() {
		return form_panel8;
	}

	public JLabel getLabel3() {
		return form_label3;
	}

	public JTextField getNicknameTextField() {
		return form_nicknameTextField;
	}

	public JPanel getPanel9() {
		return form_panel9;
	}

	public JLabel getLabel4() {
		return form_label4;
	}

	public JPanel getColorPickerPanel() {
		return form_colorPickerPanel;
	}

	public JPanel getHSpacer3() {
		return form_hSpacer3;
	}

	public JPanel getHSpacer11() {
		return form_hSpacer11;
	}

	public JPanel getHSpacer4() {
		return form_hSpacer4;
	}

	public JPanel getHSpacer5() {
		return form_hSpacer5;
	}

	public JPanel getHSpacer10() {
		return form_hSpacer10;
	}

	public JPanel getHSpacer6() {
		return form_hSpacer6;
	}

	public JPanel getHSpacer7() {
		return form_hSpacer7;
	}

	public JPanel getHSpacer9() {
		return form_hSpacer9;
	}

	public JPanel getHSpacer8() {
		return form_hSpacer8;
	}

	public JPanel getHSpacer12() {
		return form_hSpacer12;
	}

	public JPanel getHSpacer13() {
		return form_hSpacer13;
	}

	public JPanel getVSpacer3() {
		return form_vSpacer3;
	}

	protected abstract void nicknameTextFieldFocusLost(FocusEvent e);


	public JScrollPane getScrollPane2() {
		return form_scrollPane2;
	}

	public JPanel getClientsPanel2() {
		return form_clientsPanel2;
	}

	public JPanel getPanel10() {
		return form_panel10;
	}

	public JPanel getVSpacer4() {
		return form_vSpacer4;
	}

	public JPanel getPanel13() {
		return form_panel13;
	}

	public JPanel getHSpacer19() {
		return form_hSpacer19;
	}

	public JLabel getLabel6() {
		return form_label6;
	}

	public JPanel getHSpacer20() {
		return form_hSpacer20;
	}

	public JTextField getOwnUserNameTextField() {
		return form_ownUserNameTextField;
	}

	public JPanel getHSpacer21() {
		return form_hSpacer21;
	}

	public JPanel getPanel14() {
		return form_panel14;
	}

	public JPanel getHSpacer22() {
		return form_hSpacer22;
	}

	public JLabel getLabel7() {
		return form_label7;
	}

	public JPanel getHSpacer23() {
		return form_hSpacer23;
	}

	public JPanel getOwnBorderColorPanel() {
		return form_ownBorderColorPanel;
	}

	public JPanel getHSpacer24() {
		return form_hSpacer24;
	}

	protected abstract void ownUserNameTextFieldFocusLost(FocusEvent e);

	protected abstract void ownBorderColorPanelMouseClicked(MouseEvent e);

	public JPanel getPanel1() {
		return form_panel1;
	}

	public JPanel getPanel11() {
		return form_panel11;
	}

	public JPanel getPanel12() {
		return form_panel12;
	}

	public JButton getPropertiesOkButton() {
		return form_propertiesOkButton;
	}

	protected abstract void propertiesOkButtonMousePressed(MouseEvent e);

	protected abstract void clientSelectorComboBoxItemStateChanged(ItemEvent e);

	protected abstract void colorPickerPanelMouseClicked(MouseEvent e);

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
		// Generated using JFormDesigner non-commercial license
		form_panel4 = new JPanel();
		form_label1 = new JLabel();
		form_closePropertiesPanelButton = new JButton();
		form_formerMessagePanel = new JPanel();
		form_hSpacer1 = new JPanel(null);
		form_hSpacer2 = new JPanel(null);
		form_vSpacer2 = new JPanel(null);
		form_panel3 = new JPanel();
		form_vSpacer1 = new JPanel(null);
		form_tabbedPane = new JTabbedPane();
		form_scrollPane2 = new JScrollPane();
		form_clientsPanel2 = new JPanel();
		form_panel10 = new JPanel();
		form_vSpacer4 = new JPanel(null);
		form_panel13 = new JPanel();
		form_hSpacer19 = new JPanel(null);
		form_label6 = new JLabel();
		form_hSpacer20 = new JPanel(null);
		form_ownUserNameTextField = new JTextField();
		form_hSpacer21 = new JPanel(null);
		form_panel14 = new JPanel();
		form_hSpacer22 = new JPanel(null);
		form_label7 = new JLabel();
		form_hSpacer23 = new JPanel(null);
		form_ownBorderColorPanel = new JPanel();
		form_hSpacer24 = new JPanel(null);
		form_clientsScrollPane = new JScrollPane();
		form_clientsPanel = new JPanel();
		form_panel5 = new JPanel();
		form_vSpacer3 = new JPanel(null);
		form_panel6 = new JPanel();
		form_hSpacer12 = new JPanel(null);
		form_clientSelectorComboBox = new JComboBox<>();
		form_hSpacer13 = new JPanel(null);
		form_panel7 = new JPanel();
		form_hSpacer3 = new JPanel(null);
		form_label2 = new JLabel();
		form_hSpacer11 = new JPanel(null);
		form_usernameTextField = new JTextField();
		form_hSpacer4 = new JPanel(null);
		form_panel8 = new JPanel();
		form_hSpacer5 = new JPanel(null);
		form_label3 = new JLabel();
		form_hSpacer10 = new JPanel(null);
		form_nicknameTextField = new JTextField();
		form_hSpacer6 = new JPanel(null);
		form_panel9 = new JPanel();
		form_hSpacer7 = new JPanel(null);
		form_label4 = new JLabel();
		form_hSpacer9 = new JPanel(null);
		form_colorPickerPanel = new JPanel();
		form_hSpacer8 = new JPanel(null);
		form_scrollPane1 = new JScrollPane();
		form_panel2 = new JPanel();
		form_panel1 = new JPanel();
		form_panel11 = new JPanel();
		form_panel12 = new JPanel();
		form_propertiesOkButton = new JButton();

		//======== this ========
		setBorder(new LineBorder(Color.black, 2, true));
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				thisMouseEntered(e);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				thisMouseExited(e);
			}
			@Override
			public void mousePressed(MouseEvent e) {
				thisMousePressed(e);
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				thisMouseDragged(e);
			}
		});
		addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				thisFocusLost(e);
			}
		});
		setLayout(new BorderLayout());

		//======== form_panel4 ========
		{
			form_panel4.setLayout(new BorderLayout());

			//---- form_label1 ----
			form_label1.setText("properties");
			form_label1.setHorizontalTextPosition(SwingConstants.CENTER);
			form_label1.setHorizontalAlignment(SwingConstants.CENTER);
			form_label1.setFocusable(false);
			form_label1.setEnabled(false);
			form_panel4.add(form_label1, BorderLayout.CENTER);

			//---- form_closePropertiesPanelButton ----
			form_closePropertiesPanelButton.setText("x");
			form_closePropertiesPanelButton.setBorder(UIManager.getBorder("TitledBorder.border"));
			form_closePropertiesPanelButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					closePropertiesPanelButtonMouseReleased(e);
				}
			});
			form_panel4.add(form_closePropertiesPanelButton, BorderLayout.EAST);
		}
		add(form_panel4, BorderLayout.NORTH);

		//======== form_formerMessagePanel ========
		{
			form_formerMessagePanel.setLayout(new BorderLayout());

			//---- form_hSpacer1 ----
			form_hSpacer1.setMinimumSize(new Dimension(15, 12));
			form_hSpacer1.setPreferredSize(new Dimension(15, 10));
			form_formerMessagePanel.add(form_hSpacer1, BorderLayout.WEST);

			//---- form_hSpacer2 ----
			form_hSpacer2.setMinimumSize(new Dimension(15, 12));
			form_hSpacer2.setPreferredSize(new Dimension(15, 10));
			form_formerMessagePanel.add(form_hSpacer2, BorderLayout.EAST);

			//---- form_vSpacer2 ----
			form_vSpacer2.setPreferredSize(new Dimension(10, 5));
			form_vSpacer2.setMinimumSize(new Dimension(12, 5));
			form_formerMessagePanel.add(form_vSpacer2, BorderLayout.NORTH);

			//======== form_panel3 ========
			{
				form_panel3.setLayout(new BorderLayout());

				//---- form_vSpacer1 ----
				form_vSpacer1.setMinimumSize(new Dimension(12, 15));
				form_vSpacer1.setPreferredSize(new Dimension(10, 15));
				form_panel3.add(form_vSpacer1, BorderLayout.CENTER);
			}
			form_formerMessagePanel.add(form_panel3, BorderLayout.SOUTH);

			//======== form_tabbedPane ========
			{

				//======== form_scrollPane2 ========
				{

					//======== form_clientsPanel2 ========
					{
						form_clientsPanel2.setLayout(new BorderLayout());

						//======== form_panel10 ========
						{
							form_panel10.setLayout(new GridLayout(5, 0, 5, 5));
							form_panel10.add(form_vSpacer4);

							//======== form_panel13 ========
							{
								form_panel13.setLayout(new GridLayout());
								form_panel13.add(form_hSpacer19);

								//---- form_label6 ----
								form_label6.setText("username:");
								form_panel13.add(form_label6);
								form_panel13.add(form_hSpacer20);
								form_panel13.add(form_ownUserNameTextField);
								form_panel13.add(form_hSpacer21);
							}
							form_panel10.add(form_panel13);

							//======== form_panel14 ========
							{
								form_panel14.setLayout(new GridLayout());
								form_panel14.add(form_hSpacer22);

								//---- form_label7 ----
								form_label7.setText("border color:");
								form_panel14.add(form_label7);
								form_panel14.add(form_hSpacer23);

								//======== form_ownBorderColorPanel ========
								{
									form_ownBorderColorPanel.setBorder(new LineBorder(Color.black, 1, true));
									form_ownBorderColorPanel.addMouseListener(new MouseAdapter() {
										@Override
										public void mouseClicked(MouseEvent e) {
											ownBorderColorPanelMouseClicked(e);
										}
									});
									form_ownBorderColorPanel.setLayout(new BorderLayout());
								}
								form_panel14.add(form_ownBorderColorPanel);
								form_panel14.add(form_hSpacer24);
							}
							form_panel10.add(form_panel14);
						}
						form_clientsPanel2.add(form_panel10, BorderLayout.NORTH);
					}
					form_scrollPane2.setViewportView(form_clientsPanel2);
				}
				form_tabbedPane.addTab("user", form_scrollPane2);

				//======== form_clientsScrollPane ========
				{
					form_clientsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

					//======== form_clientsPanel ========
					{
						form_clientsPanel.setLayout(new BorderLayout());

						//======== form_panel5 ========
						{
							form_panel5.setLayout(new GridLayout(5, 0, 5, 5));
							form_panel5.add(form_vSpacer3);

							//======== form_panel6 ========
							{
								form_panel6.setLayout(new GridLayout());
								form_panel6.add(form_hSpacer12);

								//---- form_clientSelectorComboBox ----
								form_clientSelectorComboBox.addItemListener(e -> clientSelectorComboBoxItemStateChanged(e));
								form_panel6.add(form_clientSelectorComboBox);
								form_panel6.add(form_hSpacer13);
							}
							form_panel5.add(form_panel6);

							//======== form_panel7 ========
							{
								form_panel7.setLayout(new GridLayout());
								form_panel7.add(form_hSpacer3);

								//---- form_label2 ----
								form_label2.setText("username:");
								form_panel7.add(form_label2);
								form_panel7.add(form_hSpacer11);

								//---- form_usernameTextField ----
								form_usernameTextField.setEditable(false);
								form_panel7.add(form_usernameTextField);
								form_panel7.add(form_hSpacer4);
							}
							form_panel5.add(form_panel7);

							//======== form_panel8 ========
							{
								form_panel8.setLayout(new GridLayout());
								form_panel8.add(form_hSpacer5);

								//---- form_label3 ----
								form_label3.setText("nickname:");
								form_panel8.add(form_label3);
								form_panel8.add(form_hSpacer10);
								form_panel8.add(form_nicknameTextField);
								form_panel8.add(form_hSpacer6);
							}
							form_panel5.add(form_panel8);

							//======== form_panel9 ========
							{
								form_panel9.setLayout(new GridLayout());
								form_panel9.add(form_hSpacer7);

								//---- form_label4 ----
								form_label4.setText("border color:");
								form_panel9.add(form_label4);
								form_panel9.add(form_hSpacer9);

								//======== form_colorPickerPanel ========
								{
									form_colorPickerPanel.setBorder(new LineBorder(Color.black, 1, true));
									form_colorPickerPanel.addMouseListener(new MouseAdapter() {
										@Override
										public void mouseClicked(MouseEvent e) {
											colorPickerPanelMouseClicked(e);
										}
									});
									form_colorPickerPanel.setLayout(new BorderLayout());
								}
								form_panel9.add(form_colorPickerPanel);
								form_panel9.add(form_hSpacer8);
							}
							form_panel5.add(form_panel9);
						}
						form_clientsPanel.add(form_panel5, BorderLayout.NORTH);
					}
					form_clientsScrollPane.setViewportView(form_clientsPanel);
				}
				form_tabbedPane.addTab("clients", form_clientsScrollPane);

				//======== form_scrollPane1 ========
				{
					form_scrollPane1.setVisible(false);

					//======== form_panel2 ========
					{
						form_panel2.setLayout(new BorderLayout());
					}
					form_scrollPane1.setViewportView(form_panel2);
				}
				form_tabbedPane.addTab("text", form_scrollPane1);
			}
			form_formerMessagePanel.add(form_tabbedPane, BorderLayout.CENTER);
		}
		add(form_formerMessagePanel, BorderLayout.CENTER);

		//======== form_panel1 ========
		{
			form_panel1.setLayout(new BorderLayout());

			//======== form_panel11 ========
			{
				form_panel11.setLayout(new BorderLayout());

				//======== form_panel12 ========
				{
					form_panel12.setMinimumSize(new Dimension(100, 40));
					form_panel12.setPreferredSize(new Dimension(100, 40));
					form_panel12.setLayout(new GridBagLayout());
					((GridBagLayout)form_panel12.getLayout()).columnWidths = new int[] {0, 0};
					((GridBagLayout)form_panel12.getLayout()).rowHeights = new int[] {0, 0};
					((GridBagLayout)form_panel12.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
					((GridBagLayout)form_panel12.getLayout()).rowWeights = new double[] {1.0, 1.0E-4};

					//---- form_propertiesOkButton ----
					form_propertiesOkButton.setText("save");
					form_propertiesOkButton.setMinimumSize(null);
					form_propertiesOkButton.setMaximumSize(new Dimension(35, 35));
					form_propertiesOkButton.setPreferredSize(null);
					form_propertiesOkButton.addMouseListener(new MouseAdapter() {
						@Override
						public void mousePressed(MouseEvent e) {
							propertiesOkButtonMousePressed(e);
						}
					});
					form_panel12.add(form_propertiesOkButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(0, 0, 0, 0), 0, 0));
				}
				form_panel11.add(form_panel12, BorderLayout.CENTER);
			}
			form_panel1.add(form_panel11, BorderLayout.EAST);
		}
		add(form_panel1, BorderLayout.SOUTH);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
	// Generated using JFormDesigner non-commercial license
	protected JPanel form_panel4;
	protected JLabel form_label1;
	protected JButton form_closePropertiesPanelButton;
	protected JPanel form_formerMessagePanel;
	protected JPanel form_hSpacer1;
	protected JPanel form_hSpacer2;
	protected JPanel form_vSpacer2;
	protected JPanel form_panel3;
	protected JPanel form_vSpacer1;
	protected JTabbedPane form_tabbedPane;
	protected JScrollPane form_scrollPane2;
	protected JPanel form_clientsPanel2;
	protected JPanel form_panel10;
	protected JPanel form_vSpacer4;
	protected JPanel form_panel13;
	protected JPanel form_hSpacer19;
	protected JLabel form_label6;
	protected JPanel form_hSpacer20;
	protected JTextField form_ownUserNameTextField;
	protected JPanel form_hSpacer21;
	protected JPanel form_panel14;
	protected JPanel form_hSpacer22;
	protected JLabel form_label7;
	protected JPanel form_hSpacer23;
	protected JPanel form_ownBorderColorPanel;
	protected JPanel form_hSpacer24;
	protected JScrollPane form_clientsScrollPane;
	protected JPanel form_clientsPanel;
	protected JPanel form_panel5;
	protected JPanel form_vSpacer3;
	protected JPanel form_panel6;
	protected JPanel form_hSpacer12;
	protected JComboBox<String> form_clientSelectorComboBox;
	protected JPanel form_hSpacer13;
	protected JPanel form_panel7;
	protected JPanel form_hSpacer3;
	protected JLabel form_label2;
	protected JPanel form_hSpacer11;
	protected JTextField form_usernameTextField;
	protected JPanel form_hSpacer4;
	protected JPanel form_panel8;
	protected JPanel form_hSpacer5;
	protected JLabel form_label3;
	protected JPanel form_hSpacer10;
	protected JTextField form_nicknameTextField;
	protected JPanel form_hSpacer6;
	protected JPanel form_panel9;
	protected JPanel form_hSpacer7;
	protected JLabel form_label4;
	protected JPanel form_hSpacer9;
	protected JPanel form_colorPickerPanel;
	protected JPanel form_hSpacer8;
	protected JScrollPane form_scrollPane1;
	protected JPanel form_panel2;
	protected JPanel form_panel1;
	protected JPanel form_panel11;
	protected JPanel form_panel12;
	protected JButton form_propertiesOkButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}