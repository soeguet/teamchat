package com.soeguet.gui.interaction.generated;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import net.miginfocom.swing.*;
/*
 * Created by JFormDesigner on Sun Sep 10 12:09:03 CEST 2023
 */



/**
 * @author soeguet
 */
public abstract class ReplyPanel extends JPanel {
	public ReplyPanel() {
		initComponents();
	}

	public JPanel getFormerMessagePanel() {
		return form_formerMessagePanel;
	}

	public JPanel getPanel2() {
		return form_panel2;
	}

	public JScrollPane getScrollPane1() {
		return form_scrollPane1;
	}

	public JTextPane getReplyTextPane() {
		return form_replyTextPane;
	}

	public JPanel getPanel3() {
		return form_panel3;
	}

	public JButton getButton1() {
		return form_button1;
	}

	public JButton getButton3() {
		return form_button3;
	}

	public JButton getReplySendButton() {
		return form_replySendButton;
	}

	protected abstract void thisMousePressed(MouseEvent e);

	protected abstract void thisMouseDragged(MouseEvent e);

	public JLabel getLabel1() {
		return form_label1;
	}

	public JPanel getPanel4() {
		return form_panel4;
	}

	public JButton getCloseReplyPanelButton() {
		return form_closeReplyPanelButton;
	}

	protected abstract void closeReplyPanelButtonMouseReleased(MouseEvent e);

	protected abstract void thisMouseEntered(MouseEvent e);

	protected abstract void thisMouseExited(MouseEvent e);

	protected abstract void thisFocusLost(FocusEvent e);

	protected abstract void replyTextPaneFocusLost(FocusEvent e);

	protected abstract void replySendButtonMouseReleased(MouseEvent e);

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
		// Generated using JFormDesigner non-commercial license
		form_panel4 = new JPanel();
		form_label1 = new JLabel();
		form_closeReplyPanelButton = new JButton();
		form_formerMessagePanel = new JPanel();
		form_panel2 = new JPanel();
		form_scrollPane1 = new JScrollPane();
		form_replyTextPane = new JTextPane();
		form_panel3 = new JPanel();
		form_button1 = new JButton();
		form_button3 = new JButton();
		form_replySendButton = new JButton();

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
		setLayout(new MigLayout(
			"hidemode 3",
			// columns
			"[grow,fill]",
			// rows
			"[]" +
			"[grow]" +
			"[]"));

		//======== form_panel4 ========
		{
			form_panel4.setLayout(new BorderLayout());

			//---- form_label1 ----
			form_label1.setText("reply");
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
		add(form_panel4, "cell 0 0");

		//======== form_formerMessagePanel ========
		{
			form_formerMessagePanel.setLayout(new BorderLayout());
		}
		add(form_formerMessagePanel, "cell 0 1");

		//======== form_panel2 ========
		{
			form_panel2.setLayout(new BorderLayout());

			//======== form_scrollPane1 ========
			{

				//---- form_replyTextPane ----
				form_replyTextPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
				form_replyTextPane.addFocusListener(new FocusAdapter() {
					@Override
					public void focusLost(FocusEvent e) {
						replyTextPaneFocusLost(e);
					}
				});
				form_scrollPane1.setViewportView(form_replyTextPane);
			}
			form_panel2.add(form_scrollPane1, BorderLayout.CENTER);

			//======== form_panel3 ========
			{
				form_panel3.setLayout(new FlowLayout());

				//---- form_button1 ----
				form_button1.setText("p");
				form_panel3.add(form_button1);

				//---- form_button3 ----
				form_button3.setText("e");
				form_panel3.add(form_button3);

				//---- form_replySendButton ----
				form_replySendButton.setText("s");
				form_replySendButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						replySendButtonMouseReleased(e);
					}
				});
				form_panel3.add(form_replySendButton);
			}
			form_panel2.add(form_panel3, BorderLayout.EAST);
		}
		add(form_panel2, "cell 0 2");
		// JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
	// Generated using JFormDesigner non-commercial license
	protected JPanel form_panel4;
	protected JLabel form_label1;
	protected JButton form_closeReplyPanelButton;
	protected JPanel form_formerMessagePanel;
	protected JPanel form_panel2;
	protected JScrollPane form_scrollPane1;
	protected JTextPane form_replyTextPane;
	protected JPanel form_panel3;
	protected JButton form_button1;
	protected JButton form_button3;
	protected JButton form_replySendButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
