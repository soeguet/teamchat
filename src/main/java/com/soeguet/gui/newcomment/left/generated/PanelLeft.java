 /*
 * Created by JFormDesigner on Sat Mar 04 00:03:24 CET 2023
 */

package com.soeguet.gui.newcomment.left.generated;

import com.soeguet.gui.newcomment.custom_origin.CustomOriginPanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class PanelLeft extends CustomOriginPanel {

	protected Color borderColor;

	public PanelLeft() {
		super();
		initComponents();
	}

	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}

	private void createUIComponents() {

		form_panel1 = new JPanel() {

			@Override
			protected void paintComponent(Graphics grphcs) {

				int rounding = 20;
				Graphics2D g2d = (Graphics2D) grphcs;


				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setColor(Color.WHITE);
				g2d.fillRoundRect(13, 0, getWidth() - 13 - 1, getHeight() - 1, rounding, rounding);

				g2d.setColor(borderColor);
				g2d.drawRoundRect(13, 0, getWidth() - 13 - 1, getHeight() - 1, rounding, rounding);

				g2d.setColor(Color.WHITE);
				g2d.fillPolygon(new int[]{0, 13, 25},
						new int[]{getHeight(), getHeight() - 13, getHeight()},
						3);

				g2d.setColor(borderColor);
				g2d.drawLine(0, getHeight() - 1, 25, getHeight() - 1);
				g2d.drawLine(0, getHeight() - 1, 13, getHeight() - 13);
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
	protected abstract void actionLabelMouseEntered(MouseEvent e);
	protected abstract void actionLabelMouseExited(MouseEvent e);
	protected abstract void replyButtonClicked(MouseEvent e);
	public JLayeredPane getLayeredPane1() {
		return form_layeredPane1;
	}

	public JLabel getNameLabel() {
		return form_nameLabel;
	}

	public JPanel getPanel1() {
		return form_panel1;
	}

	public JLayeredPane getLayeredPane2() {
		return form_layeredPane2;
	}

	public JLabel getActionLabel() {
		return form_actionLabel;
	}

	public JButton getButton1() {
		return form_button1;
	}

	public JLabel getTimeLabel() {
		return form_timeLabel;
	}

	public JPanel getHSpacer1() {
		return form_hSpacer1;
	}

	public JPanel getPanel2() {
		return form_panel2;
	}

	protected abstract void actionLabelMouseClicked(MouseEvent e);



	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
		// Generated using JFormDesigner non-commercial license
		createUIComponents();

		form_panel2 = new JPanel();
		form_nameLabel = new JLabel();
		form_button1 = new JButton();
		form_timeLabel = new JLabel();
		form_layeredPane1 = new JLayeredPane();
		form_layeredPane2 = new JLayeredPane();
		form_hSpacer1 = new JPanel(null);

		//======== this ========
		setPreferredSize(null);
		setMinimumSize(null);
		setMaximumSize(null);
		setOpaque(false);
		setLayout(new MigLayout(
			"insets 0,align left bottom",
			// columns
			"[right]" +
			"[fill]" +
			"[fill]" +
			"[fill]" +
			"[fill]",
			// rows
			"[]" +
			"[fill]" +
			"[]"));

		//======== form_panel2 ========
		{
			form_panel2.setLayout(new MigLayout(
				"insets 0,gap 0 0",
				// columns
				"[fill]" +
				"[grow,fill]",
				// rows
				"[fill]" +
				"[grow,fill]"));

			//---- form_nameLabel ----
			form_nameLabel.setText("NAME");
			form_nameLabel.setFont(form_nameLabel.getFont().deriveFont(form_nameLabel.getFont().getStyle() | Font.BOLD, form_nameLabel.getFont().getSize() + 1f));
			form_nameLabel.setEnabled(false);
			form_nameLabel.setHorizontalTextPosition(SwingConstants.LEADING);
			form_panel2.add(form_nameLabel, "cell 0 0 2 1");

			//---- form_button1 ----
			form_button1.setText(" ... ");
			form_button1.setMaximumSize(null);
			form_button1.setMinimumSize(null);
			form_button1.setPreferredSize(null);
			form_button1.setBorder(null);
			form_button1.setBorderPainted(false);
			form_button1.setRequestFocusEnabled(false);
			form_button1.setOpaque(false);
			form_button1.setForeground(null);
			form_button1.setBackground(null);
			form_button1.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					replyButtonClicked(e);
				}
			});
			form_panel2.add(form_button1, "cell 0 1");

			//---- form_timeLabel ----
			form_timeLabel.setText("TIME");
			form_timeLabel.setEnabled(false);
			form_timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
			form_panel2.add(form_timeLabel, "cell 1 1");
		}
		add(form_panel2, "cell 0 0 1 2,aligny bottom,growy 0");

		//======== form_layeredPane1 ========
		{
			form_layeredPane1.setBackground(Color.red);
			form_layeredPane1.setForeground(null);
			form_layeredPane1.setMinimumSize(null);
			form_layeredPane1.setPreferredSize(null);
		}
		add(form_layeredPane1, "cell 2 1 2 2,grow");

		//======== form_panel1 ========
		{
			form_panel1.setBackground(null);
			form_panel1.setMinimumSize(null);
			form_panel1.setPreferredSize(null);
			form_panel1.setMaximumSize(null);
			form_panel1.setLayout(new MigLayout(
				"",
				// columns
				"[7!]" +
				"[grow,fill]",
				// rows
				"[]" +
				"[]"));
		}
		add(form_panel1, "cell 2 0 1 2");

		//======== form_layeredPane2 ========
		{
			form_layeredPane2.setMinimumSize(new Dimension(25, 25));

			//---- form_actionLabel ----
			form_actionLabel.setHorizontalAlignment(SwingConstants.CENTER);
			form_actionLabel.setFont(form_actionLabel.getFont().deriveFont(form_actionLabel.getFont().getSize() + 5f));
			form_actionLabel.setPreferredSize(new Dimension(25, 25));
			form_actionLabel.setMaximumSize(null);
			form_actionLabel.setMinimumSize(null);
			form_actionLabel.setForeground(new Color(0x00000000, true));
			form_actionLabel.addMouseListener(new MouseAdapter() {
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
			form_layeredPane2.add(form_actionLabel, JLayeredPane.DEFAULT_LAYER);
			form_actionLabel.setBounds(new Rectangle(new Point(0, 0), form_actionLabel.getPreferredSize()));
		}
		add(form_layeredPane2, "cell 4 0 1 2");

		//---- form_hSpacer1 ----
		form_hSpacer1.setMaximumSize(new Dimension(10, 10));
		form_hSpacer1.setMinimumSize(new Dimension(10, 10));
		add(form_hSpacer1, "cell 1 1");
		// JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
	// Generated using JFormDesigner non-commercial license
	protected JPanel form_panel2;
	protected JLabel form_nameLabel;
	protected JButton form_button1;
	protected JLabel form_timeLabel;
	protected JLayeredPane form_layeredPane1;
	protected JPanel form_panel1;
	protected JLayeredPane form_layeredPane2;
	protected JLabel form_actionLabel;
	protected JPanel form_hSpacer1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}