/*
 * Created by JFormDesigner on Sat Mar 04 00:03:24 CET 2023
 */

package com.soeguet.gui.newcomment.right.generated;

import java.awt.event.*;

import com.soeguet.gui.newcomment.custom_origin.CustomOriginPanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class PanelRight extends CustomOriginPanel {

	protected Color borderColor;

	public PanelRight() {
		super();
		initComponents();
	}

	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
		repaint();
	}

	protected abstract void replyButtonClicked(MouseEvent e);
	protected abstract void actionLabelMouseEntered(MouseEvent e);
	protected abstract void actionLabelMouseClicked(MouseEvent e);
	protected abstract void actionLabelMouseExited(MouseEvent e);




	private void createUIComponents() {

		form_panel1 = new JPanel() {

			@Override
			protected void paintComponent(Graphics grphcs) {

				int rounding = 20;
				Graphics2D g2d = (Graphics2D) grphcs;

				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setColor(Color.WHITE);
				g2d.fillRoundRect(0, 0, getWidth() - 13, getHeight() - 1, rounding, rounding);

				g2d.setColor(borderColor);
				g2d.drawRoundRect(0, 0, getWidth() - 13, getHeight() - 1, rounding, rounding);

				g2d.setColor(Color.WHITE);
				g2d.fillPolygon(new int[]{getWidth() - 1, getWidth() - 28, getWidth() - 13},
						new int[]{getHeight() - 1, getHeight() - 1, getHeight() - 13}, 3);

				g2d.setColor(borderColor);
				g2d.drawLine(getWidth() - 30, getHeight() - 1, getWidth(), getHeight() - 1);
				g2d.drawLine(getWidth() - 13, getHeight() - 13, getWidth(), getHeight());
			}

		};

		form_actionLabel = new JLabel() {

			@Override
			protected void paintComponent(Graphics g) {

				int x = 7;

				Graphics2D g2d = (Graphics2D) g;
				g2d.setColor(getForeground());
				g2d.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
				g2d.drawRoundRect(3, 3, 18, 18, 50, 50);

				g2d.fillRect(x, 12, 2, 2);
				g2d.fillRect(x + 4, 12, 2, 2);
				g2d.fillRect(x + 8, 12, 2, 2);
			}
		};
	}

	public JLayeredPane getLayeredPane1() {
		return this.form_layeredPane1;
	}

	public JLabel getNameLabel() {
		return this.form_nameLabel;
	}

	public JPanel getPanel1() {
		return this.form_panel1;
	}

	public JLabel getTimeLabel() {
		return this.form_timeLabel;
	}

	public JButton getButton1() {
		return this.form_button1;
	}

	public JPanel getPanel2() {
		return this.form_panel2;
	}

	public JLayeredPane getLayeredPane2() {
		return this.form_layeredPane2;
	}

	public JLabel getActionLabel() {
		return this.form_actionLabel;
	}

	public JPanel getHSpacer1() {
		return this.form_hSpacer1;
	}

	protected abstract void thisComponentResized(ComponentEvent e);

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
		// Generated using JFormDesigner non-commercial license
		createUIComponents();

		this.form_layeredPane1 = new JLayeredPane();
		this.form_layeredPane2 = new JLayeredPane();
		this.form_hSpacer1 = new JPanel(null);
		this.form_panel2 = new JPanel();
		this.form_timeLabel = new JLabel();
		this.form_button1 = new JButton();
		this.form_nameLabel = new JLabel();

		//======== this ========
		setPreferredSize(null);
		setMinimumSize(null);
		setMaximumSize(null);
		setOpaque(false);
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				thisComponentResized(e);
			}
		});
		setLayout(new MigLayout(
			"insets 0,align trailing bottom",
			// columns
			"[right]" +
			"[fill]" +
			"[right]" +
			"[fill]" +
			"[center]",
			// rows
			"[]" +
			"[]" +
			"[]"));

		//======== form_layeredPane1 ========
		{
			this.form_layeredPane1.setForeground(null);
			this.form_layeredPane1.setMinimumSize(null);
			this.form_layeredPane1.setPreferredSize(null);
			this.form_layeredPane1.setBackground(new Color(0xcc0033));
		}
		add(this.form_layeredPane1, "cell 1 1 2 2,grow");

		//======== form_panel1 ========
		{
			this.form_panel1.setBackground(null);
			this.form_panel1.setMinimumSize(null);
			this.form_panel1.setPreferredSize(null);
			this.form_panel1.setMaximumSize(null);
			this.form_panel1.setLayout(new MigLayout(
				"hidemode 3",
				// columns
				"[grow,fill]" +
				"[7!]",
				// rows
				"[]" +
				"[]"));
		}
		add(this.form_panel1, "pad 0,cell 2 0 1 2,aligny bottom,growy 0");

		//======== form_layeredPane2 ========
		{
			this.form_layeredPane2.setMinimumSize(new Dimension(25, 25));

			//---- form_actionLabel ----
			this.form_actionLabel.setHorizontalAlignment(SwingConstants.CENTER);
			this.form_actionLabel.setFont(this.form_actionLabel.getFont().deriveFont(this.form_actionLabel.getFont().getSize() + 5f));
			this.form_actionLabel.setPreferredSize(new Dimension(25, 25));
			this.form_actionLabel.setMaximumSize(null);
			this.form_actionLabel.setMinimumSize(null);
			this.form_actionLabel.setForeground(new Color(0x00000000, true));
			this.form_actionLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					actionLabelMouseClicked(e);
				}
				@Override
				public void mouseEntered(MouseEvent e) {
					actionLabelMouseEntered(e);
				}
				@Override
				public void mouseExited(MouseEvent e) {
					actionLabelMouseExited(e);
				}
			});
			this.form_layeredPane2.add(this.form_actionLabel, JLayeredPane.DEFAULT_LAYER);
			this.form_actionLabel.setBounds(new Rectangle(new Point(0, 0), this.form_actionLabel.getPreferredSize()));
		}
		add(this.form_layeredPane2, "cell 0 0 1 2");

		//---- form_hSpacer1 ----
		this.form_hSpacer1.setMaximumSize(new Dimension(10, 10));
		this.form_hSpacer1.setMinimumSize(new Dimension(10, 10));
		add(this.form_hSpacer1, "cell 3 1");

		//======== form_panel2 ========
		{
			this.form_panel2.setLayout(new MigLayout(
				"insets 0,gap 0 0",
				// columns
				"[grow,fill]" +
				"[fill]",
				// rows
				"[fill]" +
				"[grow,fill]"));

			//---- form_timeLabel ----
			this.form_timeLabel.setText("TIME");
			this.form_timeLabel.setEnabled(false);
			this.form_timeLabel.setHorizontalAlignment(SwingConstants.TRAILING);
			this.form_panel2.add(this.form_timeLabel, "cell 0 1");

			//---- form_button1 ----
			this.form_button1.setText("...");
			this.form_button1.setMaximumSize(null);
			this.form_button1.setMinimumSize(null);
			this.form_button1.setPreferredSize(null);
			this.form_button1.setBorder(null);
			this.form_button1.setBorderPainted(false);
			this.form_button1.setRequestFocusEnabled(false);
			this.form_button1.setOpaque(false);
			this.form_button1.setForeground(null);
			this.form_button1.setBackground(null);
			this.form_button1.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					replyButtonClicked(e);
				}
			});
			this.form_panel2.add(this.form_button1, "cell 1 1");

			//---- form_nameLabel ----
			this.form_nameLabel.setText("NAME");
			this.form_nameLabel.setFont(this.form_nameLabel.getFont().deriveFont(this.form_nameLabel.getFont().getStyle() | Font.BOLD, this.form_nameLabel.getFont().getSize() + 1f));
			this.form_nameLabel.setEnabled(false);
			this.form_nameLabel.setHorizontalAlignment(SwingConstants.TRAILING);
			this.form_panel2.add(this.form_nameLabel, "cell 0 0 2 1");
		}
		add(this.form_panel2, "cell 4 1,aligny bottom,growy 0");
		// JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
	// Generated using JFormDesigner non-commercial license
	protected JLayeredPane form_layeredPane1;
	protected JPanel form_panel1;
	protected JLayeredPane form_layeredPane2;
	protected JLabel form_actionLabel;
	protected JPanel form_hSpacer1;
	protected JPanel form_panel2;
	protected JLabel form_timeLabel;
	protected JButton form_button1;
	protected JLabel form_nameLabel;
	// JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}