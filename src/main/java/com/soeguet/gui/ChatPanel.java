package com.soeguet.gui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
/*
 * Created by JFormDesigner on Wed Aug 30 12:14:07 CEST 2023
 */

/**
 * @author soeguet
 */
public abstract class ChatPanel extends JFrame {

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
	// Generated using JFormDesigner non-commercial license
	protected JPanel form_panel;
	protected JMenuBar form_menuBar1;
	protected JMenu form_fileMenu;
	protected JMenuItem form_propertiesMenuItem;
	protected JMenuItem form_resetConnectionMenuItem;
	protected JMenuItem form_exitMenuItem;
	protected JMenu form_extraMenu;
	protected JMenuItem form_participantsMenuItem;
	protected JScrollPane form_mainTextBackgroundScrollPane;
	protected JPanel form_mainTextPanel;
	protected JPanel form_panel2;
	protected JLabel form_typingLabel;
	protected JPanel form_panel1;
	protected JScrollPane form_scrollPane1;
	protected JTextPane form_textEditorPane;
	protected JButton form_pictureButton;
	protected JButton form_emojiButton;
	protected JButton form_sendButton;
    public ChatPanel() {

        initComponents();
    }

    protected abstract void thisPropertyChange(PropertyChangeEvent e);

    protected abstract void thisComponentResized(ComponentEvent e);

    protected abstract void thisMouseClicked(MouseEvent e);

    protected abstract void propertiesMenuItemMousePressed(MouseEvent e);

    protected abstract void resetConnectionMenuItemMousePressed(MouseEvent e);

    protected abstract void exitMenuItemMousePressed(MouseEvent e);

    protected abstract void participantsMenuItemMousePressed(MouseEvent e);

    protected abstract void mainTextPanelMouseClicked(MouseEvent e);

    protected abstract void textEditorPaneMouseClicked(MouseEvent e);

    protected abstract void textEditorPaneKeyPressed(KeyEvent e);

    protected abstract void textEditorPaneKeyReleased(KeyEvent e);

    protected abstract void pictureButtonMouseClicked(MouseEvent e);

    protected abstract void emojiButton(ActionEvent e);

    protected abstract void sendButton(ActionEvent e);

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
		// Generated using JFormDesigner non-commercial license
		form_panel = new JPanel();
		form_menuBar1 = new JMenuBar();
		form_fileMenu = new JMenu();
		form_propertiesMenuItem = new JMenuItem();
		form_resetConnectionMenuItem = new JMenuItem();
		form_exitMenuItem = new JMenuItem();
		form_extraMenu = new JMenu();
		form_participantsMenuItem = new JMenuItem();
		form_mainTextBackgroundScrollPane = new JScrollPane();
		form_mainTextPanel = new JPanel();
		form_panel2 = new JPanel();
		form_typingLabel = new JLabel();
		form_panel1 = new JPanel();
		form_scrollPane1 = new JScrollPane();
		form_textEditorPane = new JTextPane();
		form_pictureButton = new JButton();
		form_emojiButton = new JButton();
		form_sendButton = new JButton();

		//======== this ========
		setFocusable(false);
		setMinimumSize(new Dimension(250, 250));
		setPreferredSize(new Dimension(700, 700));
		setVisible(true);
		addPropertyChangeListener("FrameChange", e -> thisPropertyChange(e));
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				thisComponentResized(e);
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				thisMouseClicked(e);
			}
		});
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== form_panel ========
		{
			form_panel.setMinimumSize(null);
			form_panel.setMaximumSize(null);
			form_panel.setPreferredSize(null);
			form_panel.setLayout(new BorderLayout());

			//======== form_menuBar1 ========
			{

				//======== form_fileMenu ========
				{
					form_fileMenu.setText("file");

					//---- form_propertiesMenuItem ----
					form_propertiesMenuItem.setText("properties");
					form_propertiesMenuItem.addMouseListener(new MouseAdapter() {
						@Override
						public void mousePressed(MouseEvent e) {
							propertiesMenuItemMousePressed(e);
						}
					});
					form_fileMenu.add(form_propertiesMenuItem);
					form_fileMenu.addSeparator();

					//---- form_resetConnectionMenuItem ----
					form_resetConnectionMenuItem.setText("reset connection");
					form_resetConnectionMenuItem.addMouseListener(new MouseAdapter() {
						@Override
						public void mousePressed(MouseEvent e) {
							resetConnectionMenuItemMousePressed(e);
						}
					});
					form_fileMenu.add(form_resetConnectionMenuItem);
					form_fileMenu.addSeparator();

					//---- form_exitMenuItem ----
					form_exitMenuItem.setText("exit");
					form_exitMenuItem.addMouseListener(new MouseAdapter() {
						@Override
						public void mousePressed(MouseEvent e) {
							exitMenuItemMousePressed(e);
						}
					});
					form_fileMenu.add(form_exitMenuItem);
				}
				form_menuBar1.add(form_fileMenu);

				//======== form_extraMenu ========
				{
					form_extraMenu.setText("extra");

					//---- form_participantsMenuItem ----
					form_participantsMenuItem.setText("participants");
					form_participantsMenuItem.addMouseListener(new MouseAdapter() {
						@Override
						public void mousePressed(MouseEvent e) {
							participantsMenuItemMousePressed(e);
						}
					});
					form_extraMenu.add(form_participantsMenuItem);
				}
				form_menuBar1.add(form_extraMenu);
			}
			form_panel.add(form_menuBar1, BorderLayout.PAGE_START);

			//======== form_mainTextBackgroundScrollPane ========
			{
				form_mainTextBackgroundScrollPane.setFocusable(false);
				form_mainTextBackgroundScrollPane.setRequestFocusEnabled(false);
				form_mainTextBackgroundScrollPane.setBackground(null);
				form_mainTextBackgroundScrollPane.setDoubleBuffered(true);
				form_mainTextBackgroundScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				form_mainTextBackgroundScrollPane.setAutoscrolls(true);
				form_mainTextBackgroundScrollPane.setMaximumSize(null);
				form_mainTextBackgroundScrollPane.setAlignmentX(0.0F);
				form_mainTextBackgroundScrollPane.setAlignmentY(0.0F);
				form_mainTextBackgroundScrollPane.setPreferredSize(null);
				form_mainTextBackgroundScrollPane.setMinimumSize(null);
				form_mainTextBackgroundScrollPane.setOpaque(false);

				//======== form_mainTextPanel ========
				{
					form_mainTextPanel.setAutoscrolls(true);
					form_mainTextPanel.setVerifyInputWhenFocusTarget(false);
					form_mainTextPanel.setRequestFocusEnabled(false);
					form_mainTextPanel.setMaximumSize(null);
					form_mainTextPanel.setFont(new Font("Calibri Light", form_mainTextPanel.getFont().getStyle(), 18));
					form_mainTextPanel.setBackground(Color.white);
					form_mainTextPanel.setMinimumSize(null);
					form_mainTextPanel.setPreferredSize(null);
					form_mainTextPanel.setOpaque(false);
					form_mainTextPanel.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							mainTextPanelMouseClicked(e);
						}
					});
					form_mainTextPanel.setLayout(new MigLayout(
						"fill,hidemode 3",
						// columns
						"[]",
						// rows
						null));
				}
				form_mainTextBackgroundScrollPane.setViewportView(form_mainTextPanel);
			}
			form_panel.add(form_mainTextBackgroundScrollPane, BorderLayout.CENTER);

			//======== form_panel2 ========
			{
				form_panel2.setLayout(new MigLayout(
					"fillx,insets 0,gap 0 0",
					// columns
					"[grow,fill]",
					// rows
					"[]" +
					"[center]"));

				//---- form_typingLabel ----
				form_typingLabel.setBackground(new Color(0x00ffffff, true));
				form_typingLabel.setEnabled(false);
				form_typingLabel.setFocusable(false);
				form_typingLabel.setInheritsPopupMenu(false);
				form_typingLabel.setRequestFocusEnabled(false);
				form_typingLabel.setVerifyInputWhenFocusTarget(false);
				form_typingLabel.setText(" ");
				form_panel2.add(form_typingLabel, "pad 0 15 0 15,cell 0 0");

				//======== form_panel1 ========
				{
					form_panel1.setMaximumSize(null);
					form_panel1.setPreferredSize(null);
					form_panel1.setMinimumSize(null);
					form_panel1.setOpaque(false);
					form_panel1.setLayout(new MigLayout(
						"insets 0 10 5 10,hidemode 3,gap 5 10",
						// columns
						"[292:n,grow,fill]" +
						"[fill]" +
						"[fill]" +
						"[fill]",
						// rows
						"[baseline]"));

					//======== form_scrollPane1 ========
					{
						form_scrollPane1.setMinimumSize(new Dimension(22, 50));
						form_scrollPane1.setPreferredSize(new Dimension(53, 50));
						form_scrollPane1.setMaximumSize(new Dimension(32767, 50));

						//---- form_textEditorPane ----
						form_textEditorPane.setMinimumSize(new Dimension(22, 50));
						form_textEditorPane.setMaximumSize(new Dimension(32767, 50));
						form_textEditorPane.setPreferredSize(new Dimension(53, 50));
						form_textEditorPane.setOpaque(false);
						form_textEditorPane.setDoubleBuffered(true);
						form_textEditorPane.setFont(new Font("JetBrainsMono NF", Font.PLAIN, 13));
						form_textEditorPane.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								textEditorPaneMouseClicked(e);
							}
						});
						form_textEditorPane.addKeyListener(new KeyAdapter() {
							@Override
							public void keyPressed(KeyEvent e) {
								textEditorPaneKeyPressed(e);
							}
							@Override
							public void keyReleased(KeyEvent e) {
								textEditorPaneKeyReleased(e);
							}
						});
						form_scrollPane1.setViewportView(form_textEditorPane);
					}
					form_panel1.add(form_scrollPane1, "cell 0 0");

					//---- form_pictureButton ----
					form_pictureButton.setText("@");
					form_pictureButton.setDoubleBuffered(true);
					form_pictureButton.setMinimumSize(new Dimension(50, 50));
					form_pictureButton.setPreferredSize(new Dimension(50, 50));
					form_pictureButton.setMaximumSize(new Dimension(32767, 50));
					form_pictureButton.setToolTipText("picture");
					form_pictureButton.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							pictureButtonMouseClicked(e);
						}
					});
					form_panel1.add(form_pictureButton, "cell 1 0");

					//---- form_emojiButton ----
					form_emojiButton.setText("#");
					form_emojiButton.setDoubleBuffered(true);
					form_emojiButton.setMinimumSize(new Dimension(50, 50));
					form_emojiButton.setPreferredSize(new Dimension(50, 50));
					form_emojiButton.setMaximumSize(new Dimension(32767, 50));
					form_emojiButton.setSelectedIcon(null);
					form_emojiButton.setToolTipText("emoji");
					form_emojiButton.addActionListener(e -> emojiButton(e));
					form_panel1.add(form_emojiButton, "cell 2 0");

					//---- form_sendButton ----
					form_sendButton.setText("\u2192");
					form_sendButton.setMinimumSize(new Dimension(50, 50));
					form_sendButton.setMaximumSize(new Dimension(32767, 50));
					form_sendButton.setPreferredSize(new Dimension(50, 50));
					form_sendButton.setDoubleBuffered(true);
					form_sendButton.setToolTipText("send");
					form_sendButton.addActionListener(e -> sendButton(e));
					form_panel1.add(form_sendButton, "cell 3 0");
				}
				form_panel2.add(form_panel1, "cell 0 1");
			}
			form_panel.add(form_panel2, BorderLayout.PAGE_END);
		}
		contentPane.add(form_panel, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }
	// JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
