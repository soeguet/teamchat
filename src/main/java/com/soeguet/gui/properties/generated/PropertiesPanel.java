package com.soeguet.gui.properties.generated;

import java.awt.*;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
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

	protected abstract void closeReplyPanelButtonMouseReleased(MouseEvent e);

	protected abstract void replyTextPaneFocusLost(FocusEvent e);

	protected abstract void quotePanelSenButtonMouseReleased(MouseEvent e);

	public JPanel getPanel4() {
		return form_panel4;
	}

	public JLabel getLabel1() {
		return form_label1;
	}

	public JButton getCloseReplyPanelButton() {
		return form_closeReplyPanelButton;
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

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
		// Generated using JFormDesigner non-commercial license
		form_panel4 = new JPanel();
		form_label1 = new JLabel();
		form_closeReplyPanelButton = new JButton();
		form_formerMessagePanel = new JPanel();
		form_hSpacer1 = new JPanel(null);
		form_hSpacer2 = new JPanel(null);
		form_vSpacer2 = new JPanel(null);
		form_tabbedPane = new JTabbedPane();
		form_clientsScrollPane = new JScrollPane();
		form_clientsPanel = new JPanel();
		form_scrollPane1 = new JScrollPane();
		form_panel2 = new JPanel();

		//======== this ========
		setBorder(new EtchedBorder());
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

			//---- form_closeReplyPanelButton ----
			form_closeReplyPanelButton.setText("x");
			form_closeReplyPanelButton.setBorder(UIManager.getBorder("TitledBorder.border"));
			form_closeReplyPanelButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					closeReplyPanelButtonMouseReleased(e);
				}
			});
			form_panel4.add(form_closeReplyPanelButton, BorderLayout.EAST);
		}
		add(form_panel4, BorderLayout.PAGE_START);

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
			form_vSpacer2.setPreferredSize(new Dimension(10, 15));
			form_vSpacer2.setMinimumSize(new Dimension(12, 15));
			form_formerMessagePanel.add(form_vSpacer2, BorderLayout.NORTH);

			//======== form_tabbedPane ========
			{

				//======== form_clientsScrollPane ========
				{
					form_clientsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

					//======== form_clientsPanel ========
					{
						form_clientsPanel.setLayout(new BorderLayout());
					}
					form_clientsScrollPane.setViewportView(form_clientsPanel);
				}
				form_tabbedPane.addTab("clients", form_clientsScrollPane);

				//======== form_scrollPane1 ========
				{

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
		// JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
	// Generated using JFormDesigner non-commercial license
	protected JPanel form_panel4;
	protected JLabel form_label1;
	protected JButton form_closeReplyPanelButton;
	protected JPanel form_formerMessagePanel;
	protected JPanel form_hSpacer1;
	protected JPanel form_hSpacer2;
	protected JPanel form_vSpacer2;
	protected JTabbedPane form_tabbedPane;
	protected JScrollPane form_clientsScrollPane;
	protected JPanel form_clientsPanel;
	protected JScrollPane form_scrollPane1;
	protected JPanel form_panel2;
	// JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
