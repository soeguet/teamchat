package com.soeguet.gui.image_panel.generated;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
/*
 * Created by JFormDesigner on Mon Sep 18 17:48:08 CEST 2023
 */



/**
 * @author Osman
 */
public abstract class ImagePanel extends JPanel {
	public ImagePanel() {
		initComponents();
	}

	protected abstract void thisMousePressed(MouseEvent e);

	protected abstract void thisMouseDragged(MouseEvent e);

	protected abstract void closeImagePanelButtonMouseReleased(MouseEvent e);

	public JPanel getPanel4() {
		return form_panel4;
	}

	public JLabel getLabel1() {
		return form_label1;
	}

	public JButton getCloseImagePanelButton() {
		return form_closeImagePanelButton;
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

	public JPanel getPanel3() {
		return form_panel3;
	}

	public JPanel getVSpacer1() {
		return form_vSpacer1;
	}

	public JPanel getPictureMainPanel() {
		return form_pictureMainPanel;
	}

	public JPanel getPictureInteractionPanel() {
		return form_pictureInteractionPanel;
	}

	public JTextField getPictureDescriptionTextField() {
		return form_pictureDescriptionTextField;
	}

	public JPanel getPanel2() {
		return form_panel2;
	}

	public JButton getSendPictureButton() {
		return form_sendPictureButton;
	}

	public JButton getButton1() {
		return form_button1;
	}

	public JPanel getPicturePanel() {
		return form_picturePanel;
	}

	public JScrollPane getPictureScrollPane() {
		return form_pictureScrollPane;
	}

	public JLayeredPane getPictureLayeredPane() {
		return form_pictureLayeredPane;
	}

	public JPanel getZoomPanel() {
		return form_zoomPanel;
	}

	public JButton getButton2() {
		return form_button2;
	}

	public JButton getButton3() {
		return form_button3;
	}

	public JPanel getZoomMotherPanel() {
		return form_zoomMotherPanel;
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
		// Generated using JFormDesigner non-commercial license
		form_panel4 = new JPanel();
		form_label1 = new JLabel();
		form_closeImagePanelButton = new JButton();
		form_formerMessagePanel = new JPanel();
		form_hSpacer1 = new JPanel(null);
		form_hSpacer2 = new JPanel(null);
		form_vSpacer2 = new JPanel(null);
		form_panel3 = new JPanel();
		form_vSpacer1 = new JPanel(null);
		form_pictureMainPanel = new JPanel();
		form_pictureInteractionPanel = new JPanel();
		form_pictureDescriptionTextField = new JTextField();
		form_panel2 = new JPanel();
		form_sendPictureButton = new JButton();
		form_button1 = new JButton();
		form_pictureLayeredPane = new JLayeredPane();
		form_zoomMotherPanel = new JPanel();
		form_zoomPanel = new JPanel();
		form_button2 = new JButton();
		form_button3 = new JButton();
		form_pictureScrollPane = new JScrollPane();
		form_picturePanel = new JPanel();

		//======== this ========
		setBorder(new LineBorder(Color.black, 2, true));
		addMouseListener(new MouseAdapter() {
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
		setLayout(new BorderLayout());

		//======== form_panel4 ========
		{
			form_panel4.setLayout(new BorderLayout());

			//---- form_label1 ----
			form_label1.setText("images");
			form_label1.setHorizontalTextPosition(SwingConstants.CENTER);
			form_label1.setHorizontalAlignment(SwingConstants.CENTER);
			form_label1.setFocusable(false);
			form_label1.setEnabled(false);
			form_panel4.add(form_label1, BorderLayout.CENTER);

			//---- form_closeImagePanelButton ----
			form_closeImagePanelButton.setText("x");
			form_closeImagePanelButton.setBorder(UIManager.getBorder("TitledBorder.border"));
			form_closeImagePanelButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					closeImagePanelButtonMouseReleased(e);
				}
			});
			form_panel4.add(form_closeImagePanelButton, BorderLayout.EAST);
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

			//======== form_pictureMainPanel ========
			{
				form_pictureMainPanel.setLayout(new BorderLayout());

				//======== form_pictureInteractionPanel ========
				{
					form_pictureInteractionPanel.setLayout(new BorderLayout());
					form_pictureInteractionPanel.add(form_pictureDescriptionTextField, BorderLayout.CENTER);

					//======== form_panel2 ========
					{
						form_panel2.setLayout(new BorderLayout());

						//---- form_sendPictureButton ----
						form_sendPictureButton.setText("send");
						form_panel2.add(form_sendPictureButton, BorderLayout.CENTER);

						//---- form_button1 ----
						form_button1.setText("select");
						form_panel2.add(form_button1, BorderLayout.WEST);
					}
					form_pictureInteractionPanel.add(form_panel2, BorderLayout.EAST);
				}
				form_pictureMainPanel.add(form_pictureInteractionPanel, BorderLayout.SOUTH);

				//======== form_pictureLayeredPane ========
				{

					//======== form_zoomMotherPanel ========
					{
						form_zoomMotherPanel.setBorder(null);
						form_zoomMotherPanel.setOpaque(false);
						form_zoomMotherPanel.setLayout(new BorderLayout());

						//======== form_zoomPanel ========
						{
							form_zoomPanel.setOpaque(false);
							form_zoomPanel.setLayout(new FlowLayout());

							//---- form_button2 ----
							form_button2.setText("-");
							form_zoomPanel.add(form_button2);

							//---- form_button3 ----
							form_button3.setText("+");
							form_zoomPanel.add(form_button3);
						}
						form_zoomMotherPanel.add(form_zoomPanel, BorderLayout.NORTH);
					}
					form_pictureLayeredPane.add(form_zoomMotherPanel, JLayeredPane.DEFAULT_LAYER);
					form_zoomMotherPanel.setBounds(0, 0, 365, 230);

					//======== form_pictureScrollPane ========
					{
						form_pictureScrollPane.setBorder(LineBorder.createBlackLineBorder());

						//======== form_picturePanel ========
						{
							form_picturePanel.setLayout(new BorderLayout());
						}
						form_pictureScrollPane.setViewportView(form_picturePanel);
					}
					form_pictureLayeredPane.add(form_pictureScrollPane, JLayeredPane.DEFAULT_LAYER);
					form_pictureScrollPane.setBounds(0, 0, 366, 231);
				}
				form_pictureMainPanel.add(form_pictureLayeredPane, BorderLayout.CENTER);
			}
			form_formerMessagePanel.add(form_pictureMainPanel, BorderLayout.CENTER);
		}
		add(form_formerMessagePanel, BorderLayout.CENTER);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
	// Generated using JFormDesigner non-commercial license
	protected JPanel form_panel4;
	protected JLabel form_label1;
	protected JButton form_closeImagePanelButton;
	protected JPanel form_formerMessagePanel;
	protected JPanel form_hSpacer1;
	protected JPanel form_hSpacer2;
	protected JPanel form_vSpacer2;
	protected JPanel form_panel3;
	protected JPanel form_vSpacer1;
	protected JPanel form_pictureMainPanel;
	protected JPanel form_pictureInteractionPanel;
	protected JTextField form_pictureDescriptionTextField;
	protected JPanel form_panel2;
	protected JButton form_sendPictureButton;
	protected JButton form_button1;
	protected JLayeredPane form_pictureLayeredPane;
	protected JPanel form_zoomMotherPanel;
	protected JPanel form_zoomPanel;
	protected JButton form_button2;
	protected JButton form_button3;
	protected JScrollPane form_pictureScrollPane;
	protected JPanel form_picturePanel;
	// JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}