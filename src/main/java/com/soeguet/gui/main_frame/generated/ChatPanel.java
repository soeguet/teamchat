package com.soeguet.gui.main_frame.generated;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.border.*;
import net.miginfocom.swing.*;
/*
 * Created by JFormDesigner on Mon Sep 04 21:08:05 CEST 2023
 */



/**
 * @author soeguet
 */
public abstract class ChatPanel extends JFrame {

	protected String lastMessageSenderName = "";
	protected String lastMessageTimeStamp = "";

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

	/**
	 * This method returns the name of the sender of the last message.
	 *
	 * @return the name of the last message sender
	 */
	public String getLastMessageSenderName() {
		return this.lastMessageSenderName;
	}

	/**
	 * This method sets the name of the sender of the last message.
	 *
	 * @param lastMessageSenderName the name of the sender of the last message
	 */
	public void setLastMessageSenderName(String lastMessageSenderName) {
		this.lastMessageSenderName = lastMessageSenderName;
	}

	/**
	 * Retrieves the timestamp of the last message.
	 *
	 * @return The timestamp of the last message as a string.
	 */
	public String getLastMessageTimeStamp() {
		return this.lastMessageTimeStamp;
	}

	/**
	 * Sets the timestamp of the last message.
	 *
	 * @param lastMessageTimeStamp The timestamp of the last message as a string.
	 */
	public void setLastMessageTimeStamp(String lastMessageTimeStamp) {
		this.lastMessageTimeStamp = lastMessageTimeStamp;
	}

	/**
	 * Returns the panel associated with this object.
	 *
	 * @return the panel associated with this object
	 */
	public JPanel getPanel() {
		return this.form_panel;
	}

	/**
	 * Retrieves the menu bar associated with the form.
	 *
	 * @return the JMenuBar associated with the form.
	 */
	public JMenuBar getMenuBar1() {
		return this.form_menuBar1;
	}

	/**
	 * Retrieves the file menu.
	 *
	 * @return the file menu as a JMenu object.
	 */
	public JMenu getFileMenu() {
		return this.form_fileMenu;
	}

	/**
	 * Retrieves the properties menu item of the form.
	 *
	 * @return The properties menu item.
	 */
	public JMenuItem getPropertiesMenuItem() {
		return this.form_propertiesMenuItem;
	}

	/**
	 * Returns the reset connection menu item.
	 *
	 * @return the reset connection menu item.
	 */
	public JMenuItem getResetConnectionMenuItem() {
		return this.form_resetConnectionMenuItem;
	}

	/**
	 * Retrieves the exit menu item.
	 *
	 * @return the exit menu item
	 */
	public JMenuItem getExitMenuItem() {
		return this.form_exitMenuItem;
	}

	/**
	 * Retrieves the extra menu associated with this form.
	 *
	 * @return the extra menu.
	 */
	public JMenu getExtraMenu() {
		return this.form_extraMenu;
	}

	/**
	 * Retrieves the "Participants" menu item.
	 *
	 * @return the "Participants" menu item
	 */
	public JMenuItem getParticipantsMenuItem() {
		return this.form_participantsMenuItem;
	}

	/**
	 * Returns the main text background scroll pane.
	 *
	 * @return the main text background scroll pane
	 */
	public JScrollPane getMainTextBackgroundScrollPane() {
		return this.form_mainTextBackgroundScrollPane;
	}

	/**
	 * Returns the main text panel.
	 *
	 * @return the main text panel
	 */
	public JPanel getMainTextPanel() {
		return this.form_mainTextPanel;
	}

	/**
	 * Retrieves the interaction area panel.
	 *
	 * @return the interaction area panel
	 */
	public JPanel getInteractionAreaPanel() {
		return this.form_interactionAreaPanel;
	}

	/**
	 * Retrieves the typing label.
	 *
	 * @return the typing label
	 */
	public JLabel getTypingLabel() {
		return this.form_typingLabel;
	}

	/**
	 * Retrieves Panel1.
	 *
	 * @return the Panel1
	 */
	public JPanel getPanel1() {
		return this.form_panel1;
	}

	/**
	 * Retrieves the main text field scroll pane.
	 *
	 * @return the main text field scroll pane
	 */
	public JScrollPane getMainTextFieldScrollPane() {
		return this.form_mainTextFieldScrollPane;
	}

	/**
	 * Retrieves the TextEditorPane.
	 *
	 * @return the TextEditorPane
	 */
	public JTextPane getTextEditorPane() {
		return this.form_textEditorPane;
	}

	/**
	 * Retrieves the Picture button.
	 *
	 * @return the Picture button
	 */
	public JButton getPictureButton() {
		return this.form_pictureButton;
	}

	/**
	 * Retrieves the Emoji button.
	 *
	 * @return the Emoji button
	 */
	public JButton getEmojiButton() {
		return this.form_emojiButton;
	}

	/**
	 * Retrieves the Send button.
	 *
	 * @return the Send button
	 */
	public JButton getSendButton() {
		return this.form_sendButton;
	}

	/**
	 * Retrieves the layered pane of the main text panel.
	 *
	 * @return the layered pane of the main text panel
	 */
	public JLayeredPane getMainTextPanelLayeredPane() {
		return this.form_mainTextPanelLayeredPane;
	}

	public JPanel getPanel2() {
		return this.form_panel2;
	}

	public JPanel getPanel3() {
		return this.form_panel3;
	}

	public JPanel getPanel4() {
		return this.form_panel4;
	}

	public JPanel getPanel5() {
		return this.form_panel5;
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
		// Generated using JFormDesigner non-commercial license
		this.form_panel = new JPanel();
		this.form_menuBar1 = new JMenuBar();
		this.form_fileMenu = new JMenu();
		this.form_propertiesMenuItem = new JMenuItem();
		this.form_resetConnectionMenuItem = new JMenuItem();
		this.form_exitMenuItem = new JMenuItem();
		this.form_extraMenu = new JMenu();
		this.form_participantsMenuItem = new JMenuItem();
		this.form_mainTextPanelLayeredPane = new JLayeredPane();
		this.form_mainTextBackgroundScrollPane = new JScrollPane();
		this.form_mainTextPanel = new JPanel();
		this.form_interactionAreaPanel = new JPanel();
		this.form_panel1 = new JPanel();
		this.form_panel3 = new JPanel();
		this.form_typingLabel = new JLabel();
		this.form_mainTextFieldScrollPane = new JScrollPane();
		this.form_textEditorPane = new JTextPane();
		this.form_panel2 = new JPanel();
		this.form_pictureButton = new JButton();
		this.form_emojiButton = new JButton();
		this.form_sendButton = new JButton();
		this.form_panel4 = new JPanel();
		this.form_panel5 = new JPanel();

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
			this.form_panel.setMinimumSize(null);
			this.form_panel.setMaximumSize(null);
			this.form_panel.setPreferredSize(null);
			this.form_panel.setLayout(new BorderLayout());

			//======== form_menuBar1 ========
			{

				//======== form_fileMenu ========
				{
					this.form_fileMenu.setText("file");

					//---- form_propertiesMenuItem ----
					this.form_propertiesMenuItem.setText("properties");
					this.form_propertiesMenuItem.addMouseListener(new MouseAdapter() {
						@Override
						public void mousePressed(MouseEvent e) {
							propertiesMenuItemMousePressed(e);
						}
					});
					this.form_fileMenu.add(this.form_propertiesMenuItem);
					this.form_fileMenu.addSeparator();

					//---- form_resetConnectionMenuItem ----
					this.form_resetConnectionMenuItem.setText("reset connection");
					this.form_resetConnectionMenuItem.addMouseListener(new MouseAdapter() {
						@Override
						public void mousePressed(MouseEvent e) {
							resetConnectionMenuItemMousePressed(e);
						}
					});
					this.form_fileMenu.add(this.form_resetConnectionMenuItem);
					this.form_fileMenu.addSeparator();

					//---- form_exitMenuItem ----
					this.form_exitMenuItem.setText("exit");
					this.form_exitMenuItem.addMouseListener(new MouseAdapter() {
						@Override
						public void mousePressed(MouseEvent e) {
							exitMenuItemMousePressed(e);
						}
					});
					this.form_fileMenu.add(this.form_exitMenuItem);
				}
				this.form_menuBar1.add(this.form_fileMenu);

				//======== form_extraMenu ========
				{
					this.form_extraMenu.setText("extra");

					//---- form_participantsMenuItem ----
					this.form_participantsMenuItem.setText("participants");
					this.form_participantsMenuItem.addMouseListener(new MouseAdapter() {
						@Override
						public void mousePressed(MouseEvent e) {
							participantsMenuItemMousePressed(e);
						}
					});
					this.form_extraMenu.add(this.form_participantsMenuItem);
				}
				this.form_menuBar1.add(this.form_extraMenu);
			}
			this.form_panel.add(this.form_menuBar1, BorderLayout.PAGE_START);

			//======== form_mainTextPanelLayeredPane ========
			{
				this.form_mainTextPanelLayeredPane.setOpaque(true);
				this.form_mainTextPanelLayeredPane.setBackground(null);
				this.form_mainTextPanelLayeredPane.setForeground(null);

				//======== form_mainTextBackgroundScrollPane ========
				{
					this.form_mainTextBackgroundScrollPane.setFocusable(false);
					this.form_mainTextBackgroundScrollPane.setRequestFocusEnabled(false);
					this.form_mainTextBackgroundScrollPane.setBackground(null);
					this.form_mainTextBackgroundScrollPane.setDoubleBuffered(true);
					this.form_mainTextBackgroundScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
					this.form_mainTextBackgroundScrollPane.setAutoscrolls(true);
					this.form_mainTextBackgroundScrollPane.setMaximumSize(null);
					this.form_mainTextBackgroundScrollPane.setAlignmentX(0.0F);
					this.form_mainTextBackgroundScrollPane.setAlignmentY(0.0F);
					this.form_mainTextBackgroundScrollPane.setPreferredSize(null);
					this.form_mainTextBackgroundScrollPane.setMinimumSize(null);
					this.form_mainTextBackgroundScrollPane.setOpaque(false);
					this.form_mainTextBackgroundScrollPane.setBorder(new EtchedBorder());
					this.form_mainTextBackgroundScrollPane.setViewportBorder(null);

					//======== form_mainTextPanel ========
					{
						this.form_mainTextPanel.setAutoscrolls(true);
						this.form_mainTextPanel.setVerifyInputWhenFocusTarget(false);
						this.form_mainTextPanel.setRequestFocusEnabled(false);
						this.form_mainTextPanel.setMaximumSize(null);
						this.form_mainTextPanel.setFont(new Font("Calibri Light", this.form_mainTextPanel.getFont().getStyle(), 18));
						this.form_mainTextPanel.setBackground(Color.white);
						this.form_mainTextPanel.setMinimumSize(null);
						this.form_mainTextPanel.setPreferredSize(null);
						this.form_mainTextPanel.setOpaque(false);
						this.form_mainTextPanel.setBorder(null);
						this.form_mainTextPanel.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								mainTextPanelMouseClicked(e);
							}
						});
						this.form_mainTextPanel.setLayout(new MigLayout(
							"fill,novisualpadding,hidemode 3",
							// columns
							"[fill]",
							// rows
							"[fill]"));
					}
					this.form_mainTextBackgroundScrollPane.setViewportView(this.form_mainTextPanel);
				}
				this.form_mainTextPanelLayeredPane.add(this.form_mainTextBackgroundScrollPane, JLayeredPane.DEFAULT_LAYER);
				this.form_mainTextBackgroundScrollPane.setBounds(10, 15, 865, 440);
			}
			this.form_panel.add(this.form_mainTextPanelLayeredPane, BorderLayout.CENTER);

			//======== form_interactionAreaPanel ========
			{
				this.form_interactionAreaPanel.setLayout(new MigLayout(
					"fillx,insets 0,gap 5 5",
					// columns
					"[grow,fill]",
					// rows
					"[bottom]"));

				//======== form_panel1 ========
				{
					this.form_panel1.setMaximumSize(null);
					this.form_panel1.setPreferredSize(null);
					this.form_panel1.setMinimumSize(null);
					this.form_panel1.setOpaque(false);
					this.form_panel1.setLayout(new BorderLayout(5, 5));

					//======== form_panel3 ========
					{
						this.form_panel3.setLayout(new BorderLayout());

						//---- form_typingLabel ----
						this.form_typingLabel.setBackground(new Color(0x00ffffff, true));
						this.form_typingLabel.setEnabled(false);
						this.form_typingLabel.setFocusable(false);
						this.form_typingLabel.setInheritsPopupMenu(false);
						this.form_typingLabel.setRequestFocusEnabled(false);
						this.form_typingLabel.setVerifyInputWhenFocusTarget(false);
						this.form_typingLabel.setText(" ");
						this.form_typingLabel.setMinimumSize(new Dimension(3, 10));
						this.form_typingLabel.setMaximumSize(new Dimension(3, 10));
						this.form_typingLabel.setPreferredSize(new Dimension(3, 10));
						this.form_panel3.add(this.form_typingLabel, BorderLayout.CENTER);
					}
					this.form_panel1.add(this.form_panel3, BorderLayout.NORTH);

					//======== form_mainTextFieldScrollPane ========
					{
						this.form_mainTextFieldScrollPane.setMinimumSize(new Dimension(22, 50));
						this.form_mainTextFieldScrollPane.setPreferredSize(new Dimension(53, 50));
						this.form_mainTextFieldScrollPane.setMaximumSize(new Dimension(32767, 50));

						//---- form_textEditorPane ----
						this.form_textEditorPane.setMinimumSize(new Dimension(22, 50));
						this.form_textEditorPane.setMaximumSize(new Dimension(32767, 50));
						this.form_textEditorPane.setPreferredSize(new Dimension(53, 50));
						this.form_textEditorPane.setOpaque(false);
						this.form_textEditorPane.setDoubleBuffered(true);
						this.form_textEditorPane.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								textEditorPaneMouseClicked(e);
							}
						});
						this.form_textEditorPane.addKeyListener(new KeyAdapter() {
							@Override
							public void keyPressed(KeyEvent e) {
								textEditorPaneKeyPressed(e);
							}
							@Override
							public void keyReleased(KeyEvent e) {
								textEditorPaneKeyReleased(e);
							}
						});
						this.form_mainTextFieldScrollPane.setViewportView(this.form_textEditorPane);
					}
					this.form_panel1.add(this.form_mainTextFieldScrollPane, BorderLayout.CENTER);

					//======== form_panel2 ========
					{
						this.form_panel2.setLayout(new MigLayout(
							"insets 0 10 5 10,hidemode 3,gap 5 10",
							// columns
							"[fill]" +
							"[fill]" +
							"[fill]",
							// rows
							"[baseline]"));

						//---- form_pictureButton ----
						this.form_pictureButton.setText("@");
						this.form_pictureButton.setDoubleBuffered(true);
						this.form_pictureButton.setMinimumSize(new Dimension(50, 50));
						this.form_pictureButton.setPreferredSize(new Dimension(50, 50));
						this.form_pictureButton.setMaximumSize(new Dimension(32767, 50));
						this.form_pictureButton.setToolTipText("picture");
						this.form_pictureButton.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								pictureButtonMouseClicked(e);
							}
						});
						this.form_panel2.add(this.form_pictureButton, "cell 0 0");

						//---- form_emojiButton ----
						this.form_emojiButton.setText("#");
						this.form_emojiButton.setDoubleBuffered(true);
						this.form_emojiButton.setMinimumSize(new Dimension(50, 50));
						this.form_emojiButton.setPreferredSize(new Dimension(50, 50));
						this.form_emojiButton.setMaximumSize(new Dimension(32767, 50));
						this.form_emojiButton.setSelectedIcon(null);
						this.form_emojiButton.setToolTipText("emoji");
						this.form_emojiButton.addActionListener(e -> emojiButton(e));
						this.form_panel2.add(this.form_emojiButton, "cell 1 0");

						//---- form_sendButton ----
						this.form_sendButton.setText("\u2192");
						this.form_sendButton.setMinimumSize(new Dimension(50, 50));
						this.form_sendButton.setMaximumSize(new Dimension(32767, 50));
						this.form_sendButton.setPreferredSize(new Dimension(50, 50));
						this.form_sendButton.setDoubleBuffered(true);
						this.form_sendButton.setToolTipText("send");
						this.form_sendButton.addActionListener(e -> sendButton(e));
						this.form_panel2.add(this.form_sendButton, "cell 2 0");
					}
					this.form_panel1.add(this.form_panel2, BorderLayout.EAST);

					//======== form_panel4 ========
					{
						this.form_panel4.setLayout(new BorderLayout());
					}
					this.form_panel1.add(this.form_panel4, BorderLayout.SOUTH);

					//======== form_panel5 ========
					{
						this.form_panel5.setLayout(new BorderLayout());
					}
					this.form_panel1.add(this.form_panel5, BorderLayout.WEST);
				}
				this.form_interactionAreaPanel.add(this.form_panel1, "cell 0 0");
			}
			this.form_panel.add(this.form_interactionAreaPanel, BorderLayout.PAGE_END);
		}
		contentPane.add(this.form_panel, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
	}

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
	protected JLayeredPane form_mainTextPanelLayeredPane;
	protected JScrollPane form_mainTextBackgroundScrollPane;
	protected JPanel form_mainTextPanel;
	protected JPanel form_interactionAreaPanel;
	protected JPanel form_panel1;
	protected JPanel form_panel3;
	protected JLabel form_typingLabel;
	protected JScrollPane form_mainTextFieldScrollPane;
	protected JTextPane form_textEditorPane;
	protected JPanel form_panel2;
	protected JButton form_pictureButton;
	protected JButton form_emojiButton;
	protected JButton form_sendButton;
	protected JPanel form_panel4;
	protected JPanel form_panel5;
	// JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
